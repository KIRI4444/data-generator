import type { ExportFormat } from "./enums";
export type GenerateDataRequest = { workspaceId: string; exportFormat: ExportFormat; rowsCount?: number | null };
export type GeneratedTableData = { tableId: string; tableName: string; rows: Record<string, unknown>[] };
export type GenerateDataResponse = {
  generationHistoryId: string; workspaceId: string; exportFormat: ExportFormat; tablesCount: number;
  rowsCount: number; resultData: string; tables: GeneratedTableData[]; createdAt?: string;
};
