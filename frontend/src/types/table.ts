export type DataTableCreateRequest = { workspaceId: string; name: string; displayName?: string | null; rowCount?: number | null };
export type DataTableUpdateRequest = Omit<DataTableCreateRequest, "workspaceId">;
export type DataTableResponse = DataTableCreateRequest & { tableId: string; createdAt?: string; updatedAt?: string };
