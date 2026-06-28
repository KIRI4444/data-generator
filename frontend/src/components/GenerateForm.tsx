import { useState } from "react";
import { EXPORT_FORMATS, type ExportFormat } from "../types/enums";
import type { GenerateDataRequest } from "../types/generation";

export default function GenerateForm({ workspaceId, pending, onSubmit }: { workspaceId: string; pending: boolean; onSubmit: (value: GenerateDataRequest) => void }) {
  const [exportFormat, setExportFormat] = useState<ExportFormat>("JSON");
  const [rowsCount, setRowsCount] = useState("10");
  return (
    <form className="form form-grid compact" onSubmit={(e) => { e.preventDefault(); onSubmit({ workspaceId, exportFormat, rowsCount: rowsCount ? Number(rowsCount) : null }); }}>
      <label>Формат экспорта<select value={exportFormat} onChange={(e) => setExportFormat(e.target.value as ExportFormat)}>{EXPORT_FORMATS.map((x) => <option key={x}>{x}</option>)}</select></label>
      <label>Строк на таблицу<input type="number" min="1" value={rowsCount} onChange={(e) => setRowsCount(e.target.value)} /></label>
      <div className="actions full"><button disabled={pending}>{pending ? "Генерация…" : "Сгенерировать данные"}</button></div>
    </form>
  );
}
