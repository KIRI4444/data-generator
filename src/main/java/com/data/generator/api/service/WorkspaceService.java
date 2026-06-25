package com.data.generator.api.service;

import com.data.generator.api.dto.workspace.CreateWorkspaceRq;
import com.data.generator.api.dto.workspace.UpdateWorkspaceRq;
import com.data.generator.api.dto.workspace.WorkspaceRs;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {

    WorkspaceRs create(CreateWorkspaceRq rq);

    WorkspaceRs update(UUID workspaceId, UpdateWorkspaceRq rq);

    WorkspaceRs getById(UUID workspaceId);

    List<WorkspaceRs> getAll();

    void delete(UUID workspaceId);
}