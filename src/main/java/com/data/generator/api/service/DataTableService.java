package com.data.generator.api.service;

import com.data.generator.api.dto.table.CreateDataTableRq;
import com.data.generator.api.dto.table.UpdateDataTableRq;
import com.data.generator.api.dto.table.DataTableRs;

import java.util.List;
import java.util.UUID;

public interface DataTableService {

    DataTableRs create(CreateDataTableRq rq);

    DataTableRs update(UUID tableId, UpdateDataTableRq rq);

    DataTableRs getById(UUID tableId);

    List<DataTableRs> getByWorkspaceId(UUID workspaceId);

    void delete(UUID tableId);
}