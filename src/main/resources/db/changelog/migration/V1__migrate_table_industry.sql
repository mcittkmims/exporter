CREATE TABLE IF NOT EXISTS industry (
    id BIGSERIAL PRIMARY KEY,
    industry_code TEXT UNIQUE NOT NULL,
    industry_name TEXT,
    industry_description TEXT,
    inserted_at TIMESTAMP,
    updated_at TIMESTAMP
);