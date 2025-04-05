package dev.sorokin.event.notificator.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users_notifications")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "graph.withoutFields"
        ),
        @NamedEntityGraph(
                name = "graph.withAllFields",
                attributeNodes = {
                        @NamedAttributeNode("fieldsEntity")
                }
        )
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationChangedEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "subscriber_id")
    private Long subscriberId;
    @Column(name = "user_changed_id")
    private Long userChangedId;
    @Column(name = "event_id", nullable = false)
    private Long eventId;
    @Column(name = "owner_event_id", nullable = false)
    private Long ownerEventId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "changed_event_fields_id", referencedColumnName = "id")
    EventFieldsEntity fieldsEntity;
}