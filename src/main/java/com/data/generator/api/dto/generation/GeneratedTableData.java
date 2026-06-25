package com.data.generator.api.dto.generation;

import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder(toBuilder = true)
public record GeneratedTableData(
        UUID tableId,
        String tableName,
        List<Map<String, Object>> rows
) {
}