CREATE TABLE IF NOT EXISTS company_location (
    id BIGSERIAL PRIMARY KEY,
    location TEXT UNIQUE,
    inserted_at TIMESTAMP,
    updated_at TIMESTAMP
)