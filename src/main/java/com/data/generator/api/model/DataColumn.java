package com.data.generator.api.model;

import com.data.generator.api.enums.ColumnType;
import com.data.generator.api.enums.GeneratorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "data_column", schema = "data_generator")
@NoArgsConstructor
@AllArgsConstructor
public class DataColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "column_id", nullable = false)
    private UUID columnId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private DataTable table;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "column_type", nullable = false)
    private ColumnType columnType;

    @Enumerated(EnumType.STRING)
    @Column(name = "generator_type", nullable = false)
    private GeneratorType generatorType;

    @Column(name = "nullable", nullable = false)
    private Boolean nullable;

    @Column(name = "unique_value", nullable = false)
    private Boolean uniqueValue;

    @Column(name = "primary_key", nullable = false)
    private Boolean primaryKey;

    @Column(name = "min_value")
    private String minValue;

    @Column(name = "max_value")
    private String maxValue;

    @Column(name = "fixed_value")
    private String fixedValue;

    @Column(name = "template_id")
    private UUID templateId;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}