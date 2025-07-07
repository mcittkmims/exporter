CREATE TABLE IF NOT EXISTS country (
    id BIGSERIAL PRIMARY KEY,
    country_name TEXT UNIQUE NOT NULL,
    inserted_at TIMESTAMP,
    updated_at TIMESTAMP
)