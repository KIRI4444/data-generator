package com.data.generator.api.controller.impl;

import com.data.generator.api.controller.WorkspaceController;
import com.data.generator.api.dto.workspace.CreateWorkspaceRq;
import com.data.generator.api.dto.workspace.UpdateWorkspaceRq;
import com.data.generator.api.dto.workspace.WorkspaceRs;
import com.data.generator.api.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class WorkspaceControllerImpl implements WorkspaceController {

    private final WorkspaceService workspaceService;

    @Override
    public ResponseEntity<WorkspaceRs> create(CreateWorkspaceRq rq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workspaceService.create(rq));
    }

    @Override
    public ResponseEntity<WorkspaceRs> update(UUID workspaceId, UpdateWorkspaceRq rq) {
        return ResponseEntity.ok(workspaceService.update(workspaceId, rq));
    }

    @Override
    public ResponseEntity<WorkspaceRs> getById(UUID workspaceId) {
        return ResponseEntity.ok(workspaceService.getById(workspaceId));
    }

    @Override
    public ResponseEntity<List<WorkspaceRs>> getAll() {
        return ResponseEntity.ok(workspaceService.getAll());
    }

    @Override
    public ResponseEntity<Void> delete(UUID workspaceId) {
        workspaceService.delete(workspaceId);
        return ResponseEntity.noContent().build();
    }
}