package com.data.generator.api.service.impl;

import com.data.generator.api.enums.ColumnType;
import com.data.generator.api.enums.GeneratorType;
import com.data.generator.api.model.DataColumn;
import com.data.generator.api.model.DataTemplate;
import com.data.generator.api.repository.DataColumnRepository;
import com.data.generator.api.repository.DataRelationRepository;
import com.data.generator.api.repository.DataTableRepository;
import com.data.generator.api.repository.DataTemplateRepository;
import com.data.generator.api.repository.GenerationHistoryRepository;
import com.data.generator.api.repository.WorkspaceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataGenerationServiceImplTest {

    @Test
    void customTemplateIsUsedForUniqueColumn() {
        UUID templateId = UUID.randomUUID();
        DataTemplate template = new DataTemplate();
        template.setTemplateId(templateId);
        template.setPattern("{uuid}-{date}");

        DataTemplateRepository templateRepository = mock(DataTemplateRepository.class);
        when(templateRepository.findById(templateId)).thenReturn(Optional.of(template));

        DataGenerationServiceImpl service = new DataGenerationServiceImpl(
                mock(WorkspaceRepository.class),
                mock(DataTableRepository.class),
                mock(DataColumnRepository.class),
                mock(DataRelationRepository.class),
                templateRepository,
                mock(GenerationHistoryRepository.class),
                new ObjectMapper()
        );

        DataColumn column = new DataColumn();
        column.setName("row");
        column.setColumnType(ColumnType.STRING);
        column.setGeneratorType(GeneratorType.CUSTOM_TEMPLATE);
        column.setTemplateId(templateId);
        column.setNullable(false);
        column.setUniqueValue(true);
        column.setPrimaryKey(false);

        Object value = ReflectionTestUtils.invokeMethod(service, "generateColumnValue", column, 0);

        assertThat(value)
                .isInstanceOf(String.class)
                .asString()
                .matches("[0-9a-f-]{36}-\\d{4}-\\d{2}-\\d{2}")
                .doesNotStartWith("row_");
    }
}
