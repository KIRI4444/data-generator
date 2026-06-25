package com.data.generator.api.controller.impl;

import com.data.generator.api.controller.DataGenerationController;
import com.data.generator.api.dto.generation.GenerateDataRq;
import com.data.generator.api.dto.generation.GenerateDataRs;
import com.data.generator.api.service.DataGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DataGenerationControllerImpl implements DataGenerationController {

    private final DataGenerationService dataGenerationService;

    @Override
    public ResponseEntity<GenerateDataRs> generate(GenerateDataRq rq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataGenerationService.generate(rq));
    }

    @Override
    public ResponseEntity<GenerateDataRs> getHistoryById(UUID generationHistoryId) {
        return ResponseEntity.ok(dataGenerationService.getHistoryById(generationHistoryId));
    }
}