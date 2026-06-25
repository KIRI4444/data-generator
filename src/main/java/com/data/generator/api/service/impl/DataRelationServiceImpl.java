package com.data.generator.api.service.impl;

import com.data.generator.api.dto.relation.CreateDataRelationRq;
import com.data.generator.api.dto.relation.DataRelationRs;
import com.data.generator.api.exception.EntityNotFoundException;
import com.data.generator.api.mapper.DataRelationMapper;
import com.data.generator.api.model.DataColumn;
import com.data.generator.api.model.DataRelation;
import com.data.generator.api.model.DataTable;
import com.data.generator.api.repository.DataColumnRepository;
import com.data.generator.api.repository.DataRelationRepository;
import com.data.generator.api.repository.DataTableRepository;
import com.data.generator.api.service.DataRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataRelationServiceImpl implements DataRelationService {

    private final DataRelationRepository dataRelationRepository;
    private final DataTableRepository dataTableRepository;
    private final DataColumnRepository dataColumnRepository;
    private final DataRelationMapper dataRelationMapper;

    @Override
    @Transactional
    public DataRelationRs create(CreateDataRelationRq rq) {
        DataTable sourceTable = dataTableRepository.findById(rq.sourceTableId())
                .orElseThrow(() -> new EntityNotFoundException("Source table not found: " + rq.sourceTableId()));

        DataTable targetTable = dataTableRepository.findById(rq.targetTableId())
                .orElseThrow(() -> new EntityNotFoundException("Target table not found: " + rq.targetTableId()));

        DataColumn sourceColumn = dataColumnRepository.findById(rq.sourceColumnId())
                .orElseThrow(() -> new EntityNotFoundException("Source column not found: " + rq.sourceColumnId()));

        DataColumn targetColumn = dataColumnRepository.findById(rq.targetColumnId())
                .orElseThrow(() -> new EntityNotFoundException("Target column not found: " + rq.targetColumnId()));

        validateColumnBelongsToTable(sourceColumn, sourceTable.getTableId(), "Source column does not belong to source table");
        validateColumnBelongsToTable(targetColumn, targetTable.getTableId(), "Target column does not belong to target table");
        validateTablesInSameWorkspace(sourceTable, targetTable);

        DataRelation relation = dataRelationMapper.toEntity(rq);
        relation.setSourceTable(sourceTable);
        relation.setTargetTable(targetTable);
        relation.setSourceColumn(sourceColumn);
        relation.setTargetColumn(targetColumn);
        relation.setCreatedAt(LocalDateTime.now());

        DataRelation savedRelation = dataRelationRepository.save(relation);
        return dataRelationMapper.toRs(savedRelation);
    }

    @Override
    @Transactional(readOnly = true)
    public DataRelationRs getById(UUID relationId) {
        DataRelation relation = getRelationOrThrow(relationId);
        return dataRelationMapper.toRs(relation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataRelationRs> getBySourceTableId(UUID sourceTableId) {
        if (!dataTableRepository.existsById(sourceTableId)) {
            throw new EntityNotFoundException("Source table not found: " + sourceTableId);
        }

        return dataRelationRepository.findAllBySourceTableTableId(sourceTableId)
                .stream()
                .map(dataRelationMapper::toRs)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataRelationRs> getByTargetTableId(UUID targetTableId) {
        if (!dataTableRepository.existsById(targetTableId)) {
            throw new EntityNotFoundException("Target table not found: " + targetTableId);
        }

        return dataRelationRepository.findAllByTargetTableTableId(targetTableId)
                .stream()
                .map(dataRelationMapper::toRs)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataRelationRs> getByWorkspaceId(UUID workspaceId) {
        return dataRelationRepository.findAllBySourceTableWorkspaceWorkspaceId(workspaceId)
                .stream()
                .map(dataRelationMapper::toRs)
                .toList();
    }

    @Override
    @Transactional
    public void delete(UUID relationId) {
        DataRelation relation = getRelationOrThrow(relationId);
        dataRelationRepository.delete(relation);
    }

    private DataRelation getRelationOrThrow(UUID relationId) {
        return dataRelationRepository.findById(relationId)
                .orElseThrow(() -> new EntityNotFoundException("Data relation not found: " + relationId));
    }

    private void validateColumnBelongsToTable(DataColumn column, UUID tableId, String message) {
        if (column.getTable() == null || !column.getTable().getTableId().equals(tableId)) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateTablesInSameWorkspace(DataTable sourceTable, DataTable targetTable) {
        UUID sourceWorkspaceId = sourceTable.getWorkspace().getWorkspaceId();
        UUID targetWorkspaceId = targetTable.getWorkspace().getWorkspaceId();

        if (!sourceWorkspaceId.equals(targetWorkspaceId)) {
            throw new IllegalArgumentException("Relation tables must belong to the same workspace");
        }
    }
}