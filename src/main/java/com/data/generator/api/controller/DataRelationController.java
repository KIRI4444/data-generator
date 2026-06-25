package com.data.generator.api.controller;

import com.data.generator.api.dto.relation.CreateDataRelationRq;
import com.data.generator.api.dto.relation.DataRelationRs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/relations")
public interface DataRelationController {

    @PostMapping
    ResponseEntity<DataRelationRs> create(@RequestBody CreateDataRelationRq rq);

    @GetMapping("/{relationId}")
    ResponseEntity<DataRelationRs> getById(@PathVariable UUID relationId);

    @GetMapping("/source-table/{sourceTableId}")
    ResponseEntity<List<DataRelationRs>> getBySourceTableId(@PathVariable UUID sourceTableId);

    @GetMapping("/target-table/{targetTableId}")
    ResponseEntity<List<DataRelationRs>> getByTargetTableId(@PathVariable UUID targetTableId);

    @GetMapping("/workspace/{workspaceId}")
    ResponseEntity<List<DataRelationRs>> getByWorkspaceId(@PathVariable UUID workspaceId);

    @DeleteMapping("/{relationId}")
    ResponseEntity<Void> delete(@PathVariable UUID relationId);
}