import { http } from "./http";
import type { DataTemplateCreateRequest, DataTemplateResponse, DataTemplateUpdateRequest } from "../types/template";

export const getTemplatesByWorkspace = (id: string) => http.get<DataTemplateResponse[]>(`/api/v1/templates/workspace/${id}`).then((r) => r.data);
export const createTemplate = (payload: DataTemplateCreateRequest) => http.post<DataTemplateResponse>("/api/v1/templates", payload).then((r) => r.data);
export const updateTemplate = (id: string, payload: DataTemplateUpdateRequest) => http.put<DataTemplateResponse>(`/api/v1/templates/${id}`, payload).then((r) => r.data);
export const deleteTemplate = (id: string) => http.delete(`/api/v1/templates/${id}`);
