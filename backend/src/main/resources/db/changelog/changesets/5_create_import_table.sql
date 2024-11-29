CREATE TABLE import
(
    id                     BIGSERIAL PRIMARY KEY,
    status                 VARCHAR(50) NOT NULL,
    message                TEXT,
    created_entities_count BIGINT      NOT NULL,
    created_at             TIMESTAMP   NOT NULL,
    finished_at            TIMESTAMP,
    user_id                BIGINT      NOT NULL REFERENCES users (id)
);
