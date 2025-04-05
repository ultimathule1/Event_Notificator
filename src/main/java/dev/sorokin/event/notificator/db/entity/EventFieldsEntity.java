package dev.sorokin.event.notificator.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "event_fields")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventFieldsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq")
    @SequenceGenerator(name = "id_seq", sequenceName = "event_fields_id_seq")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "max_places")
    private Integer maxPlaces;
    @Column(name = "date")
    private OffsetDateTime date;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "duration")
    private Integer duration;
    @Column(name = "location_id")
    private Long locationId;
    @Column(name = "status")
    private String status;

    public EventFieldsEntity(EventFieldsAuditEntity auditEntity) {
        this.name = auditEntity.getName();
        this.maxPlaces = auditEntity.getMaxPlaces();
        this.date = auditEntity.getDate();
        this.cost = auditEntity.getCost();
        this.duration = auditEntity.getDuration();
        this.locationId = auditEntity.getLocationId();
        this.status = auditEntity.getStatus();
    }
}
