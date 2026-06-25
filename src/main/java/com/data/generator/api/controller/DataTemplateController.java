package com.data.generator.api.controller;

import com.data.generator.api.dto.template.CreateDataTemplateRq;
import com.data.generator.api.dto.template.DataTemplateRs;
import com.data.generator.api.dto.template.UpdateDataTemplateRq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/templates")
public interface DataTemplateController {

    @PostMapping
    ResponseEntity<DataTemplateRs> create(@RequestBody CreateDataTemplateRq rq);

    @PutMapping("/{templateId}")
    ResponseEntity<DataTemplateRs> update(
            @PathVariable UUID templateId,
            @RequestBody UpdateDataTemplateRq rq
    );

    @GetMapping("/{templateId}")
    ResponseEntity<DataTemplateRs> getById(@PathVariable UUID templateId);

    @GetMapping("/workspace/{workspaceId}")
    ResponseEntity<List<DataTemplateRs>> getByWorkspaceId(@PathVariable UUID workspaceId);

    @DeleteMapping("/{templateId}")
    ResponseEntity<Void> delete(@PathVariable UUID templateId);
}