export const COLUMN_TYPES = ["STRING", "INTEGER", "LONG", "DOUBLE", "BOOLEAN", "DATE", "DATETIME", "UUID", "EMAIL", "PHONE", "FIRST_NAME", "LAST_NAME", "FULL_NAME", "ADDRESS", "CUSTOM"] as const;
export const GENERATOR_TYPES = ["RANDOM_STRING", "RANDOM_NUMBER", "RANDOM_BOOLEAN", "RANDOM_DATE", "RANDOM_UUID", "EMAIL", "PHONE", "FIRST_NAME", "LAST_NAME", "FULL_NAME", "ADDRESS", "FIXED_VALUE", "CUSTOM_TEMPLATE", "RELATION_VALUE"] as const;
export const RELATION_TYPES = ["ONE_TO_ONE", "ONE_TO_MANY", "MANY_TO_ONE", "MANY_TO_MANY"] as const;
export const EXPORT_FORMATS = ["JSON", "SQL", "CSV"] as const;
export type ColumnType = typeof COLUMN_TYPES[number];
export type GeneratorType = typeof GENERATOR_TYPES[number];
export type RelationType = typeof RELATION_TYPES[number];
export type ExportFormat = typeof EXPORT_FORMATS[number];

export const COLUMN_TYPE_LABELS: Record<ColumnType, string> = {
  STRING: "Строка", INTEGER: "Целое число", LONG: "Длинное целое", DOUBLE: "Дробное число",
  BOOLEAN: "Логическое значение", DATE: "Дата", DATETIME: "Дата и время", UUID: "UUID",
  EMAIL: "Электронная почта", PHONE: "Телефон", FIRST_NAME: "Имя", LAST_NAME: "Фамилия",
  FULL_NAME: "Полное имя", ADDRESS: "Адрес", CUSTOM: "Пользовательский",
};

export const GENERATOR_TYPE_LABELS: Record<GeneratorType, string> = {
  RANDOM_STRING: "Случайная строка", RANDOM_NUMBER: "Случайное число",
  RANDOM_BOOLEAN: "Случайное логическое значение", RANDOM_DATE: "Случайная дата",
  RANDOM_UUID: "Случайный UUID", EMAIL: "Электронная почта", PHONE: "Телефон",
  FIRST_NAME: "Имя", LAST_NAME: "Фамилия", FULL_NAME: "Полное имя", ADDRESS: "Адрес",
  FIXED_VALUE: "Фиксированное значение", CUSTOM_TEMPLATE: "Пользовательский шаблон",
  RELATION_VALUE: "Значение из связи",
};

export const RELATION_TYPE_LABELS: Record<RelationType, string> = {
  ONE_TO_ONE: "Один к одному", ONE_TO_MANY: "Один ко многим",
  MANY_TO_ONE: "Многие к одному", MANY_TO_MANY: "Многие ко многим",
};
