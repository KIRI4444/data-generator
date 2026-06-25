package com.data.generator.api.service;

import com.data.generator.api.dto.column.CreateDataColumnRq;
import com.data.generator.api.dto.column.UpdateDataColumnRq;
import com.data.generator.api.dto.column.DataColumnRs;

import java.util.List;
import java.util.UUID;

public interface DataColumnService {

    DataColumnRs create(CreateDataColumnRq rq);

    DataColumnRs update(UUID columnId, UpdateDataColumnRq rq);

    DataColumnRs getById(UUID columnId);

    List<DataColumnRs> getByTableId(UUID tableId);

    void delete(UUID columnId);
}