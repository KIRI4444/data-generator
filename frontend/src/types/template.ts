import type { GeneratorType } from "./enums";
export type DataTemplateUpdateRequest = { name: string; generatorType: GeneratorType; pattern: string; description?: string | null };
export type DataTemplateCreateRequest = DataTemplateUpdateRequest & { workspaceId: string };
export type DataTemplateResponse = DataTemplateCreateRequest & { templateId: string; createdAt?: string; updatedAt?: string };
