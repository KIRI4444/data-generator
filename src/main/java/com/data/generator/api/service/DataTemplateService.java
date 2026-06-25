package com.data.generator.api.service;

import com.data.generator.api.dto.template.CreateDataTemplateRq;
import com.data.generator.api.dto.template.UpdateDataTemplateRq;
import com.data.generator.api.dto.template.DataTemplateRs;

import java.util.List;
import java.util.UUID;

public interface DataTemplateService {

    DataTemplateRs create(CreateDataTemplateRq rq);

    DataTemplateRs update(UUID templateId, UpdateDataTemplateRq rq);

    DataTemplateRs getById(UUID templateId);

    List<DataTemplateRs> getByWorkspaceId(UUID workspaceId);

    void delete(UUID templateId);
}