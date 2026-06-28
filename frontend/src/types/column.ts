import type { ColumnType, GeneratorType } from "./enums";
export type DataColumnUpdateRequest = {
  name: string; displayName?: string | null; columnType: ColumnType; generatorType?: GeneratorType | null;
  nullable: boolean; uniqueValue: boolean; primaryKey: boolean; minValue?: string | null; maxValue?: string | null;
  fixedValue?: string | null; templateId?: string | null; sortOrder?: number | null;
};
export type DataColumnCreateRequest = DataColumnUpdateRequest & { tableId: string };
export type DataColumnResponse = DataColumnCreateRequest & { columnId: string; createdAt?: string; updatedAt?: string };
