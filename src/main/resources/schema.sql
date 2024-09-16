CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    last_login TIMESTAMP,
    token      VARCHAR(255),
    is_active  BOOLEAN DEFAULT TRUE,
    created    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE phones
(
    id          UUID PRIMARY KEY,
    user_id     UUID        NOT NULL,
    number      VARCHAR(20) NOT NULL,
    city_code    VARCHAR(10) NOT NULL,
    country_code VARCHAR(10) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);