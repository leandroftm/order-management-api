CREATE TABLE users
(
    id    BIGINT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    createdAt DATE NOT NULL,
    updatedAt DATE NOT NULL,
    role VARCHAR(20) NOT NULL,

    CONSTRAINT UC_Email UNIQUE (email)
);