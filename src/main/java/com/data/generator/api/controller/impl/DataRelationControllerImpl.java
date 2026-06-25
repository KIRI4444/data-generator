package com.data.generator.api.controller.impl;

import com.data.generator.api.controller.DataRelationController;
import com.data.generator.api.dto.relation.CreateDataRelationRq;
import com.data.generator.api.dto.relation.DataRelationRs;
import com.data.generator.api.service.DataRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DataRelationControllerImpl implements DataRelationController {

    private final DataRelationService dataRelationService;

    @Override
    public ResponseEntity<DataRelationRs> create(CreateDataRelationRq rq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataRelationService.create(rq));
    }

    @Override
    public ResponseEntity<DataRelationRs> getById(UUID relationId) {
        return ResponseEntity.ok(dataRelationService.getById(relationId));
    }

    @Override
    public ResponseEntity<List<DataRelationRs>> getBySourceTableId(UUID sourceTableId) {
        return ResponseEntity.ok(dataRelationService.getBySourceTableId(sourceTableId));
    }

    @Override
    public ResponseEntity<List<DataRelationRs>> getByTargetTableId(UUID targetTableId) {
        return ResponseEntity.ok(dataRelationService.getByTargetTableId(targetTableId));
    }

    @Override
    public ResponseEntity<List<DataRelationRs>> getByWorkspaceId(UUID workspaceId) {
        return ResponseEntity.ok(dataRelationService.getByWorkspaceId(workspaceId));
    }

    @Override
    public ResponseEntity<Void> delete(UUID relationId) {
        dataRelationService.delete(relationId);
        return ResponseEntity.noContent().build();
    }
}