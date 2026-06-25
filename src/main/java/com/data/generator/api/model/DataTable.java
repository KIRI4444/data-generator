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
@Table(name = "data_table", schema = "data_generator")
@NoArgsConstructor
@AllArgsConstructor
public class DataTable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "table_id", nullable = false)
    private UUID tableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "row_count", nullable = false)
    private Integer rowCount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataColumn> columns = new ArrayList<>();

    @OneToMany(mappedBy = "sourceTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataRelation> outgoingRelations = new ArrayList<>();

    @OneToMany(mappedBy = "targetTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataRelation> incomingRelations = new ArrayList<>();
}