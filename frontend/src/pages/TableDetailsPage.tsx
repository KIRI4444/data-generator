import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Link, useParams } from "react-router-dom";
import { createColumn, deleteColumn, getColumnsByTable, updateColumn } from "../api/columnsApi";
import { getTable } from "../api/tablesApi";
import { getTemplatesByWorkspace } from "../api/templatesApi";
import ColumnForm from "../components/ColumnForm";
import { ErrorState, LoadingState } from "../components/States";
import type { DataColumnResponse, DataColumnUpdateRequest } from "../types/column";
import { COLUMN_TYPE_LABELS, GENERATOR_TYPE_LABELS } from "../types/enums";

export default function TableDetailsPage() {
  const { tableId = "" } = useParams();
  const qc = useQueryClient();
  const [editing, setEditing] = useState<DataColumnResponse>();
  const table = useQuery({ queryKey: ["table", tableId], queryFn: () => getTable(tableId) });
  const columns = useQuery({ queryKey: ["columns", tableId], queryFn: () => getColumnsByTable(tableId) });
  const templates = useQuery({ queryKey: ["templates", table.data?.workspaceId], queryFn: () => getTemplatesByWorkspace(table.data!.workspaceId), enabled: !!table.data?.workspaceId });
  const save = useMutation({ mutationFn: (v: DataColumnUpdateRequest) => editing ? updateColumn(editing.columnId, v) : createColumn({ tableId, ...v }), onSuccess: () => { qc.invalidateQueries({ queryKey: ["columns", tableId] }); setEditing(undefined); } });
  const remove = useMutation({ mutationFn: deleteColumn, onSuccess: () => qc.invalidateQueries({ queryKey: ["columns", tableId] }) });
  if (table.isLoading) return <LoadingState />;
  if (!table.data) return <ErrorState error={table.error ?? new Error("Таблица не найдена")} />;
  const error = columns.error || templates.error || save.error || remove.error;
  return (
    <div className="stack">
      <Link className="back" to={`/workspaces/${table.data.workspaceId}`}>← Рабочее пространство</Link>
      <div className="page-heading"><div><p className="eyebrow">Таблица</p><h1>{table.data.displayName || table.data.name}</h1><p className="muted">{table.data.name} · строк по умолчанию: {table.data.rowCount ?? "—"}</p></div></div>
      {error && <ErrorState error={error} />}
      <section className="card"><h2>{editing ? `Редактирование: ${editing.name}` : "Добавить столбец"}</h2><ColumnForm initial={editing} templates={templates.data ?? []} pending={save.isPending} onSubmit={(v) => save.mutate(v)} onCancel={editing ? () => setEditing(undefined) : undefined} /></section>
      <section className="stack"><h2>Столбцы</h2>{columns.isLoading && <LoadingState />}
        <div className="list">{columns.data?.sort((a, b) => (a.sortOrder ?? 0) - (b.sortOrder ?? 0)).map((c) => <div className="list-row" key={c.columnId}><div><b>{c.name}</b><small>{COLUMN_TYPE_LABELS[c.columnType]} · {c.generatorType ? GENERATOR_TYPE_LABELS[c.generatorType] : "без генератора"}{c.primaryKey ? " · первичный ключ" : ""}{c.uniqueValue ? " · уникальный" : ""}</small></div><div className="actions"><button className="secondary" onClick={() => setEditing(c)}>Изменить</button><button className="danger" onClick={() => window.confirm(`Удалить столбец «${c.name}»?`) && remove.mutate(c.columnId)}>Удалить</button></div></div>)}</div>
      </section>
    </div>
  );
}
