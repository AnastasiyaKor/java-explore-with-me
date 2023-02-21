DROP TABLE IF EXISTS hits;
CREATE TABLE IF NOT EXISTS hits
(
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app VARCHAR,
    apps VARCHAR,
    uri VARCHAR,
    ip VARCHAR,
    timestamp timestamp with time zone
);
