import { useEffect, useState } from "react";
import type { DataTableUpdateRequest } from "../types/table";

export default function TableForm({ initial, pending, onSubmit, onCancel }: {
  initial?: DataTableUpdateRequest; pending: boolean; onSubmit: (value: DataTableUpdateRequest) => void; onCancel?: () => void;
}) {
  const [name, setName] = useState(initial?.name ?? "");
  const [displayName, setDisplayName] = useState(initial?.displayName ?? "");
  const [rowCount, setRowCount] = useState(initial?.rowCount?.toString() ?? "10");
  useEffect(() => { setName(initial?.name ?? ""); setDisplayName(initial?.displayName ?? ""); setRowCount(initial?.rowCount?.toString() ?? "10"); }, [initial]);
  return (
    <form className="form form-grid" onSubmit={(e) => { e.preventDefault(); onSubmit({ name: name.trim(), displayName: displayName.trim() || null, rowCount: rowCount ? Number(rowCount) : null }); }}>
      <label>Название в базе данных<input required value={name} onChange={(e) => setName(e.target.value)} /></label>
      <label>Отображаемое название<input value={displayName ?? ""} onChange={(e) => setDisplayName(e.target.value)} /></label>
      <label>Количество строк по умолчанию<input type="number" min="1" value={rowCount} onChange={(e) => setRowCount(e.target.value)} /></label>
      <div className="actions full"><button disabled={pending}>{pending ? "Сохранение…" : "Сохранить таблицу"}</button>{onCancel && <button type="button" className="secondary" onClick={onCancel}>Отмена</button>}</div>
    </form>
  );
}
