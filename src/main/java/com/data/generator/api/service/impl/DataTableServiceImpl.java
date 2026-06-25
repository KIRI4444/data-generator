package com.data.generator.api.service.impl;

import com.data.generator.api.dto.table.CreateDataTableRq;
import com.data.generator.api.dto.table.DataTableRs;
import com.data.generator.api.dto.table.UpdateDataTableRq;
import com.data.generator.api.exception.EntityNotFoundException;
import com.data.generator.api.mapper.DataTableMapper;
import com.data.generator.api.model.DataTable;
import com.data.generator.api.model.Workspace;
import com.data.generator.api.repository.DataTableRepository;
import com.data.generator.api.repository.WorkspaceRepository;
import com.data.generator.api.service.DataTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataTableServiceImpl implements DataTableService {

    private final DataTableRepository dataTableRepository;
    private final WorkspaceRepository workspaceRepository;
    private final DataTableMapper dataTableMapper;

    @Override
    @Transactional
    public DataTableRs create(CreateDataTableRq rq) {
        Workspace workspace = workspaceRepository.findById(rq.workspaceId())
                .orElseThrow(() -> new EntityNotFoundException("Workspace not found: " + rq.workspaceId()));

        if (dataTableRepository.existsByWorkspaceWorkspaceIdAndName(rq.workspaceId(), rq.name())) {
            throw new IllegalArgumentException("Data table already exists in workspace: " + rq.name());
        }

        DataTable table = dataTableMapper.toEntity(rq);
        table.setWorkspace(workspace);
        table.setCreatedAt(LocalDateTime.now());
        table.setUpdatedAt(LocalDateTime.now());

        DataTable savedTable = dataTableRepository.save(table);
        return dataTableMapper.toRs(savedTable);
    }

    @Override
    @Transactional
    public DataTableRs update(UUID tableId, UpdateDataTableRq rq) {
        DataTable table = getTableOrThrow(tableId);

        UUID workspaceId = table.getWorkspace().getWorkspaceId();

        if (!Objects.equals(table.getName(), rq.name())
                && dataTableRepository.existsByWorkspaceWorkspaceIdAndName(workspaceId, rq.name())) {
            throw new IllegalArgumentException("Data table already exists in workspace: " + rq.name());
        }

        dataTableMapper.updateEntity(table, rq);
        table.setUpdatedAt(LocalDateTime.now());

        DataTable savedTable = dataTableRepository.save(table);
        return dataTableMapper.toRs(savedTable);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTableRs getById(UUID tableId) {
        DataTable table = getTableOrThrow(tableId);
        return dataTableMapper.toRs(table);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataTableRs> getByWorkspaceId(UUID workspaceId) {
        if (!workspaceRepository.existsById(workspaceId)) {
            throw new EntityNotFoundException("Workspace not found: " + workspaceId);
        }

        return dataTableRepository.findAllByWorkspaceWorkspaceId(workspaceId)
                .stream()
                .map(dataTableMapper::toRs)
                .toList();
    }

    @Override
    @Transactional
    public void delete(UUID tableId) {
        DataTable table = getTableOrThrow(tableId);
        dataTableRepository.delete(table);
    }

    private DataTable getTableOrThrow(UUID tableId) {
        return dataTableRepository.findById(tableId)
                .orElseThrow(() -> new EntityNotFoundException("Data table not found: " + tableId));
    }
}