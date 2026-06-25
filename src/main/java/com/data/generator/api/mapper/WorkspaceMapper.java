package com.data.generator.api.mapper;

import com.data.generator.api.dto.workspace.CreateWorkspaceRq;
import com.data.generator.api.dto.workspace.UpdateWorkspaceRq;
import com.data.generator.api.dto.workspace.WorkspaceRs;
import com.data.generator.api.model.Workspace;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceMapper {

    public Workspace toEntity(CreateWorkspaceRq rq) {
        Workspace workspace = new Workspace();
        workspace.setName(rq.name());
        workspace.setDescription(rq.description());
        return workspace;
    }

    public void updateEntity(Workspace workspace, UpdateWorkspaceRq rq) {
        workspace.setName(rq.name());
        workspace.setDescription(rq.description());
    }

    public WorkspaceRs toRs(Workspace workspace) {
        return WorkspaceRs.builder()
                .workspaceId(workspace.getWorkspaceId())
                .name(workspace.getName())
                .description(workspace.getDescription())
                .createdAt(workspace.getCreatedAt())
                .updatedAt(workspace.getUpdatedAt())
                .build();
    }
}