CREATE TABLE IF NOT EXISTS company_location (
    id BIGSERIAL PRIMARY KEY,
    location TEXT UNIQUE NOT NULL,
    inserted_at TIMESTAMP,
    updated_at TIMESTAMP
)