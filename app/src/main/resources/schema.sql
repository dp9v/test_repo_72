DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

DROP TABLE IF EXISTS url_checks;

CREATE TABLE url_checks (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    status_code INT NOT NULL,
    h1 VARCHAR(255),
    title VARCHAR(255),
    description TEXT,
    url_id bigint REFERENCES urls (id),
    created_at TIMESTAMP NOT NULL
);