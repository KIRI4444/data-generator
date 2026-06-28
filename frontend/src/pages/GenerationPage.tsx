import { useMutation, useQuery } from "@tanstack/react-query";
import { Link, useParams } from "react-router-dom";
import { generateData } from "../api/generationApi";
import { getWorkspace } from "../api/workspacesApi";
import GenerateForm from "../components/GenerateForm";
import ResultViewer from "../components/ResultViewer";
import { ErrorState } from "../components/States";

export default function GenerationPage() {
  const { workspaceId = "" } = useParams();
  const workspace = useQuery({ queryKey: ["workspace", workspaceId], queryFn: () => getWorkspace(workspaceId) });
  const generation = useMutation({ mutationFn: generateData });
  return (
    <div className="stack">
      <Link className="back" to={`/workspaces/${workspaceId}`}>← Рабочее пространство</Link>
      <div><p className="eyebrow">Генерация</p><h1>{workspace.data?.name ?? "Данные рабочего пространства"}</h1><p className="muted">Выберите формат и сгенерируйте строки для всех настроенных таблиц.</p></div>
      <section className="card"><GenerateForm workspaceId={workspaceId} pending={generation.isPending} onSubmit={(v) => generation.mutate(v)} /></section>
      {generation.error && <ErrorState error={generation.error} />}
      {generation.data && <><ResultViewer result={generation.data} /><Link to={`/generation/history/${generation.data.generationHistoryId}`}>Открыть постоянную ссылку на результат →</Link></>}
    </div>
  );
}
