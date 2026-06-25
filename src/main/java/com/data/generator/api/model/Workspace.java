package com.data.generator.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "workspace", schema = "data_generator")
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataTable> tables = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataTemplate> templates = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GenerationHistory> generationHistory = new ArrayList<>();
}