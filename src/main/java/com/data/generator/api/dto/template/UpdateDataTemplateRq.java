package com.data.generator.api.dto.template;

import com.data.generator.api.enums.GeneratorType;
import lombok.Builder;

@Builder(toBuilder = true)
public record UpdateDataTemplateRq(
        String name,
        GeneratorType generatorType,
        String pattern,
        String description
) {
}