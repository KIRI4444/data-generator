import type { RelationType } from "./enums";
export type DataRelationCreateRequest = { sourceTableId: string; targetTableId: string; sourceColumnId: string; targetColumnId: string; relationType: RelationType };
export type DataRelationResponse = DataRelationCreateRequest & { relationId: string; createdAt?: string };
