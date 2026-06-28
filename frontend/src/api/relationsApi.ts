import { http } from "./http";
import type { DataRelationCreateRequest, DataRelationResponse } from "../types/relation";

export const getRelationsByWorkspace = (id: string) => http.get<DataRelationResponse[]>(`/api/v1/relations/workspace/${id}`).then((r) => r.data);
export const createRelation = (payload: DataRelationCreateRequest) => http.post<DataRelationResponse>("/api/v1/relations", payload).then((r) => r.data);
export const deleteRelation = (id: string) => http.delete(`/api/v1/relations/${id}`);
