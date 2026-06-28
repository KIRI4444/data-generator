import { http } from "./http";
import type { DataTableCreateRequest, DataTableResponse, DataTableUpdateRequest } from "../types/table";

export const getTablesByWorkspace = (id: string) => http.get<DataTableResponse[]>(`/api/v1/tables/workspace/${id}`).then((r) => r.data);
export const getTable = (id: string) => http.get<DataTableResponse>(`/api/v1/tables/${id}`).then((r) => r.data);
export const createTable = (payload: DataTableCreateRequest) => http.post<DataTableResponse>("/api/v1/tables", payload).then((r) => r.data);
export const updateTable = (id: string, payload: DataTableUpdateRequest) => http.put<DataTableResponse>(`/api/v1/tables/${id}`, payload).then((r) => r.data);
export const deleteTable = (id: string) => http.delete(`/api/v1/tables/${id}`);
