package com.raphael.pesapal_interview.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "chart_types", schema = "finance", catalog = "pesapal")
public class ChartType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chart_types_id_gen")
    @SequenceGenerator(name = "chart_types_id_gen", sequenceName = "finance.chart_types_oid_seq", allocationSize = 1)
    @Column(name = "oid", nullable = false)
    private Long id;

    @Column(name = "chart_type_name", nullable = false, length = 100)
    private String chartTypeName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chart_class_id", nullable = false)
    private ChartClass chartClass;

    @Column(name = "type_code", nullable = false, length = 100)
    private String typeCode;

    @ColumnDefault("0")
    @Column(name = "parent_id", nullable = false)
    private Long parentId;


    @Column(name = "version")
    private Integer version;

    @Embedded
    private CommonEntityAttributes commonEntityAttributes;

}