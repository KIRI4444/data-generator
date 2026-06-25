package com.data.generator.api.dto.generation;

import com.data.generator.api.enums.ExportFormat;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record GenerateDataRq(
        UUID workspaceId,
        ExportFormat exportFormat,
        Integer rowsCount
) {
}