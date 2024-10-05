CREATE SEQUENCE cars_logs_seq
    START WITH 1
    INCREMENT BY 1;
CREATE SEQUENCE human_beings_logs_seq
    START WITH 1
    INCREMENT BY 1;
CREATE TABLE cars_logs
(
    id BIGINT DEFAULT nextval('cars_logs_seq') PRIMARY KEY,
    car_id   BIGINT       NOT NULL,
    action   VARCHAR(50)   NOT NULL,
    username VARCHAR(255) NOT NULL,
    date     TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE human_beings_logs
(
    id BIGINT DEFAULT nextval('human_beings_logs_seq') PRIMARY KEY,
    human_id BIGINT       NOT NULL,
    action   VARCHAR(50)   NOT NULL,
    username VARCHAR(255) NOT NULL,
    date     TIMESTAMP    NOT NULL DEFAULT NOW()
);