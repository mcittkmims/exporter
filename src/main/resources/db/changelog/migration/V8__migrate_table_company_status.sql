CREATE TABLE IF NOT EXISTS company_status (
    id BIGSERIAL PRIMARY KEY,
    status TEXT UNIQUE NOT NULL,
    inserted_at TIMESTAMP,
    updated_at TIMESTAMP
)