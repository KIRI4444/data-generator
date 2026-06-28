import { http } from "./http";
import type { WorkspaceRequest, WorkspaceResponse } from "../types/workspace";

export const getWorkspaces = () => http.get<WorkspaceResponse[]>("/api/v1/workspaces").then((r) => r.data);
export const getWorkspace = (id: string) => http.get<WorkspaceResponse>(`/api/v1/workspaces/${id}`).then((r) => r.data);
export const createWorkspace = (payload: WorkspaceRequest) => http.post<WorkspaceResponse>("/api/v1/workspaces", payload).then((r) => r.data);
export const updateWorkspace = (id: string, payload: WorkspaceRequest) => http.put<WorkspaceResponse>(`/api/v1/workspaces/${id}`, payload).then((r) => r.data);
export const deleteWorkspace = (id: string) => http.delete(`/api/v1/workspaces/${id}`);
