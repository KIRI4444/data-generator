import { useEffect, useState } from "react";
import { COLUMN_TYPES, COLUMN_TYPE_LABELS, GENERATOR_TYPES, GENERATOR_TYPE_LABELS, type ColumnType, type GeneratorType } from "../types/enums";
import type { DataColumnUpdateRequest } from "../types/column";
import type { DataTemplateResponse } from "../types/template";

const blank: DataColumnUpdateRequest = { name: "", displayName: "", columnType: "STRING", generatorType: "RANDOM_STRING", nullable: false, uniqueValue: false, primaryKey: false, minValue: null, maxValue: null, fixedValue: null, templateId: null, sortOrder: 1 };

export default function ColumnForm({ initial, templates, pending, onSubmit, onCancel }: {
  initial?: DataColumnUpdateRequest; templates: DataTemplateResponse[]; pending: boolean; onSubmit: (value: DataColumnUpdateRequest) => void; onCancel?: () => void;
}) {
  const [value, setValue] = useState<DataColumnUpdateRequest>(initial ?? blank);
  useEffect(() => setValue(initial ?? blank), [initial]);
  const set = <K extends keyof DataColumnUpdateRequest>(key: K, val: DataColumnUpdateRequest[K]) => setValue((v) => ({ ...v, [key]: val }));
  const usesTemplate = value.generatorType === "CUSTOM_TEMPLATE";
  return (
    <form className="form form-grid" onSubmit={(e) => { e.preventDefault(); onSubmit({ ...value, name: value.name.trim(), displayName: value.displayName?.trim() || null, minValue: value.minValue || null, maxValue: value.maxValue || null, fixedValue: value.fixedValue || null, templateId: usesTemplate ? value.templateId || null : null }); }}>
      <label>Название<input required value={value.name} onChange={(e) => set("name", e.target.value)} /></label>
      <label>Отображаемое название<input value={value.displayName ?? ""} onChange={(e) => set("displayName", e.target.value)} /></label>
      <label>Тип столбца<select value={value.columnType} onChange={(e) => set("columnType", e.target.value as ColumnType)}>{COLUMN_TYPES.map((x) => <option key={x} value={x}>{COLUMN_TYPE_LABELS[x]}</option>)}</select></label>
      <label>Генератор<select value={value.generatorType ?? ""} onChange={(e) => {
        const generatorType = (e.target.value || null) as GeneratorType | null;
        setValue((current) => ({ ...current, generatorType, templateId: generatorType === "CUSTOM_TEMPLATE" ? current.templateId : null }));
      }}><option value="">Нет</option>{GENERATOR_TYPES.map((x) => <option key={x} value={x}>{GENERATOR_TYPE_LABELS[x]}</option>)}</select></label>
      {usesTemplate && (
        <div className="template-choice full">
          <label>Пользовательский шаблон
            <select required value={value.templateId ?? ""} onChange={(e) => set("templateId", e.target.value || null)}>
              <option value="">Выберите шаблон…</option>
              {templates.map((t) => <option key={t.templateId} value={t.templateId}>{t.name} — {t.pattern}</option>)}
            </select>
          </label>
          {templates.length === 0
            ? <p className="form-hint warning">В этом рабочем пространстве пока нет шаблонов. Сначала создайте шаблон на странице рабочего пространства.</p>
            : <p className="form-hint">Выбранный шаблон будет использоваться для генерации значения этого столбца.</p>}
        </div>
      )}
      <label>Минимум<input value={value.minValue ?? ""} onChange={(e) => set("minValue", e.target.value)} /></label>
      <label>Максимум<input value={value.maxValue ?? ""} onChange={(e) => set("maxValue", e.target.value)} /></label>
      <label>Фиксированное значение<input value={value.fixedValue ?? ""} onChange={(e) => set("fixedValue", e.target.value)} /></label>
      <label>Порядок сортировки<input type="number" min="0" value={value.sortOrder ?? ""} onChange={(e) => set("sortOrder", e.target.value ? Number(e.target.value) : null)} /></label>
      <div className="checks"><label><input type="checkbox" checked={value.nullable} onChange={(e) => set("nullable", e.target.checked)} /> Допускает NULL</label><label><input type="checkbox" checked={value.uniqueValue} onChange={(e) => set("uniqueValue", e.target.checked)} /> Уникальный</label><label><input type="checkbox" checked={value.primaryKey} onChange={(e) => set("primaryKey", e.target.checked)} /> Первичный ключ</label></div>
      <div className="actions full"><button disabled={pending}>{pending ? "Сохранение…" : "Сохранить столбец"}</button>{onCancel && <button type="button" className="secondary" onClick={onCancel}>Отмена</button>}</div>
    </form>
  );
}
