CREATE TABLE urls (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

INSERT INTO urls (name, created_at) VALUES ('https://habr.com', '2022-09-08 21:16:28.105')

CREATE TABLE url_checks (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    status_code INT NOT NULL,
    h1 VARCHAR(255),
    title VARCHAR(255),
    description TEXT,
    url_id bigint REFERENCES urls (id),
    created_at TIMESTAMP NOT NULL
);

INSERT INTO url_checks (status_code, h1, title, description, url_id, created_at) VALUES ('200', 'h1', 'title', 'description', 1, '2023-09-08 22:00:28.105')