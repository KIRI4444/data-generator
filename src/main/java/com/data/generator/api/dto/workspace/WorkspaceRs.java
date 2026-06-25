package com.data.generator.api.dto.workspace;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record WorkspaceRs(
        UUID workspaceId,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}