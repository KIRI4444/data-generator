package com.data.generator.api.dto.template;

import com.data.generator.api.enums.GeneratorType;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CreateDataTemplateRq(
        UUID workspaceId,
        String name,
        GeneratorType generatorType,
        String pattern,
        String description
) {
}