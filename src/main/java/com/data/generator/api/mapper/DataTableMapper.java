package com.data.generator.api.mapper;

import com.data.generator.api.dto.table.CreateDataTableRq;
import com.data.generator.api.dto.table.DataTableRs;
import com.data.generator.api.dto.table.UpdateDataTableRq;
import com.data.generator.api.model.DataTable;
import org.springframework.stereotype.Component;

@Component
public class DataTableMapper {

    public DataTable toEntity(CreateDataTableRq rq) {
        DataTable table = new DataTable();
        table.setName(rq.name());
        table.setDisplayName(rq.displayName());
        table.setRowCount(rq.rowCount());
        return table;
    }

    public void updateEntity(DataTable table, UpdateDataTableRq rq) {
        table.setName(rq.name());
        table.setDisplayName(rq.displayName());
        table.setRowCount(rq.rowCount());
    }

    public DataTableRs toRs(DataTable table) {
        return DataTableRs.builder()
                .tableId(table.getTableId())
                .workspaceId(table.getWorkspace() == null ? null : table.getWorkspace().getWorkspaceId())
                .name(table.getName())
                .displayName(table.getDisplayName())
                .rowCount(table.getRowCount())
                .createdAt(table.getCreatedAt())
                .updatedAt(table.getUpdatedAt())
                .build();
    }
}