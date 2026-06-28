import { http } from "./http";
import type { DataColumnCreateRequest, DataColumnResponse, DataColumnUpdateRequest } from "../types/column";

export const getColumnsByTable = (id: string) => http.get<DataColumnResponse[]>(`/api/v1/columns/table/${id}`).then((r) => r.data);
export const getColumn = (id: string) => http.get<DataColumnResponse>(`/api/v1/columns/${id}`).then((r) => r.data);
export const createColumn = (payload: DataColumnCreateRequest) => http.post<DataColumnResponse>("/api/v1/columns", payload).then((r) => r.data);
export const updateColumn = (id: string, payload: DataColumnUpdateRequest) => http.put<DataColumnResponse>(`/api/v1/columns/${id}`, payload).then((r) => r.data);
export const deleteColumn = (id: string) => http.delete(`/api/v1/columns/${id}`);
