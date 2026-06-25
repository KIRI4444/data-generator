package com.data.generator.api.dto.workspace;

import lombok.Builder;

@Builder(toBuilder = true)
public record CreateWorkspaceRq(
        String name,
        String description
) {
}