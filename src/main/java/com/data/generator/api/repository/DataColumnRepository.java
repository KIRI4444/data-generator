package com.data.generator.api.repository;

import com.data.generator.api.model.DataColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DataColumnRepository extends JpaRepository<DataColumn, UUID> {

    List<DataColumn> findAllByTableTableIdOrderBySortOrderAsc(UUID tableId);

    boolean existsByTableTableIdAndName(UUID tableId, String name);
}