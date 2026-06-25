package com.data.generator.api.controller.impl;

import com.data.generator.api.controller.DataTableController;
import com.data.generator.api.dto.table.CreateDataTableRq;
import com.data.generator.api.dto.table.DataTableRs;
import com.data.generator.api.dto.table.UpdateDataTableRq;
import com.data.generator.api.service.DataTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DataTableControllerImpl implements DataTableController {

    private final DataTableService dataTableService;

    @Override
    public ResponseEntity<DataTableRs> create(CreateDataTableRq rq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataTableService.create(rq));
    }

    @Override
    public ResponseEntity<DataTableRs> update(UUID tableId, UpdateDataTableRq rq) {
        return ResponseEntity.ok(dataTableService.update(tableId, rq));
    }

    @Override
    public ResponseEntity<DataTableRs> getById(UUID tableId) {
        return ResponseEntity.ok(dataTableService.getById(tableId));
    }

    @Override
    public ResponseEntity<List<DataTableRs>> getByWorkspaceId(UUID workspaceId) {
        return ResponseEntity.ok(dataTableService.getByWorkspaceId(workspaceId));
    }

    @Override
    public ResponseEntity<Void> delete(UUID tableId) {
        dataTableService.delete(tableId);
        return ResponseEntity.noContent().build();
    }
}