import { Link, Outlet } from "react-router-dom";

export default function Layout() {
  return (
    <>
      <header className="topbar">
        <Link className="brand" to="/">Генератор данных</Link>
        <span className="muted">Тестовые данные без лишней возни с API</span>
      </header>
      <main className="container"><Outlet /></main>
    </>
  );
}
