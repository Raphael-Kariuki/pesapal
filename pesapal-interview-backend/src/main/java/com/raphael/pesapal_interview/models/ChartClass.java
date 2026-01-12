package com.raphael.pesapal_interview.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "chart_classes")
public class ChartClass extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chart_classes_id_gen")
    @SequenceGenerator(name = "chart_classes_id_gen", sequenceName = "chart_classes_oid_seq", allocationSize = 1)
    @Column(name = "oid", nullable = false)
    private Long oid;

    @Column(name = "class_name", nullable = false, length = 100)
    private String className;

    @Column(name = "class_code", nullable = false, length = 100)
    private String classCode;

    @Column(name = "class_type", nullable = false, length = 100)
    private String classType;


    @Column(name = "version")
    private Integer version;

    @Embedded
    private CommonEntityAttributes commonEntityAttributes;
}