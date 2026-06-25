package com.data.generator.api.dto.relation;

import com.data.generator.api.enums.RelationType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record DataRelationRs(
        UUID relationId,
        UUID sourceTableId,
        UUID targetTableId,
        UUID sourceColumnId,
        UUID targetColumnId,
        RelationType relationType,
        LocalDateTime createdAt
) {
}