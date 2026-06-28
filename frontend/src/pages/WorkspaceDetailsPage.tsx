import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Link, useParams } from "react-router-dom";
import { createRelation, deleteRelation, getRelationsByWorkspace } from "../api/relationsApi";
import { createTable, deleteTable, getTablesByWorkspace, updateTable } from "../api/tablesApi";
import { createTemplate, deleteTemplate, getTemplatesByWorkspace, updateTemplate } from "../api/templatesApi";
import { getWorkspace, updateWorkspace } from "../api/workspacesApi";
import { ErrorState, LoadingState } from "../components/States";
import RelationForm from "../components/RelationForm";
import TableForm from "../components/TableForm";
import TemplateForm from "../components/TemplateForm";
import WorkspaceForm from "../components/WorkspaceForm";
import type { DataTableResponse, DataTableUpdateRequest } from "../types/table";
import type { DataTemplateResponse, DataTemplateUpdateRequest } from "../types/template";
import { GENERATOR_TYPE_LABELS, RELATION_TYPE_LABELS } from "../types/enums";

export default function WorkspaceDetailsPage() {
  const { workspaceId = "" } = useParams();
  const qc = useQueryClient();
  const [editWorkspace, setEditWorkspace] = useState(false);
  const [editingTable, setEditingTable] = useState<DataTableResponse>();
  const [editingTemplate, setEditingTemplate] = useState<DataTemplateResponse>();
  const workspace = useQuery({ queryKey: ["workspace", workspaceId], queryFn: () => getWorkspace(workspaceId) });
  const tables = useQuery({ queryKey: ["tables", workspaceId], queryFn: () => getTablesByWorkspace(workspaceId) });
  const templates = useQuery({ queryKey: ["templates", workspaceId], queryFn: () => getTemplatesByWorkspace(workspaceId) });
  const relations = useQuery({ queryKey: ["relations", workspaceId], queryFn: () => getRelationsByWorkspace(workspaceId) });
  const refresh = (key: string) => qc.invalidateQueries({ queryKey: [key, workspaceId] });
  const workspaceSave = useMutation({ mutationFn: (v: { name: string; description?: string | null }) => updateWorkspace(workspaceId, v), onSuccess: () => { refresh("workspace"); setEditWorkspace(false); } });
  const tableSave = useMutation({ mutationFn: (v: DataTableUpdateRequest) => editingTable ? updateTable(editingTable.tableId, v) : createTable({ workspaceId, ...v }), onSuccess: () => { refresh("tables"); setEditingTable(undefined); } });
  const tableDelete = useMutation({ mutationFn: deleteTable, onSuccess: () => refresh("tables") });
  const templateSave = useMutation({ mutationFn: (v: DataTemplateUpdateRequest) => editingTemplate ? updateTemplate(editingTemplate.templateId, v) : createTemplate({ workspaceId, ...v }), onSuccess: () => { refresh("templates"); setEditingTemplate(undefined); } });
  const templateDelete = useMutation({ mutationFn: deleteTemplate, onSuccess: () => refresh("templates") });
  const relationSave = useMutation({ mutationFn: createRelation, onSuccess: () => refresh("relations") });
  const relationDelete = useMutation({ mutationFn: deleteRelation, onSuccess: () => refresh("relations") });
  const anyError = workspace.error || tables.error || templates.error || relations.error || workspaceSave.error || tableSave.error || tableDelete.error || templateSave.error || templateDelete.error || relationSave.error || relationDelete.error;
  if (workspace.isLoading) return <LoadingState />;
  if (!workspace.data) return <ErrorState error={workspace.error ?? new Error("Рабочее пространство не найдено")} />;
  const tableName = (id: string) => tables.data?.find((t) => t.tableId === id)?.name ?? id;
  return (
    <div className="stack">
      <Link className="back" to="/">← Рабочие пространства</Link>
      <div className="page-heading"><div><p className="eyebrow">Рабочее пространство</p><h1>{workspace.data.name}</h1><p className="muted">{workspace.data.description || "Описание отсутствует"}</p></div><div className="actions"><button className="secondary" onClick={() => setEditWorkspace((x) => !x)}>Изменить</button><Link className="button-link" to={`/workspaces/${workspaceId}/generation`}>Сгенерировать данные</Link></div></div>
      {anyError && <ErrorState error={anyError} />}
      {editWorkspace && <section className="card"><WorkspaceForm initial={workspace.data} pending={workspaceSave.isPending} onSubmit={(v) => workspaceSave.mutate(v)} onCancel={() => setEditWorkspace(false)} /></section>}

      <section className="stack"><div className="section-heading"><div><p className="eyebrow">Схема</p><h2>Таблицы</h2></div></div>
        <div className="card"><h3>{editingTable ? `Редактирование: ${editingTable.name}` : "Добавить таблицу"}</h3><TableForm initial={editingTable} pending={tableSave.isPending} onSubmit={(v) => tableSave.mutate(v)} onCancel={editingTable ? () => setEditingTable(undefined) : undefined} /></div>
        <div className="list">{tables.data?.map((t) => <div className="list-row" key={t.tableId}><div><Link to={`/tables/${t.tableId}`}><b>{t.displayName || t.name}</b></Link><small>{t.name} · строк по умолчанию: {t.rowCount ?? "—"}</small></div><div className="actions"><button className="secondary" onClick={() => setEditingTable(t)}>Изменить</button><button className="danger" onClick={() => window.confirm(`Удалить таблицу «${t.name}»?`) && tableDelete.mutate(t.tableId)}>Удалить</button></div></div>)}</div>
      </section>

      <section className="stack"><div><p className="eyebrow">Повторное использование</p><h2>Шаблоны</h2></div>
        <div className="card"><h3>{editingTemplate ? `Редактирование: ${editingTemplate.name}` : "Добавить шаблон"}</h3><TemplateForm initial={editingTemplate} pending={templateSave.isPending} onSubmit={(v) => templateSave.mutate(v)} onCancel={editingTemplate ? () => setEditingTemplate(undefined) : undefined} /></div>
        <div className="list">{templates.data?.map((t) => <div className="list-row" key={t.templateId}><div><b>{t.name}</b><small>{GENERATOR_TYPE_LABELS[t.generatorType]} · <code>{t.pattern}</code></small></div><div className="actions"><button className="secondary" onClick={() => setEditingTemplate(t)}>Изменить</button><button className="danger" onClick={() => templateDelete.mutate(t.templateId)}>Удалить</button></div></div>)}</div>
      </section>

      <section className="stack"><div><p className="eyebrow">Зависимости</p><h2>Связи</h2></div>
        <div className="card"><RelationForm tables={tables.data ?? []} pending={relationSave.isPending} onSubmit={(v) => relationSave.mutate(v)} />{(tables.data?.length ?? 0) < 2 && <p className="muted">Для создания связи необходимо как минимум две таблицы.</p>}</div>
        <div className="list">{relations.data?.map((r) => <div className="list-row" key={r.relationId}><div><b>{tableName(r.sourceTableId)} → {tableName(r.targetTableId)}</b><small>{RELATION_TYPE_LABELS[r.relationType]}</small></div><button className="danger" onClick={() => relationDelete.mutate(r.relationId)}>Удалить</button></div>)}</div>
      </section>
    </div>
  );
}
