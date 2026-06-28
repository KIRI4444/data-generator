import { Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import GenerationPage from "./pages/GenerationPage";
import HistoryPage from "./pages/HistoryPage";
import TableDetailsPage from "./pages/TableDetailsPage";
import WorkspaceDetailsPage from "./pages/WorkspaceDetailsPage";
import WorkspacesPage from "./pages/WorkspacesPage";

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<WorkspacesPage />} />
        <Route path="workspaces/:workspaceId" element={<WorkspaceDetailsPage />} />
        <Route path="workspaces/:workspaceId/generation" element={<GenerationPage />} />
        <Route path="tables/:tableId" element={<TableDetailsPage />} />
        <Route path="generation/history/:generationHistoryId" element={<HistoryPage />} />
        <Route path="*" element={<div className="card"><h1>Страница не найдена</h1></div>} />
      </Route>
    </Routes>
  );
}
