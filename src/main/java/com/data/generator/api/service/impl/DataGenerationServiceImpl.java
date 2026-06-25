package com.data.generator.api.service.impl;

import com.data.generator.api.dto.generation.GenerateDataRq;
import com.data.generator.api.dto.generation.GenerateDataRs;
import com.data.generator.api.dto.generation.GeneratedTableData;
import com.data.generator.api.enums.ColumnType;
import com.data.generator.api.enums.ExportFormat;
import com.data.generator.api.enums.GeneratorType;
import com.data.generator.api.enums.RelationType;
import com.data.generator.api.exception.EntityNotFoundException;
import com.data.generator.api.model.DataColumn;
import com.data.generator.api.model.DataRelation;
import com.data.generator.api.model.DataTable;
import com.data.generator.api.model.DataTemplate;
import com.data.generator.api.model.GenerationHistory;
import com.data.generator.api.model.Workspace;
import com.data.generator.api.repository.DataColumnRepository;
import com.data.generator.api.repository.DataRelationRepository;
import com.data.generator.api.repository.DataTableRepository;
import com.data.generator.api.repository.DataTemplateRepository;
import com.data.generator.api.repository.GenerationHistoryRepository;
import com.data.generator.api.repository.WorkspaceRepository;
import com.data.generator.api.service.DataGenerationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataGenerationServiceImpl implements DataGenerationService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final WorkspaceRepository workspaceRepository;
    private final DataTableRepository dataTableRepository;
    private final DataColumnRepository dataColumnRepository;
    private final DataRelationRepository dataRelationRepository;
    private final DataTemplateRepository dataTemplateRepository;
    private final GenerationHistoryRepository generationHistoryRepository;
    private final ObjectMapper objectMapper;

    private final Random random = new Random();

    @Override
    @Transactional
    public GenerateDataRs generate(GenerateDataRq rq) {
        Workspace workspace = getWorkspaceOrThrow(rq.workspaceId());
        List<DataTable> tables = getTables(workspace.getWorkspaceId());
        List<DataRelation> relations = getRelations(workspace.getWorkspaceId());

        Map<UUID, List<DataColumn>> columnsByTableId = getColumnsByTableId(tables);

        List<GeneratedTableData> generatedTables = generateTables(rq, tables, columnsByTableId);

        applyRelationValues(generatedTables, relations);

        List<GeneratedTableData> exportTables = generatedTables;

        if (ExportFormat.SQL.equals(rq.exportFormat())) {
            exportTables = sortTablesForSqlExport(generatedTables, relations);
        }

        Integer tablesCount = generatedTables.size();
        Integer rowsCount = countRows(generatedTables);
        String resultData = exportResult(rq.exportFormat(), exportTables);

        GenerationHistory history = saveGenerationHistory(
                workspace,
                rq.exportFormat(),
                tablesCount,
                rowsCount,
                resultData
        );

        return buildGenerateDataRs(history, generatedTables);
    }

    @Override
    @Transactional(readOnly = true)
    public GenerateDataRs getHistoryById(UUID generationHistoryId) {
        GenerationHistory history = generationHistoryRepository.findById(generationHistoryId)
                .orElseThrow(() -> new EntityNotFoundException("Generation history not found: " + generationHistoryId));

        return GenerateDataRs.builder()
                .generationHistoryId(history.getGenerationHistoryId())
                .workspaceId(history.getWorkspace() == null ? null : history.getWorkspace().getWorkspaceId())
                .exportFormat(history.getExportFormat())
                .tablesCount(history.getTablesCount())
                .rowsCount(history.getRowsCount())
                .resultData(history.getResultData())
                .tables(List.of())
                .createdAt(history.getCreatedAt())
                .build();
    }

    private Workspace getWorkspaceOrThrow(UUID workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("Workspace not found: " + workspaceId));
    }

    private List<DataTable> getTables(UUID workspaceId) {
        return dataTableRepository.findAllByWorkspaceWorkspaceId(workspaceId)
                .stream()
                .sorted(Comparator.comparing(DataTable::getName))
                .toList();
    }

    private List<DataRelation> getRelations(UUID workspaceId) {
        return dataRelationRepository.findAllBySourceTableWorkspaceWorkspaceId(workspaceId);
    }

    private Map<UUID, List<DataColumn>> getColumnsByTableId(List<DataTable> tables) {
        return tables.stream()
                .collect(Collectors.toMap(
                        DataTable::getTableId,
                        table -> dataColumnRepository.findAllByTableTableIdOrderBySortOrderAsc(table.getTableId())
                ));
    }

    private List<GeneratedTableData> generateTables(
            GenerateDataRq rq,
            List<DataTable> tables,
            Map<UUID, List<DataColumn>> columnsByTableId
    ) {
        return tables.stream()
                .map(table -> generateTableData(rq, table, columnsByTableId.getOrDefault(table.getTableId(), List.of())))
                .toList();
    }

    private GeneratedTableData generateTableData(
            GenerateDataRq rq,
            DataTable table,
            List<DataColumn> columns
    ) {
        int rowsCount = resolveRowsCount(rq, table);
        List<Map<String, Object>> rows = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < rowsCount; rowIndex++) {
            rows.add(generateRow(columns, rowIndex));
        }

        return GeneratedTableData.builder()
                .tableId(table.getTableId())
                .tableName(table.getName())
                .rows(rows)
                .build();
    }

    private int resolveRowsCount(GenerateDataRq rq, DataTable table) {
        if (rq.rowsCount() != null && rq.rowsCount() > 0) {
            return rq.rowsCount();
        }

        if (table.getRowCount() != null && table.getRowCount() > 0) {
            return table.getRowCount();
        }

        return 10;
    }

    private Map<String, Object> generateRow(List<DataColumn> columns, int rowIndex) {
        Map<String, Object> row = new LinkedHashMap<>();

        for (DataColumn column : columns) {
            Object value = generateColumnValue(column, rowIndex);
            row.put(column.getName(), value);
        }

        return row;
    }

    private Object generateColumnValue(DataColumn column, int rowIndex) {
        if (Boolean.TRUE.equals(column.getNullable()) && random.nextInt(100) < 10) {
            return null;
        }

        if (Boolean.TRUE.equals(column.getPrimaryKey()) || Boolean.TRUE.equals(column.getUniqueValue())) {
            return generateUniqueValue(column, rowIndex);
        }

        GeneratorType generatorType = column.getGeneratorType();

        if (generatorType == null) {
            return generateByColumnType(column);
        }

        return switch (generatorType) {
            case RANDOM_STRING -> randomString(12);
            case RANDOM_NUMBER -> randomNumber(column);
            case RANDOM_BOOLEAN -> random.nextBoolean();
            case RANDOM_DATE -> randomDate(column);
            case RANDOM_UUID -> UUID.randomUUID().toString();
            case EMAIL -> randomEmail();
            case PHONE -> randomPhone();
            case FIRST_NAME -> randomFirstName();
            case LAST_NAME -> randomLastName();
            case FULL_NAME -> randomFullName();
            case ADDRESS -> randomAddress();
            case FIXED_VALUE -> column.getFixedValue();
            case CUSTOM_TEMPLATE -> generateFromTemplate(column);
            case RELATION_VALUE -> null;
        };
    }

    private Object generateUniqueValue(DataColumn column, int rowIndex) {
        GeneratorType generatorType = column.getGeneratorType();

        if (GeneratorType.EMAIL.equals(generatorType)) {
            return "user_" + (rowIndex + 1) + "_" + randomString(6).toLowerCase() + "@example.com";
        }

        if (GeneratorType.RANDOM_UUID.equals(generatorType)) {
            return UUID.randomUUID().toString();
        }

        if (GeneratorType.PHONE.equals(generatorType)) {
            return "+7" + (900_000_00_00L + rowIndex);
        }

        ColumnType columnType = column.getColumnType();

        if (columnType == ColumnType.INTEGER) {
            return rowIndex + 1;
        }

        if (columnType == ColumnType.LONG) {
            return (long) rowIndex + 1L;
        }

        if (columnType == ColumnType.UUID) {
            return UUID.randomUUID().toString();
        }

        if (columnType == ColumnType.EMAIL) {
            return "user_" + (rowIndex + 1) + "_" + randomString(6).toLowerCase() + "@example.com";
        }

        return column.getName() + "_" + (rowIndex + 1);
    }

    private Object generateByColumnType(DataColumn column) {
        ColumnType columnType = column.getColumnType();

        if (columnType == null) {
            return randomString(12);
        }

        return switch (columnType) {
            case STRING, CUSTOM -> randomString(12);
            case INTEGER -> randomInt(column);
            case LONG -> randomLong(column);
            case DOUBLE -> randomDouble(column);
            case BOOLEAN -> random.nextBoolean();
            case DATE -> randomDate(column);
            case DATETIME -> randomDateTime(column);
            case UUID -> UUID.randomUUID().toString();
            case EMAIL -> randomEmail();
            case PHONE -> randomPhone();
            case FIRST_NAME -> randomFirstName();
            case LAST_NAME -> randomLastName();
            case FULL_NAME -> randomFullName();
            case ADDRESS -> randomAddress();
        };
    }

    private Object randomNumber(DataColumn column) {
        ColumnType columnType = column.getColumnType();

        if (columnType == ColumnType.LONG) {
            return randomLong(column);
        }

        if (columnType == ColumnType.DOUBLE) {
            return randomDouble(column);
        }

        return randomInt(column);
    }

    private Integer randomInt(DataColumn column) {
        int min = parseIntOrDefault(column.getMinValue(), 1);
        int max = parseIntOrDefault(column.getMaxValue(), 10_000);

        if (max <= min) {
            return min;
        }

        return random.nextInt(max - min + 1) + min;
    }

    private Long randomLong(DataColumn column) {
        long min = parseLongOrDefault(column.getMinValue(), 1L);
        long max = parseLongOrDefault(column.getMaxValue(), 1_000_000L);

        if (max <= min) {
            return min;
        }

        return min + Math.abs(random.nextLong() % (max - min + 1));
    }

    private Double randomDouble(DataColumn column) {
        double min = parseDoubleOrDefault(column.getMinValue(), 1.0);
        double max = parseDoubleOrDefault(column.getMaxValue(), 10_000.0);

        if (max <= min) {
            return min;
        }

        return min + (max - min) * random.nextDouble();
    }

    private String randomDate(DataColumn column) {
        LocalDate minDate = parseDateOrDefault(column.getMinValue(), LocalDate.now().minusYears(5));
        LocalDate maxDate = parseDateOrDefault(column.getMaxValue(), LocalDate.now());

        if (maxDate.isBefore(minDate)) {
            return minDate.format(DATE_FORMATTER);
        }

        long days = maxDate.toEpochDay() - minDate.toEpochDay();
        LocalDate result = minDate.plusDays(random.nextLong(days + 1));

        return result.format(DATE_FORMATTER);
    }

    private String randomDateTime(DataColumn column) {
        LocalDateTime minDateTime = parseDateTimeOrDefault(column.getMinValue(), LocalDateTime.now().minusYears(5));
        LocalDateTime maxDateTime = parseDateTimeOrDefault(column.getMaxValue(), LocalDateTime.now());

        if (maxDateTime.isBefore(minDateTime)) {
            return minDateTime.format(DATETIME_FORMATTER);
        }

        long minSeconds = java.sql.Timestamp.valueOf(minDateTime).getTime() / 1000;
        long maxSeconds = java.sql.Timestamp.valueOf(maxDateTime).getTime() / 1000;
        long randomSeconds = minSeconds + Math.abs(random.nextLong() % (maxSeconds - minSeconds + 1));

        return LocalDateTime.ofEpochSecond(
                randomSeconds,
                0,
                java.time.ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now())
        ).format(DATETIME_FORMATTER);
    }

    private String randomString(int length) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            result.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return result.toString();
    }

    private String randomEmail() {
        return randomString(8).toLowerCase() + "@example.com";
    }

    private String randomPhone() {
        return "+7" + (900_000_00_00L + Math.abs(random.nextLong() % 99_999_999L));
    }

    private String randomFirstName() {
        List<String> values = List.of("Ivan", "Petr", "Alex", "Maria", "Anna", "Elena", "Dmitry", "Sergey");
        return values.get(random.nextInt(values.size()));
    }

    private String randomLastName() {
        List<String> values = List.of("Ivanov", "Petrov", "Sidorov", "Smirnov", "Volkov", "Kuznetsov");
        return values.get(random.nextInt(values.size()));
    }

    private String randomFullName() {
        return randomFirstName() + " " + randomLastName();
    }

    private String randomAddress() {
        List<String> streets = List.of("Lenina", "Tverskaya", "Arbat", "Nevsky", "Sadovaya", "Pushkina");
        return streets.get(random.nextInt(streets.size())) + " st., " + randomInt(1, 200);
    }

    private String generateFromTemplate(DataColumn column) {
        if (column.getTemplateId() == null) {
            return column.getFixedValue();
        }

        DataTemplate template = dataTemplateRepository.findById(column.getTemplateId())
                .orElseThrow(() -> new EntityNotFoundException("Data template not found: " + column.getTemplateId()));

        String pattern = template.getPattern();

        if (pattern == null || pattern.isBlank()) {
            return null;
        }

        return applyTemplatePattern(pattern);
    }

    private String applyTemplatePattern(String pattern) {
        return pattern
                .replace("{uuid}", UUID.randomUUID().toString())
                .replace("{string}", randomString(8))
                .replace("{number}", String.valueOf(randomInt(1, 10_000)))
                .replace("{boolean}", String.valueOf(random.nextBoolean()))
                .replace("{date}", LocalDate.now().minusDays(randomInt(0, 365)).format(DATE_FORMATTER))
                .replace("{datetime}", LocalDateTime.now().minusDays(randomInt(0, 365)).format(DATETIME_FORMATTER))
                .replace("{email}", randomEmail())
                .replace("{phone}", randomPhone())
                .replace("{firstName}", randomFirstName())
                .replace("{lastName}", randomLastName())
                .replace("{fullName}", randomFullName())
                .replace("{address}", randomAddress());
    }

    private void applyRelationValues(
            List<GeneratedTableData> generatedTables,
            List<DataRelation> relations
    ) {
        Map<UUID, GeneratedTableData> tableDataById = generatedTables.stream()
                .collect(Collectors.toMap(GeneratedTableData::tableId, tableData -> tableData));

        for (DataRelation relation : relations) {
            applyRelationValue(tableDataById, relation);
        }
    }

    private void applyRelationValue(
            Map<UUID, GeneratedTableData> tableDataById,
            DataRelation relation
    ) {
        if (relation.getSourceTable() == null
                || relation.getTargetTable() == null
                || relation.getSourceColumn() == null
                || relation.getTargetColumn() == null) {
            return;
        }

        GeneratedTableData sourceTableData = tableDataById.get(relation.getSourceTable().getTableId());
        GeneratedTableData targetTableData = tableDataById.get(relation.getTargetTable().getTableId());

        if (sourceTableData == null || targetTableData == null) {
            return;
        }

        if (sourceTableData.rows().isEmpty() || targetTableData.rows().isEmpty()) {
            return;
        }

        String sourceColumnName = relation.getSourceColumn().getName();
        String targetColumnName = relation.getTargetColumn().getName();

        for (int i = 0; i < sourceTableData.rows().size(); i++) {
            Map<String, Object> sourceRow = sourceTableData.rows().get(i);
            Map<String, Object> targetRow = resolveTargetRow(relation.getRelationType(), targetTableData.rows(), i);

            sourceRow.put(sourceColumnName, targetRow.get(targetColumnName));
        }
    }

    private Map<String, Object> resolveTargetRow(
            RelationType relationType,
            List<Map<String, Object>> targetRows,
            int sourceRowIndex
    ) {
        if (relationType == RelationType.ONE_TO_ONE) {
            return targetRows.get(sourceRowIndex % targetRows.size());
        }

        return targetRows.get(random.nextInt(targetRows.size()));
    }

    private Integer countRows(List<GeneratedTableData> generatedTables) {
        return generatedTables.stream()
                .map(GeneratedTableData::rows)
                .mapToInt(List::size)
                .sum();
    }

    private String exportResult(
            ExportFormat exportFormat,
            List<GeneratedTableData> generatedTables
    ) {
        if (exportFormat == null) {
            return exportToJson(generatedTables);
        }

        return switch (exportFormat) {
            case JSON -> exportToJson(generatedTables);
            case SQL -> exportToSql(generatedTables);
            case CSV -> exportToCsv(generatedTables);
        };
    }

    private String exportToJson(List<GeneratedTableData> generatedTables) {
        try {
            return objectMapper.writeValueAsString(generatedTables);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not export generated data to JSON", e);
        }
    }

    private String exportToSql(List<GeneratedTableData> generatedTables) {
        StringBuilder result = new StringBuilder();

        for (GeneratedTableData tableData : generatedTables) {
            for (Map<String, Object> row : tableData.rows()) {
                result.append("INSERT INTO ")
                        .append(tableData.tableName())
                        .append(" (")
                        .append(String.join(", ", row.keySet()))
                        .append(") VALUES (")
                        .append(row.values().stream()
                                .map(this::toSqlValue)
                                .collect(Collectors.joining(", ")))
                        .append(");")
                        .append(System.lineSeparator());
            }

            result.append(System.lineSeparator());
        }

        return result.toString();
    }

    private String toSqlValue(Object value) {
        if (value == null) {
            return "NULL";
        }

        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }

        return "'" + value.toString().replace("'", "''") + "'";
    }

    private String exportToCsv(List<GeneratedTableData> generatedTables) {
        StringBuilder result = new StringBuilder();

        for (GeneratedTableData tableData : generatedTables) {
            result.append("TABLE: ")
                    .append(tableData.tableName())
                    .append(System.lineSeparator());

            if (tableData.rows().isEmpty()) {
                result.append(System.lineSeparator());
                continue;
            }

            List<String> headers = new ArrayList<>(tableData.rows().get(0).keySet());
            result.append(String.join(",", headers)).append(System.lineSeparator());

            for (Map<String, Object> row : tableData.rows()) {
                String line = headers.stream()
                        .map(header -> toCsvValue(row.get(header)))
                        .collect(Collectors.joining(","));

                result.append(line).append(System.lineSeparator());
            }

            result.append(System.lineSeparator());
        }

        return result.toString();
    }

    private String toCsvValue(Object value) {
        if (value == null) {
            return "";
        }

        String text = value.toString();

        if (text.contains(",") || text.contains("\"") || text.contains(System.lineSeparator())) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }

        return text;
    }

    private GenerationHistory saveGenerationHistory(
            Workspace workspace,
            ExportFormat exportFormat,
            Integer tablesCount,
            Integer rowsCount,
            String resultData
    ) {
        GenerationHistory history = new GenerationHistory();
        history.setWorkspace(workspace);
        history.setExportFormat(exportFormat == null ? ExportFormat.JSON : exportFormat);
        history.setTablesCount(tablesCount);
        history.setRowsCount(rowsCount);
        history.setResultData(resultData);
        history.setCreatedAt(LocalDateTime.now());

        return generationHistoryRepository.save(history);
    }

    private GenerateDataRs buildGenerateDataRs(
            GenerationHistory history,
            List<GeneratedTableData> generatedTables
    ) {
        return GenerateDataRs.builder()
                .generationHistoryId(history.getGenerationHistoryId())
                .workspaceId(history.getWorkspace().getWorkspaceId())
                .exportFormat(history.getExportFormat())
                .tablesCount(history.getTablesCount())
                .rowsCount(history.getRowsCount())
                .resultData(history.getResultData())
                .tables(generatedTables)
                .createdAt(history.getCreatedAt())
                .build();
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return value == null ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private long parseLongOrDefault(String value, long defaultValue) {
        try {
            return value == null ? defaultValue : Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return value == null ? defaultValue : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private LocalDate parseDateOrDefault(String value, LocalDate defaultValue) {
        try {
            return value == null ? defaultValue : LocalDate.parse(value, DATE_FORMATTER);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    private LocalDateTime parseDateTimeOrDefault(String value, LocalDateTime defaultValue) {
        try {
            return value == null ? defaultValue : LocalDateTime.parse(value, DATETIME_FORMATTER);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    private int randomInt(int min, int max) {
        if (max <= min) {
            return min;
        }

        return random.nextInt(max - min + 1) + min;
    }

    private List<GeneratedTableData> sortTablesForSqlExport(
            List<GeneratedTableData> tables,
            List<DataRelation> relations
    ) {
        Map<UUID, GeneratedTableData> tableById = tables.stream()
                .collect(Collectors.toMap(GeneratedTableData::tableId, Function.identity()));

        Map<UUID, Set<UUID>> dependencies = new HashMap<>();

        for (GeneratedTableData table : tables) {
            dependencies.put(table.tableId(), new HashSet<>());
        }

        for (DataRelation relation : relations) {
            UUID sourceTableId = relation.getSourceTable().getTableId();
            UUID targetTableId = relation.getTargetTable().getTableId();

            if (dependencies.containsKey(sourceTableId) && dependencies.containsKey(targetTableId)) {
                dependencies.get(sourceTableId).add(targetTableId);
            }
        }

        List<GeneratedTableData> sorted = new ArrayList<>();
        Set<UUID> visited = new HashSet<>();
        Set<UUID> visiting = new HashSet<>();

        for (GeneratedTableData table : tables) {
            visitTableForSqlSort(
                    table.tableId(),
                    dependencies,
                    tableById,
                    visited,
                    visiting,
                    sorted
            );
        }

        return sorted;
    }

    private void visitTableForSqlSort(
            UUID tableId,
            Map<UUID, Set<UUID>> dependencies,
            Map<UUID, GeneratedTableData> tableById,
            Set<UUID> visited,
            Set<UUID> visiting,
            List<GeneratedTableData> sorted
    ) {
        if (visited.contains(tableId)) {
            return;
        }

        if (visiting.contains(tableId)) {
            throw new IllegalArgumentException("Circular table relation detected for SQL export");
        }

        visiting.add(tableId);

        for (UUID dependencyTableId : dependencies.getOrDefault(tableId, Set.of())) {
            visitTableForSqlSort(
                    dependencyTableId,
                    dependencies,
                    tableById,
                    visited,
                    visiting,
                    sorted
            );
        }

        visiting.remove(tableId);
        visited.add(tableId);

        GeneratedTableData table = tableById.get(tableId);

        if (table != null) {
            sorted.add(table);
        }
    }
}