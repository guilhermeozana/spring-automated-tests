CREATE TABLE person (
    id BIGINT PRIMARY KEY UNIQUE NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    address TEXT NOT NULL,
    gender TEXT NOT NULL,
    email TEXT NOT NULL
);