package com.data.generator.api.model;

import com.data.generator.api.enums.GeneratorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "data_template", schema = "data_generator")
@NoArgsConstructor
@AllArgsConstructor
public class DataTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "template_id", nullable = false)
    private UUID templateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "generator_type", nullable = false)
    private GeneratorType generatorType;

    @Column(name = "pattern", nullable = false, columnDefinition = "TEXT")
    private String pattern;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}