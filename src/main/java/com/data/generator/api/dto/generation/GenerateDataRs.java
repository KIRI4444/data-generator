package com.data.generator.api.dto.generation;

import com.data.generator.api.enums.ExportFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
public record GenerateDataRs(
        UUID generationHistoryId,
        UUID workspaceId,
        ExportFormat exportFormat,
        Integer tablesCount,
        Integer rowsCount,
        String resultData,
        List<GeneratedTableData> tables,
        LocalDateTime createdAt
) {
}