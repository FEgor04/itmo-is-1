CREATE SEQUENCE admin_requests_seq
    START WITH 1
    INCREMENT BY 1;
CREATE SEQUENCE users_seq
    START WITH 1
    INCREMENT BY 1;
CREATE TABLE admin_requests (
                                id BIGINT DEFAULT nextval('admin_requests_seq') PRIMARY KEY,
                                username VARCHAR(255) NOT NULL,
                                password VARCHAR(255) NOT NULL,
                                status VARCHAR(50) NOT NULL
);
CREATE TABLE users (
                       id BIGINT DEFAULT nextval('users_seq') PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);