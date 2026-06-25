package com.data.generator.api.service.impl;

import com.data.generator.api.dto.column.CreateDataColumnRq;
import com.data.generator.api.dto.column.DataColumnRs;
import com.data.generator.api.dto.column.UpdateDataColumnRq;
import com.data.generator.api.exception.EntityNotFoundException;
import com.data.generator.api.mapper.DataColumnMapper;
import com.data.generator.api.model.DataColumn;
import com.data.generator.api.model.DataTable;
import com.data.generator.api.repository.DataColumnRepository;
import com.data.generator.api.repository.DataTableRepository;
import com.data.generator.api.service.DataColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataColumnServiceImpl implements DataColumnService {

    private final DataColumnRepository dataColumnRepository;
    private final DataTableRepository dataTableRepository;
    private final DataColumnMapper dataColumnMapper;

    @Override
    @Transactional
    public DataColumnRs create(CreateDataColumnRq rq) {
        DataTable table = dataTableRepository.findById(rq.tableId())
                .orElseThrow(() -> new EntityNotFoundException("Data table not found: " + rq.tableId()));

        if (dataColumnRepository.existsByTableTableIdAndName(rq.tableId(), rq.name())) {
            throw new IllegalArgumentException("Data column already exists in table: " + rq.name());
        }

        DataColumn column = dataColumnMapper.toEntity(rq);
        column.setTable(table);
        column.setCreatedAt(LocalDateTime.now());
        column.setUpdatedAt(LocalDateTime.now());

        DataColumn savedColumn = dataColumnRepository.save(column);
        return dataColumnMapper.toRs(savedColumn);
    }

    @Override
    @Transactional
    public DataColumnRs update(UUID columnId, UpdateDataColumnRq rq) {
        DataColumn column = getColumnOrThrow(columnId);

        UUID tableId = column.getTable().getTableId();

        if (!Objects.equals(column.getName(), rq.name())
                && dataColumnRepository.existsByTableTableIdAndName(tableId, rq.name())) {
            throw new IllegalArgumentException("Data column already exists in table: " + rq.name());
        }

        dataColumnMapper.updateEntity(column, rq);
        column.setUpdatedAt(LocalDateTime.now());

        DataColumn savedColumn = dataColumnRepository.save(column);
        return dataColumnMapper.toRs(savedColumn);
    }

    @Override
    @Transactional(readOnly = true)
    public DataColumnRs getById(UUID columnId) {
        DataColumn column = getColumnOrThrow(columnId);
        return dataColumnMapper.toRs(column);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataColumnRs> getByTableId(UUID tableId) {
        if (!dataTableRepository.existsById(tableId)) {
            throw new EntityNotFoundException("Data table not found: " + tableId);
        }

        return dataColumnRepository.findAllByTableTableIdOrderBySortOrderAsc(tableId)
                .stream()
                .map(dataColumnMapper::toRs)
                .toList();
    }

    @Override
    @Transactional
    public void delete(UUID columnId) {
        DataColumn column = getColumnOrThrow(columnId);
        dataColumnRepository.delete(column);
    }

    private DataColumn getColumnOrThrow(UUID columnId) {
        return dataColumnRepository.findById(columnId)
                .orElseThrow(() -> new EntityNotFoundException("Data column not found: " + columnId));
    }
}