package com.data.generator.api.dto.table;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record DataTableRs(
        UUID tableId,
        UUID workspaceId,
        String name,
        String displayName,
        Integer rowCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}