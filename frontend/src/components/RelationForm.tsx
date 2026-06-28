import { useEffect, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { getColumnsByTable } from "../api/columnsApi";
import { RELATION_TYPES, RELATION_TYPE_LABELS, type RelationType } from "../types/enums";
import type { DataRelationCreateRequest } from "../types/relation";
import type { DataTableResponse } from "../types/table";

export default function RelationForm({ tables, pending, onSubmit }: { tables: DataTableResponse[]; pending: boolean; onSubmit: (value: DataRelationCreateRequest) => void }) {
  const [sourceTableId, setSourceTableId] = useState("");
  const [targetTableId, setTargetTableId] = useState("");
  const [sourceColumnId, setSourceColumnId] = useState("");
  const [targetColumnId, setTargetColumnId] = useState("");
  const [relationType, setRelationType] = useState<RelationType>("MANY_TO_ONE");
  const source = useQuery({ queryKey: ["columns", sourceTableId], queryFn: () => getColumnsByTable(sourceTableId), enabled: !!sourceTableId });
  const target = useQuery({ queryKey: ["columns", targetTableId], queryFn: () => getColumnsByTable(targetTableId), enabled: !!targetTableId });
  useEffect(() => setSourceColumnId(""), [sourceTableId]);
  useEffect(() => setTargetColumnId(""), [targetTableId]);
  return (
    <form className="form form-grid" onSubmit={(e) => { e.preventDefault(); onSubmit({ sourceTableId, targetTableId, sourceColumnId, targetColumnId, relationType }); }}>
      <label>Исходная таблица<select required value={sourceTableId} onChange={(e) => setSourceTableId(e.target.value)}><option value="">Выберите…</option>{tables.map((t) => <option key={t.tableId} value={t.tableId}>{t.displayName || t.name}</option>)}</select></label>
      <label>Исходный столбец<select required disabled={!sourceTableId} value={sourceColumnId} onChange={(e) => setSourceColumnId(e.target.value)}><option value="">Выберите…</option>{source.data?.map((c) => <option key={c.columnId} value={c.columnId}>{c.name}</option>)}</select></label>
      <label>Целевая таблица<select required value={targetTableId} onChange={(e) => setTargetTableId(e.target.value)}><option value="">Выберите…</option>{tables.map((t) => <option key={t.tableId} value={t.tableId}>{t.displayName || t.name}</option>)}</select></label>
      <label>Целевой столбец<select required disabled={!targetTableId} value={targetColumnId} onChange={(e) => setTargetColumnId(e.target.value)}><option value="">Выберите…</option>{target.data?.map((c) => <option key={c.columnId} value={c.columnId}>{c.name}</option>)}</select></label>
      <label>Тип связи<select value={relationType} onChange={(e) => setRelationType(e.target.value as RelationType)}>{RELATION_TYPES.map((x) => <option key={x} value={x}>{RELATION_TYPE_LABELS[x]}</option>)}</select></label>
      <div className="actions full"><button disabled={pending || tables.length < 2}>{pending ? "Сохранение…" : "Создать связь"}</button></div>
    </form>
  );
}
