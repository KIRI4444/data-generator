import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Link } from "react-router-dom";
import { createWorkspace, deleteWorkspace, getWorkspaces, updateWorkspace } from "../api/workspacesApi";
import WorkspaceForm from "../components/WorkspaceForm";
import { EmptyState, ErrorState, LoadingState } from "../components/States";
import type { WorkspaceRequest, WorkspaceResponse } from "../types/workspace";

export default function WorkspacesPage() {
  const qc = useQueryClient();
  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState<WorkspaceResponse>();
  const query = useQuery({ queryKey: ["workspaces"], queryFn: getWorkspaces });
  const save = useMutation({
    mutationFn: (value: WorkspaceRequest) => editing ? updateWorkspace(editing.workspaceId, value) : createWorkspace(value),
    onSuccess: () => { qc.invalidateQueries({ queryKey: ["workspaces"] }); setShowForm(false); setEditing(undefined); },
  });
  const remove = useMutation({ mutationFn: deleteWorkspace, onSuccess: () => qc.invalidateQueries({ queryKey: ["workspaces"] }) });
  return (
    <div className="stack">
      <div className="page-heading"><div><p className="eyebrow">Ваши проекты</p><h1>Рабочие пространства</h1><p className="muted">Управляйте таблицами, связями, шаблонами и наборами сгенерированных данных.</p></div><button onClick={() => { setEditing(undefined); setShowForm((x) => !x); }}>Создать пространство</button></div>
      {(showForm || editing) && <section className="card"><h2>{editing ? "Редактирование пространства" : "Новое рабочее пространство"}</h2>{save.error && <ErrorState error={save.error} />}<WorkspaceForm initial={editing} pending={save.isPending} onSubmit={(v) => save.mutate(v)} onCancel={() => { setShowForm(false); setEditing(undefined); }} /></section>}
      {query.isLoading && <LoadingState />}
      {query.error && <ErrorState error={query.error} />}
      {remove.error && <ErrorState error={remove.error} />}
      <div className="card-grid">
        {query.data?.map((workspace) => (
          <article className="card" key={workspace.workspaceId}>
            <div><h2>{workspace.name}</h2><p className="muted">{workspace.description || "Описание отсутствует"}</p></div>
            <div className="actions"><Link className="button-link" to={`/workspaces/${workspace.workspaceId}`}>Открыть</Link><button className="secondary" onClick={() => setEditing(workspace)}>Изменить</button><button className="danger" disabled={remove.isPending} onClick={() => window.confirm(`Удалить «${workspace.name}»?`) && remove.mutate(workspace.workspaceId)}>Удалить</button></div>
          </article>
        ))}
      </div>
      {query.data?.length === 0 && <EmptyState>Рабочих пространств пока нет. Создайте первое, чтобы начать.</EmptyState>}
    </div>
  );
}
