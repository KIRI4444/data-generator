package com.data.generator.api.model;

import com.data.generator.api.enums.RelationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "data_relation", schema = "data_generator")
@NoArgsConstructor
@AllArgsConstructor
public class DataRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "relation_id", nullable = false)
    private UUID relationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_table_id", nullable = false)
    private DataTable sourceTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_table_id", nullable = false)
    private DataTable targetTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_column_id", nullable = false)
    private DataColumn sourceColumn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_column_id", nullable = false)
    private DataColumn targetColumn;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", nullable = false)
    private RelationType relationType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}