import { useQuery } from "@tanstack/react-query";
import { Link, useParams } from "react-router-dom";
import { getGenerationHistory } from "../api/generationApi";
import ResultViewer from "../components/ResultViewer";
import { ErrorState, LoadingState } from "../components/States";

export default function HistoryPage() {
  const { generationHistoryId = "" } = useParams();
  const query = useQuery({ queryKey: ["history", generationHistoryId], queryFn: () => getGenerationHistory(generationHistoryId) });
  if (query.isLoading) return <LoadingState label="Загрузка истории генерации…" />;
  if (!query.data) return <ErrorState error={query.error ?? new Error("Запись истории не найдена")} />;
  return <div className="stack"><Link className="back" to={`/workspaces/${query.data.workspaceId}/generation`}>← Генерация</Link><div><p className="eyebrow">История генерации</p><h1>Результат в формате {query.data.exportFormat}</h1></div><ResultViewer result={query.data} /></div>;
}
