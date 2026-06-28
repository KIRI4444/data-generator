import { useEffect, useState } from "react";
import { GENERATOR_TYPES, GENERATOR_TYPE_LABELS, type GeneratorType } from "../types/enums";
import type { DataTemplateUpdateRequest } from "../types/template";

const TEMPLATE_TOKENS = [
  "{uuid}", "{string}", "{number}", "{boolean}", "{date}", "{datetime}",
  "{email}", "{phone}", "{firstName}", "{lastName}", "{fullName}", "{address}",
] as const;

export default function TemplateForm({ initial, pending, onSubmit, onCancel }: {
  initial?: DataTemplateUpdateRequest; pending: boolean; onSubmit: (value: DataTemplateUpdateRequest) => void; onCancel?: () => void;
}) {
  const [name, setName] = useState(initial?.name ?? "");
  const [generatorType, setGeneratorType] = useState<GeneratorType>(initial?.generatorType ?? "CUSTOM_TEMPLATE");
  const [pattern, setPattern] = useState(initial?.pattern ?? "");
  const [description, setDescription] = useState(initial?.description ?? "");
  useEffect(() => { if (initial) { setName(initial.name); setGeneratorType(initial.generatorType); setPattern(initial.pattern); setDescription(initial.description ?? ""); } }, [initial]);
  const addToken = (token: string) => setPattern((current) => current + token);
  return (
    <form className="form form-grid" onSubmit={(e) => { e.preventDefault(); onSubmit({ name: name.trim(), generatorType, pattern, description: description.trim() || null }); }}>
      <label>Название<input required value={name} onChange={(e) => setName(e.target.value)} /></label>
      <label>Генератор<select value={generatorType} onChange={(e) => setGeneratorType(e.target.value as GeneratorType)}>{GENERATOR_TYPES.map((x) => <option key={x} value={x}>{GENERATOR_TYPE_LABELS[x]}</option>)}</select></label>
      <div className="full template-builder">
        <label>Шаблон<input required value={pattern} onChange={(e) => setPattern(e.target.value)} placeholder="USER-{number}-{uuid}" /></label>
        <p className="form-hint">Нажмите на конструкцию, чтобы добавить её в шаблон:</p>
        <div className="token-list">
          {TEMPLATE_TOKENS.map((token) => <button className="token" type="button" key={token} onClick={() => addToken(token)}>{token}</button>)}
        </div>
        <p className="form-hint">Пример: <code>USER-{"{number}"}-{"{uuid}"}</code>. Значения вроде <code>{"{user_id}"}</code> пока не поддерживаются.</p>
      </div>
      <label className="full">Описание<textarea value={description ?? ""} onChange={(e) => setDescription(e.target.value)} /></label>
      <div className="actions full"><button disabled={pending}>{pending ? "Сохранение…" : "Сохранить шаблон"}</button>{onCancel && <button type="button" className="secondary" onClick={onCancel}>Отмена</button>}</div>
    </form>
  );
}
