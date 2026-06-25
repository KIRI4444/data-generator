package com.data.generator.api.service;

import com.data.generator.api.dto.relation.CreateDataRelationRq;
import com.data.generator.api.dto.relation.DataRelationRs;

import java.util.List;
import java.util.UUID;

public interface DataRelationService {

    DataRelationRs create(CreateDataRelationRq rq);

    DataRelationRs getById(UUID relationId);

    List<DataRelationRs> getBySourceTableId(UUID sourceTableId);

    List<DataRelationRs> getByTargetTableId(UUID targetTableId);

    List<DataRelationRs> getByWorkspaceId(UUID workspaceId);

    void delete(UUID relationId);
}