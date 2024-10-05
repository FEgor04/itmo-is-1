DELETE
FROM car;

DELETE
FROM human_being;

ALTER TABLE car
    ADD COLUMN owner_id BIGINT NOT NULL;

ALTER TABLE car
    ADD CONSTRAINT fk_car_owner FOREIGN KEY (owner_id) REFERENCES users (id);

ALTER TABLE human_being
    ADD COLUMN owner_id BIGINT NOT NULL;

ALTER TABLE human_being
    ADD CONSTRAINT fk_human_being_owner FOREIGN KEY (owner_id) REFERENCES users (id);

