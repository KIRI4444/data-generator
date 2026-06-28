export type WorkspaceRequest = { name: string; description?: string | null };
export type WorkspaceResponse = WorkspaceRequest & { workspaceId: string; createdAt?: string; updatedAt?: string };
