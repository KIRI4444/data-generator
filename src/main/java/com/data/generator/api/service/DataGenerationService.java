package com.data.generator.api.service;

import com.data.generator.api.dto.generation.GenerateDataRq;
import com.data.generator.api.dto.generation.GenerateDataRs;

import java.util.UUID;

public interface DataGenerationService {

    GenerateDataRs generate(GenerateDataRq rq);

    GenerateDataRs getHistoryById(UUID generationHistoryId);
}