package com.data.generator.api.controller.impl;

import com.data.generator.api.controller.DataColumnController;
import com.data.generator.api.dto.column.CreateDataColumnRq;
import com.data.generator.api.dto.column.DataColumnRs;
import com.data.generator.api.dto.column.UpdateDataColumnRq;
import com.data.generator.api.service.DataColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DataColumnControllerImpl implements DataColumnController {

    private final DataColumnService dataColumnService;

    @Override
    public ResponseEntity<DataColumnRs> create(CreateDataColumnRq rq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataColumnService.create(rq));
    }

    @Override
    public ResponseEntity<DataColumnRs> update(UUID columnId, UpdateDataColumnRq rq) {
        return ResponseEntity.ok(dataColumnService.update(columnId, rq));
    }

    @Override
    public ResponseEntity<DataColumnRs> getById(UUID columnId) {
        return ResponseEntity.ok(dataColumnService.getById(columnId));
    }

    @Override
    public ResponseEntity<List<DataColumnRs>> getByTableId(UUID tableId) {
        return ResponseEntity.ok(dataColumnService.getByTableId(tableId));
    }

    @Override
    public ResponseEntity<Void> delete(UUID columnId) {
        dataColumnService.delete(columnId);
        return ResponseEntity.noContent().build();
    }
}