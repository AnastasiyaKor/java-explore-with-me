DROP TABLE IF EXISTS requests, compilation_events, compilations, events, locations, categories, users;

CREATE TABLE IF NOT EXISTS users
(
    id    INT GENERATED ALWAYS AS IDENTITY,
    email VARCHAR NOT NULL UNIQUE,
    name  VARCHAR NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR UNIQUE NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  INT GENERATED ALWAYS AS IDENTITY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id),
    CONSTRAINT uniq_location UNIQUE (lat, lon)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 INT GENERATED ALWAYS AS IDENTITY,
    initiator          INT           NOT NULL,
    annotation         VARCHAR(2000) NOT NULL,
    category           INT           NOT NULL,
    confirmed_requests INT,
    description        VARCHAR(7000) NOT NULL,
    event_date         TIMESTAMP     NOT NULL,
    location           INT           NOT NULL,
    paid               BOOLEAN       NOT NULL,
    participant_limit  INT,
    request_moderation BOOLEAN       NOT NULL,
    title              VARCHAR(120)  NOT NULL,
    views              INT,
    create_on          TIMESTAMP     NOT NULL,
    published_on       TIMESTAMP,
    state              VARCHAR(10)   NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_initiator FOREIGN KEY (initiator) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_category FOREIGN KEY (category) REFERENCES categories (id) ON DELETE CASCADE,
    CONSTRAINT fk_location FOREIGN KEY (location) REFERENCES locations (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     INT GENERATED ALWAYS AS IDENTITY,
    pinned BOOLEAN,
    title  VARCHAR(120),
    CONSTRAINT pk_compilations PRIMARY KEY (id)

);

CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id INT NOT NULL,
    event_id       INT NOT NULL,
    CONSTRAINT pk_compilations_event PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE,
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests
(
    id        INT GENERATED ALWAYS AS IDENTITY,
    created   TIMESTAMP,
    event     INT NOT NULL,
    requester INT NOT NULL,
    status    VARCHAR(10),
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_event FOREIGN KEY (event) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_requester FOREIGN KEY (requester) REFERENCES users (id) ON DELETE CASCADE
)
