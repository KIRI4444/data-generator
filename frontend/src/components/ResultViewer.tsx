import { useMemo, useState } from "react";
import type { GenerateDataResponse } from "../types/generation";

export default function ResultViewer({ result }: { result: GenerateDataResponse }) {
  const [copied, setCopied] = useState(false);
  const display = useMemo(() => {
    if (result.exportFormat !== "JSON") return result.resultData;
    try { return JSON.stringify(JSON.parse(result.resultData), null, 2); } catch { return result.resultData; }
  }, [result]);
  const copy = async () => { await navigator.clipboard.writeText(result.resultData); setCopied(true); window.setTimeout(() => setCopied(false), 1500); };
  return (
    <section className="stack">
      <div className="stats">
        <span><b>ID истории</b>{result.generationHistoryId}</span><span><b>Таблиц</b>{result.tablesCount}</span>
        <span><b>Строк</b>{result.rowsCount}</span><span><b>Создано</b>{result.createdAt ? new Date(result.createdAt).toLocaleString("ru-RU") : "—"}</span>
      </div>
      <div className="section-heading"><h2>Результат</h2><button className="secondary" onClick={copy}>{copied ? "Скопировано!" : "Копировать результат"}</button></div>
      <pre className="result">{display}</pre>
      <h2>Сгенерированные таблицы</h2>
      {result.tables?.map((table) => <details className="card" key={table.tableId}><summary>{table.tableName} · строк: {table.rows.length}</summary><pre className="result small">{JSON.stringify(table.rows, null, 2)}</pre></details>)}
    </section>
  );
}
