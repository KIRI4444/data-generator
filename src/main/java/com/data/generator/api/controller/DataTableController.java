package com.data.generator.api.controller;

import com.data.generator.api.dto.table.CreateDataTableRq;
import com.data.generator.api.dto.table.DataTableRs;
import com.data.generator.api.dto.table.UpdateDataTableRq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/tables")
public interface DataTableController {

    @PostMapping
    ResponseEntity<DataTableRs> create(@RequestBody CreateDataTableRq rq);

    @PutMapping("/{tableId}")
    ResponseEntity<DataTableRs> update(
            @PathVariable UUID tableId,
            @RequestBody UpdateDataTableRq rq
    );

    @GetMapping("/{tableId}")
    ResponseEntity<DataTableRs> getById(@PathVariable UUID tableId);

    @GetMapping("/workspace/{workspaceId}")
    ResponseEntity<List<DataTableRs>> getByWorkspaceId(@PathVariable UUID workspaceId);

    @DeleteMapping("/{tableId}")
    ResponseEntity<Void> delete(@PathVariable UUID tableId);
}