package com.data.generator.api.controller.impl;

import com.data.generator.api.controller.DataTemplateController;
import com.data.generator.api.dto.template.CreateDataTemplateRq;
import com.data.generator.api.dto.template.DataTemplateRs;
import com.data.generator.api.dto.template.UpdateDataTemplateRq;
import com.data.generator.api.service.DataTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DataTemplateControllerImpl implements DataTemplateController {

    private final DataTemplateService dataTemplateService;

    @Override
    public ResponseEntity<DataTemplateRs> create(CreateDataTemplateRq rq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataTemplateService.create(rq));
    }

    @Override
    public ResponseEntity<DataTemplateRs> update(UUID templateId, UpdateDataTemplateRq rq) {
        return ResponseEntity.ok(dataTemplateService.update(templateId, rq));
    }

    @Override
    public ResponseEntity<DataTemplateRs> getById(UUID templateId) {
        return ResponseEntity.ok(dataTemplateService.getById(templateId));
    }

    @Override
    public ResponseEntity<List<DataTemplateRs>> getByWorkspaceId(UUID workspaceId) {
        return ResponseEntity.ok(dataTemplateService.getByWorkspaceId(workspaceId));
    }

    @Override
    public ResponseEntity<Void> delete(UUID templateId) {
        dataTemplateService.delete(templateId);
        return ResponseEntity.noContent().build();
    }
}