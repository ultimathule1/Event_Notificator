package dev.sorokin.event.notificator.config;

import dev.sorokin.event.notificator.api.dto.event.EventChangerEvent;
import dev.sorokin.event.notificator.exception.kafka.NotRetryableException;
import dev.sorokin.event.notificator.exception.kafka.RetryableException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    Environment env;

    public KafkaConfig(Environment env) {
        this.env = env;
    }

    @Bean
    ConsumerFactory<Long, EventChangerEvent> consumerFactory(
            KafkaProperties kafkaProperties
    ) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties(new DefaultSslBundleRegistry());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.LongDeserializer.class);
        var factory = new DefaultKafkaConsumerFactory<Long, EventChangerEvent>(props);
        factory.setValueDeserializer(new JsonDeserializer<>(EventChangerEvent.class, false));
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<Long, EventChangerEvent> kafkaListenerContainerFactory(
            ConsumerFactory<Long, EventChangerEvent> consumerFactory,
            KafkaTemplate<Long, Object> kafkaTemplateForDlt
    ) {
        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplateForDlt),
                new FixedBackOff(Integer.parseInt(env.getProperty("dlt.fixed-back-off.interval", "5000")),
                        Integer.parseInt(env.getProperty("dlt.fixed-back-off.attempts", "3"))));
        defaultErrorHandler.addNotRetryableExceptions(NotRetryableException.class);
        defaultErrorHandler.addRetryableExceptions(RetryableException.class);

        ConcurrentKafkaListenerContainerFactory<Long, EventChangerEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(defaultErrorHandler);

        return factory;
    }

    @Bean
    KafkaTemplate<Long, Object> kafkaTemplateForDlt(ProducerFactory<Long, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * This ProducerFactory sends messages that could not be processed to a separate dlt topic
     */
    @Bean
    ProducerFactory<Long, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.consumer.bootstrap-servers"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }
}
