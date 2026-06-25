package com.data.generator.api.model;

import com.data.generator.api.enums.ExportFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "generation_history", schema = "data_generator")
@NoArgsConstructor
@AllArgsConstructor
public class GenerationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "generation_history_id", nullable = false)
    private UUID generationHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    @Column(name = "export_format", nullable = false)
    private ExportFormat exportFormat;

    @Column(name = "tables_count", nullable = false)
    private Integer tablesCount;

    @Column(name = "rows_count", nullable = false)
    private Integer rowsCount;

    @Column(name = "result_data", nullable = false, columnDefinition = "TEXT")
    private String resultData;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}