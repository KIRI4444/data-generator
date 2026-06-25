package com.data.generator.api.dto.table;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CreateDataTableRq(
        UUID workspaceId,
        String name,
        String displayName,
        Integer rowCount
) {
}