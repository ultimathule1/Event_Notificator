package dev.sorokin.event.notificator.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "user_changed_id")
    private Long userChangedId;

    @Column(name = "message_id", nullable = false, unique = true)
    private String messageId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}