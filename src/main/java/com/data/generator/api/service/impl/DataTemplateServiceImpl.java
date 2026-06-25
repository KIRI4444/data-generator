package com.data.generator.api.service.impl;

import com.data.generator.api.dto.template.CreateDataTemplateRq;
import com.data.generator.api.dto.template.DataTemplateRs;
import com.data.generator.api.dto.template.UpdateDataTemplateRq;
import com.data.generator.api.exception.EntityNotFoundException;
import com.data.generator.api.mapper.DataTemplateMapper;
import com.data.generator.api.model.DataTemplate;
import com.data.generator.api.model.Workspace;
import com.data.generator.api.repository.DataTemplateRepository;
import com.data.generator.api.repository.WorkspaceRepository;
import com.data.generator.api.service.DataTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataTemplateServiceImpl implements DataTemplateService {

    private final DataTemplateRepository dataTemplateRepository;
    private final WorkspaceRepository workspaceRepository;
    private final DataTemplateMapper dataTemplateMapper;

    @Override
    @Transactional
    public DataTemplateRs create(CreateDataTemplateRq rq) {
        Workspace workspace = workspaceRepository.findById(rq.workspaceId())
                .orElseThrow(() -> new EntityNotFoundException("Workspace not found: " + rq.workspaceId()));

        if (dataTemplateRepository.existsByWorkspaceWorkspaceIdAndName(rq.workspaceId(), rq.name())) {
            throw new IllegalArgumentException("Data template already exists in workspace: " + rq.name());
        }

        DataTemplate template = dataTemplateMapper.toEntity(rq);
        template.setWorkspace(workspace);
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());

        DataTemplate savedTemplate = dataTemplateRepository.save(template);
        return dataTemplateMapper.toRs(savedTemplate);
    }

    @Override
    @Transactional
    public DataTemplateRs update(UUID templateId, UpdateDataTemplateRq rq) {
        DataTemplate template = getTemplateOrThrow(templateId);

        UUID workspaceId = template.getWorkspace().getWorkspaceId();

        if (!Objects.equals(template.getName(), rq.name())
                && dataTemplateRepository.existsByWorkspaceWorkspaceIdAndName(workspaceId, rq.name())) {
            throw new IllegalArgumentException("Data template already exists in workspace: " + rq.name());
        }

        dataTemplateMapper.updateEntity(template, rq);
        template.setUpdatedAt(LocalDateTime.now());

        DataTemplate savedTemplate = dataTemplateRepository.save(template);
        return dataTemplateMapper.toRs(savedTemplate);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTemplateRs getById(UUID templateId) {
        DataTemplate template = getTemplateOrThrow(templateId);
        return dataTemplateMapper.toRs(template);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataTemplateRs> getByWorkspaceId(UUID workspaceId) {
        if (!workspaceRepository.existsById(workspaceId)) {
            throw new EntityNotFoundException("Workspace not found: " + workspaceId);
        }

        return dataTemplateRepository.findAllByWorkspaceWorkspaceId(workspaceId)
                .stream()
                .map(dataTemplateMapper::toRs)
                .toList();
    }

    @Override
    @Transactional
    public void delete(UUID templateId) {
        DataTemplate template = getTemplateOrThrow(templateId);
        dataTemplateRepository.delete(template);
    }

    private DataTemplate getTemplateOrThrow(UUID templateId) {
        return dataTemplateRepository.findById(templateId)
                .orElseThrow(() -> new EntityNotFoundException("Data template not found: " + templateId));
    }
}