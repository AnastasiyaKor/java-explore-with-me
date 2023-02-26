DROP TABLE IF EXISTS apps,hits;
CREATE TABLE IF NOT EXISTS apps
(
    id  int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app VARCHAR
);
CREATE TABLE IF NOT EXISTS hits
(
    id        int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app_id    int REFERENCES apps (id) ON DELETE CASCADE,
    uri       VARCHAR,
    ip        VARCHAR,
    timestamp timestamp with time zone
);