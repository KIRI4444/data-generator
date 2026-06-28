import { useEffect, useState } from "react";
import type { WorkspaceRequest } from "../types/workspace";

export default function WorkspaceForm({ initial, pending, onSubmit, onCancel }: {
  initial?: WorkspaceRequest; pending: boolean; onSubmit: (value: WorkspaceRequest) => void; onCancel?: () => void;
}) {
  const [name, setName] = useState(initial?.name ?? "");
  const [description, setDescription] = useState(initial?.description ?? "");
  useEffect(() => { setName(initial?.name ?? ""); setDescription(initial?.description ?? ""); }, [initial]);
  return (
    <form className="form" onSubmit={(e) => { e.preventDefault(); onSubmit({ name: name.trim(), description: description.trim() || null }); }}>
      <label>Название<input required value={name} onChange={(e) => setName(e.target.value)} /></label>
      <label>Описание<textarea value={description ?? ""} onChange={(e) => setDescription(e.target.value)} /></label>
      <div className="actions"><button disabled={pending}>{pending ? "Сохранение…" : "Сохранить рабочее пространство"}</button>{onCancel && <button type="button" className="secondary" onClick={onCancel}>Отмена</button>}</div>
    </form>
  );
}
