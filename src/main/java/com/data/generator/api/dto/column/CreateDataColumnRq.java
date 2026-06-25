package com.data.generator.api.dto.column;

import com.data.generator.api.enums.ColumnType;
import com.data.generator.api.enums.GeneratorType;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CreateDataColumnRq(
        UUID tableId,
        String name,
        String displayName,
        ColumnType columnType,
        GeneratorType generatorType,
        Boolean nullable,
        Boolean uniqueValue,
        Boolean primaryKey,
        String minValue,
        String maxValue,
        String fixedValue,
        UUID templateId,
        Integer sortOrder
) {
}