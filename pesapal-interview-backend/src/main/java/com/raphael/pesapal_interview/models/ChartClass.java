package com.raphael.pesapal_interview.models;

import com.raphael.pesapal_interview.dto.ChartClassDTOs;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@DynamicUpdate
@Table(name = "chart_classes",catalog = "pesapal", schema = "finance")
public class ChartClass extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chart_classes_id_gen")
    @SequenceGenerator(name = "chart_classes_id_gen", sequenceName = "finance.chart_classes_oid_seq", allocationSize = 1)
    @Column(name = "oid", nullable = false)
    private Long oid;

    @Column(name = "class_name", nullable = false, length = 100)
    private String className;

    @Column(name = "class_code", nullable = false, length = 100)
    private String classCode;

    @Column(name = "class_type", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private ChartClassDTOs.ChartClassTypeEnum classType;


    @Column(name = "version")
    private Integer version;

    @Embedded
    private CommonEntityAttributes commonEntityAttributes;
}