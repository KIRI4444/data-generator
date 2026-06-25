package com.data.generator.api.controller;

import com.data.generator.api.dto.column.CreateDataColumnRq;
import com.data.generator.api.dto.column.DataColumnRs;
import com.data.generator.api.dto.column.UpdateDataColumnRq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/columns")
public interface DataColumnController {

    @PostMapping
    ResponseEntity<DataColumnRs> create(@RequestBody CreateDataColumnRq rq);

    @PutMapping("/{columnId}")
    ResponseEntity<DataColumnRs> update(
            @PathVariable UUID columnId,
            @RequestBody UpdateDataColumnRq rq
    );

    @GetMapping("/{columnId}")
    ResponseEntity<DataColumnRs> getById(@PathVariable UUID columnId);

    @GetMapping("/table/{tableId}")
    ResponseEntity<List<DataColumnRs>> getByTableId(@PathVariable UUID tableId);

    @DeleteMapping("/{columnId}")
    ResponseEntity<Void> delete(@PathVariable UUID columnId);
}