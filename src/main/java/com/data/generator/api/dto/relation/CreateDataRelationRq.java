package com.data.generator.api.dto.relation;

import com.data.generator.api.enums.RelationType;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CreateDataRelationRq(
        UUID sourceTableId,
        UUID targetTableId,
        UUID sourceColumnId,
        UUID targetColumnId,
        RelationType relationType
) {
}