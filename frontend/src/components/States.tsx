import { getErrorMessage } from "../api/http";

export const LoadingState = ({ label = "Загрузка…" }: { label?: string }) => <div className="state">{label}</div>;
export const EmptyState = ({ children }: { children: React.ReactNode }) => <div className="state muted">{children}</div>;
export const ErrorState = ({ error }: { error: unknown }) => <div className="error" role="alert">{getErrorMessage(error)}</div>;
