package com.data.generator.api.dto.workspace;

import lombok.Builder;

@Builder(toBuilder = true)
public record UpdateWorkspaceRq(
        String name,
        String description
) {
}