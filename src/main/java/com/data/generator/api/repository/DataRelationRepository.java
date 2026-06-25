package com.data.generator.api.repository;

import com.data.generator.api.model.DataRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DataRelationRepository extends JpaRepository<DataRelation, UUID> {

    List<DataRelation> findAllBySourceTableTableId(UUID sourceTableId);

    List<DataRelation> findAllByTargetTableTableId(UUID targetTableId);

    List<DataRelation> findAllBySourceTableWorkspaceWorkspaceId(UUID workspaceId);
}