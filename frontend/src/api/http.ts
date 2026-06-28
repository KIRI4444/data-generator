import axios from "axios";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

export const http = axios.create({
  baseURL: apiBaseUrl,
  headers: { "Content-Type": "application/json" },
});

export function getErrorMessage(error: unknown): string {
  if (axios.isAxiosError(error)) {
    const data = error.response?.data as { message?: string; error?: string } | string | undefined;
    if (typeof data === "string") return data;
    return data?.message ?? data?.error ?? error.message;
  }
  return error instanceof Error ? error.message : "Произошла непредвиденная ошибка";
}
