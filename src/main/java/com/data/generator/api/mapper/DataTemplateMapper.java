package com.data.generator.api.mapper;

import com.data.generator.api.dto.template.CreateDataTemplateRq;
import com.data.generator.api.dto.template.DataTemplateRs;
import com.data.generator.api.dto.template.UpdateDataTemplateRq;
import com.data.generator.api.model.DataTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataTemplateMapper {

    public DataTemplate toEntity(CreateDataTemplateRq rq) {
        DataTemplate template = new DataTemplate();
        template.setName(rq.name());
        template.setGeneratorType(rq.generatorType());
        template.setPattern(rq.pattern());
        template.setDescription(rq.description());
        return template;
    }

    public void updateEntity(DataTemplate template, UpdateDataTemplateRq rq) {
        template.setName(rq.name());
        template.setGeneratorType(rq.generatorType());
        template.setPattern(rq.pattern());
        template.setDescription(rq.description());
    }

    public DataTemplateRs toRs(DataTemplate template) {
        return DataTemplateRs.builder()
                .templateId(template.getTemplateId())
                .workspaceId(template.getWorkspace() == null ? null : template.getWorkspace().getWorkspaceId())
                .name(template.getName())
                .generatorType(template.getGeneratorType())
                .pattern(template.getPattern())
                .description(template.getDescription())
                .createdAt(template.getCreatedAt())
                .updatedAt(template.getUpdatedAt())
                .build();
    }
}