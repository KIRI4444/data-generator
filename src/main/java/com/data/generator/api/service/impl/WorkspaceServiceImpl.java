package com.data.generator.api.service.impl;

import com.data.generator.api.dto.workspace.CreateWorkspaceRq;
import com.data.generator.api.dto.workspace.UpdateWorkspaceRq;
import com.data.generator.api.dto.workspace.WorkspaceRs;
import com.data.generator.api.exception.EntityNotFoundException;
import com.data.generator.api.mapper.WorkspaceMapper;
import com.data.generator.api.model.Workspace;
import com.data.generator.api.repository.WorkspaceRepository;
import com.data.generator.api.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;

    @Override
    @Transactional
    public WorkspaceRs create(CreateWorkspaceRq rq) {
        Workspace workspace = workspaceMapper.toEntity(rq);
        workspace.setCreatedAt(LocalDateTime.now());
        workspace.setUpdatedAt(LocalDateTime.now());

        Workspace savedWorkspace = workspaceRepository.save(workspace);
        return workspaceMapper.toRs(savedWorkspace);
    }

    @Override
    @Transactional
    public WorkspaceRs update(UUID workspaceId, UpdateWorkspaceRq rq) {
        Workspace workspace = getWorkspaceOrThrow(workspaceId);

        workspaceMapper.updateEntity(workspace, rq);
        workspace.setUpdatedAt(LocalDateTime.now());

        Workspace savedWorkspace = workspaceRepository.save(workspace);
        return workspaceMapper.toRs(savedWorkspace);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkspaceRs getById(UUID workspaceId) {
        Workspace workspace = getWorkspaceOrThrow(workspaceId);
        return workspaceMapper.toRs(workspace);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkspaceRs> getAll() {
        return workspaceRepository.findAll()
                .stream()
                .map(workspaceMapper::toRs)
                .toList();
    }

    @Override
    @Transactional
    public void delete(UUID workspaceId) {
        Workspace workspace = getWorkspaceOrThrow(workspaceId);
        workspaceRepository.delete(workspace);
    }

    private Workspace getWorkspaceOrThrow(UUID workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("Workspace not found: " + workspaceId));
    }
}