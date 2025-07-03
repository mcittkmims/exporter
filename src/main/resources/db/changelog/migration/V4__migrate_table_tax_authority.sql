CREATE TABLE IF NOT EXISTS tax_authority (
    id BIGSERIAL PRIMARY KEY,
    authority_code TEXT NOT NULL,
    authority_name TEXT NOT NULL,
    inserted_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT unique_code_name UNIQUE (authority_code, authority_name)
);