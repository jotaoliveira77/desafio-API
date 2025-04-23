CREATE TABLE movies(
    movieId BIGINT PRIMARY KEY UNIQUE NOT NULL,
    title VARCHAR(min 1, max 100)  NOT NULL,
    director VARCHAR(min 1, max 100) NOT NULL,
    genre VARCHAR(min 1, max 50) NOT NULL,
)