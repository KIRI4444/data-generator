CREATE SCHEMA IF NOT EXISTS data_generator;

CREATE TABLE data_generator.workspace
(
    workspace_id UUID PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  TEXT,
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP NOT NULL
);

CREATE TABLE data_generator.data_table
(
    table_id     UUID PRIMARY KEY,
    workspace_id UUID NOT NULL,
    name         VARCHAR(255) NOT NULL,
    display_name VARCHAR(255),
    row_count    INTEGER,
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP NOT NULL,

    CONSTRAINT fk_data_table_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES data_generator.workspace (workspace_id)
            ON DELETE CASCADE,

    CONSTRAINT uq_data_table_workspace_name
        UNIQUE (workspace_id, name)
);

CREATE TABLE data_generator.data_column
(
    column_id      UUID PRIMARY KEY,
    table_id       UUID NOT NULL,
    name           VARCHAR(255) NOT NULL,
    display_name   VARCHAR(255),
    column_type    VARCHAR(50) NOT NULL,
    generator_type VARCHAR(50) NOT NULL,
    nullable       BOOLEAN,
    unique_value   BOOLEAN,
    primary_key    BOOLEAN,
    min_value      VARCHAR(255),
    max_value      VARCHAR(255),
    fixed_value    TEXT,
    template_id    UUID,
    sort_order     INTEGER,
    created_at     TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP NOT NULL,

    CONSTRAINT fk_data_column_table
        FOREIGN KEY (table_id)
            REFERENCES data_generator.data_table (table_id)
            ON DELETE CASCADE,

    CONSTRAINT uq_data_column_table_name
        UNIQUE (table_id, name)
);

CREATE TABLE data_generator.data_template
(
    template_id    UUID PRIMARY KEY,
    workspace_id   UUID NOT NULL,
    name           VARCHAR(255) NOT NULL,
    generator_type VARCHAR(50) NOT NULL,
    pattern        TEXT,
    description    TEXT,
    created_at     TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP NOT NULL,

    CONSTRAINT fk_data_template_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES data_generator.workspace (workspace_id)
            ON DELETE CASCADE,

    CONSTRAINT uq_data_template_workspace_name
        UNIQUE (workspace_id, name)
);

CREATE TABLE data_generator.data_relation
(
    relation_id      UUID PRIMARY KEY,
    source_table_id  UUID NOT NULL,
    target_table_id  UUID NOT NULL,
    source_column_id UUID NOT NULL,
    target_column_id UUID NOT NULL,
    relation_type    VARCHAR(50) NOT NULL,
    created_at       TIMESTAMP NOT NULL,

    CONSTRAINT fk_data_relation_source_table
        FOREIGN KEY (source_table_id)
            REFERENCES data_generator.data_table (table_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_data_relation_target_table
        FOREIGN KEY (target_table_id)
            REFERENCES data_generator.data_table (table_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_data_relation_source_column
        FOREIGN KEY (source_column_id)
            REFERENCES data_generator.data_column (column_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_data_relation_target_column
        FOREIGN KEY (target_column_id)
            REFERENCES data_generator.data_column (column_id)
            ON DELETE CASCADE
);

CREATE TABLE data_generator.generation_history
(
    generation_history_id UUID PRIMARY KEY,
    workspace_id          UUID NOT NULL,
    export_format         VARCHAR(50) NOT NULL,
    tables_count          INTEGER NOT NULL,
    rows_count            INTEGER NOT NULL,
    result_data           TEXT,
    created_at            TIMESTAMP NOT NULL,

    CONSTRAINT fk_generation_history_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES data_generator.workspace (workspace_id)
            ON DELETE CASCADE
);