package com.data.generator.api.repository;

import com.data.generator.api.model.GenerationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GenerationHistoryRepository extends JpaRepository<GenerationHistory, UUID> {

    List<GenerationHistory> findAllByWorkspaceWorkspaceIdOrderByCreatedAtDesc(UUID workspaceId);
}