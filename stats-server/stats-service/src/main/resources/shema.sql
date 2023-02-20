DROP TABLE IF EXISTS endpoint_hit;
CREATE TABLE IF NOT EXISTS endpoint_hit
(
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app VARCHAR,
    uri VARCHAR,
    ip VARCHAR,
    timestamp timestamp with time zone
);
