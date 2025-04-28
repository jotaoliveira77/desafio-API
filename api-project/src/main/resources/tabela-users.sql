CREATE TABLE users (
    id PRIMARY KEY UNIQUE NOT NULL,
    username VARCHAR(min 5, max 30) UNIQUE NOT NULL,
    password VARCHAR(min 8, max 30) NOT NULL,
    email VARCHAR(max 50) UNIQUE NOT NULL,
    enabled BOOLEAN NOT NULL
);