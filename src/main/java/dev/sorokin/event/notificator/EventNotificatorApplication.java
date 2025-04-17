package dev.sorokin.event.notificator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EventNotificatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventNotificatorApplication.class, args);
    }

}