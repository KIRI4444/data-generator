package com.data.generator.api.mapper;

import com.data.generator.api.dto.relation.CreateDataRelationRq;
import com.data.generator.api.dto.relation.DataRelationRs;
import com.data.generator.api.model.DataRelation;
import org.springframework.stereotype.Component;

@Component
public class DataRelationMapper {

    public DataRelation toEntity(CreateDataRelationRq rq) {
        DataRelation relation = new DataRelation();
        relation.setRelationType(rq.relationType());
        return relation;
    }

    public DataRelationRs toRs(DataRelation relation) {
        return DataRelationRs.builder()
                .relationId(relation.getRelationId())
                .sourceTableId(relation.getSourceTable() == null ? null : relation.getSourceTable().getTableId())
                .targetTableId(relation.getTargetTable() == null ? null : relation.getTargetTable().getTableId())
                .sourceColumnId(relation.getSourceColumn() == null ? null : relation.getSourceColumn().getColumnId())
                .targetColumnId(relation.getTargetColumn() == null ? null : relation.getTargetColumn().getColumnId())
                .relationType(relation.getRelationType())
                .createdAt(relation.getCreatedAt())
                .build();
    }
}