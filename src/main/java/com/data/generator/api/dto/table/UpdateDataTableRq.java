package com.data.generator.api.dto.table;

import lombok.Builder;

@Builder(toBuilder = true)
public record UpdateDataTableRq(
        String name,
        String displayName,
        Integer rowCount
) {
}