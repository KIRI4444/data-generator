package com.data.generator.api.mapper;

import com.data.generator.api.dto.column.CreateDataColumnRq;
import com.data.generator.api.dto.column.DataColumnRs;
import com.data.generator.api.dto.column.UpdateDataColumnRq;
import com.data.generator.api.model.DataColumn;
import org.springframework.stereotype.Component;

@Component
public class DataColumnMapper {

    public DataColumn toEntity(CreateDataColumnRq rq) {
        DataColumn column = new DataColumn();
        column.setName(rq.name());
        column.setDisplayName(rq.displayName());
        column.setColumnType(rq.columnType());
        column.setGeneratorType(rq.generatorType());
        column.setNullable(rq.nullable());
        column.setUniqueValue(rq.uniqueValue());
        column.setPrimaryKey(rq.primaryKey());
        column.setMinValue(rq.minValue());
        column.setMaxValue(rq.maxValue());
        column.setFixedValue(rq.fixedValue());
        column.setTemplateId(rq.templateId());
        column.setSortOrder(rq.sortOrder());
        return column;
    }

    public void updateEntity(DataColumn column, UpdateDataColumnRq rq) {
        column.setName(rq.name());
        column.setDisplayName(rq.displayName());
        column.setColumnType(rq.columnType());
        column.setGeneratorType(rq.generatorType());
        column.setNullable(rq.nullable());
        column.setUniqueValue(rq.uniqueValue());
        column.setPrimaryKey(rq.primaryKey());
        column.setMinValue(rq.minValue());
        column.setMaxValue(rq.maxValue());
        column.setFixedValue(rq.fixedValue());
        column.setTemplateId(rq.templateId());
        column.setSortOrder(rq.sortOrder());
    }

    public DataColumnRs toRs(DataColumn column) {
        return DataColumnRs.builder()
                .columnId(column.getColumnId())
                .tableId(column.getTable() == null ? null : column.getTable().getTableId())
                .name(column.getName())
                .displayName(column.getDisplayName())
                .columnType(column.getColumnType())
                .generatorType(column.getGeneratorType())
                .nullable(column.getNullable())
                .uniqueValue(column.getUniqueValue())
                .primaryKey(column.getPrimaryKey())
                .minValue(column.getMinValue())
                .maxValue(column.getMaxValue())
                .fixedValue(column.getFixedValue())
                .templateId(column.getTemplateId())
                .sortOrder(column.getSortOrder())
                .createdAt(column.getCreatedAt())
                .updatedAt(column.getUpdatedAt())
                .build();
    }
}