package com.data.generator.api.repository;

import com.data.generator.api.model.DataTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DataTableRepository extends JpaRepository<DataTable, UUID> {

    List<DataTable> findAllByWorkspaceWorkspaceId(UUID workspaceId);

    boolean existsByWorkspaceWorkspaceIdAndName(UUID workspaceId, String name);
}