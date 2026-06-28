import { http } from "./http";
import type { GenerateDataRequest, GenerateDataResponse } from "../types/generation";

export const generateData = (payload: GenerateDataRequest) => http.post<GenerateDataResponse>("/api/v1/generation", payload).then((r) => r.data);
export const getGenerationHistory = (id: string) => http.get<GenerateDataResponse>(`/api/v1/generation/history/${id}`).then((r) => r.data);
