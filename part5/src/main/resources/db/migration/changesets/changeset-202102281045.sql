# From https://github.com/chucknorris-io/chuck-db

# Joke table
CREATE TABLE IF NOT EXISTS joke
(
    created_at TIMESTAMP NOT NULL ,
    joke_id    VARCHAR(255) PRIMARY KEY,
    updated_at TIMESTAMP NOT NULL ,
    value      TEXT NOT NULL
);