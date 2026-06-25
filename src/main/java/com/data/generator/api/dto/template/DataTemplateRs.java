package com.data.generator.api.dto.template;

import com.data.generator.api.enums.GeneratorType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record DataTemplateRs(
        UUID templateId,
        UUID workspaceId,
        String name,
        GeneratorType generatorType,
        String pattern,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}