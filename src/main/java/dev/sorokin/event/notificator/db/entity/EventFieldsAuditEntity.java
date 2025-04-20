package dev.sorokin.event.notificator.db.entity;

import dev.sorokin.event.notificator.db.converter.ZoneOffsetConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "event_fields_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFieldsAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_seq")
    @SequenceGenerator(name = "audit_seq", sequenceName = "audit_id")
    private Long auditId;

    @Column(name = "event_fields_id")
    private Long eventFieldsId;

    @Column(name = "name")
    private String name;
    @Column(name = "max_places")
    private Integer maxPlaces;
    @Column(name = "date")
    private OffsetDateTime date;
    @Convert(converter = ZoneOffsetConverter.class)
    @Column(name = "offset_date")
    private ZoneOffset offsetDate;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "duration")
    private Integer duration;
    @Column(name = "location_id")
    private Long locationId;
    @Column(name = "status")
    private String status;

    @Column(name = "changed_at")
    @LastModifiedDate
    private LocalDateTime changedAt;
    @Column(name = "type_operation")
    private String typeOperation;
    @Column(name = "changed_by")
    private Long changedBy;
}
