package com.data.generator.api.controller;

import com.data.generator.api.dto.workspace.CreateWorkspaceRq;
import com.data.generator.api.dto.workspace.UpdateWorkspaceRq;
import com.data.generator.api.dto.workspace.WorkspaceRs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/workspaces")
public interface WorkspaceController {

    @PostMapping
    ResponseEntity<WorkspaceRs> create(@RequestBody CreateWorkspaceRq rq);

    @PutMapping("/{workspaceId}")
    ResponseEntity<WorkspaceRs> update(
            @PathVariable UUID workspaceId,
            @RequestBody UpdateWorkspaceRq rq
    );

    @GetMapping("/{workspaceId}")
    ResponseEntity<WorkspaceRs> getById(@PathVariable UUID workspaceId);

    @GetMapping
    ResponseEntity<List<WorkspaceRs>> getAll();

    @DeleteMapping("/{workspaceId}")
    ResponseEntity<Void> delete(@PathVariable UUID workspaceId);
}