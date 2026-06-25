package com.data.generator.api.controller;

import com.data.generator.api.dto.generation.GenerateDataRq;
import com.data.generator.api.dto.generation.GenerateDataRs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/generation")
public interface DataGenerationController {

    @PostMapping
    ResponseEntity<GenerateDataRs> generate(@RequestBody GenerateDataRq rq);

    @GetMapping("/history/{generationHistoryId}")
    ResponseEntity<GenerateDataRs> getHistoryById(@PathVariable UUID generationHistoryId);
}