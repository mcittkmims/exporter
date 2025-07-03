CREATE TABLE IF NOT EXISTS tax_info (
    id BIGSERIAL PRIMARY KEY,
    tax_registration_number TEXT UNIQUE NOT NULL,
    date_of_registration DATE,
    tax_payer_type TEXT,
    inserted_at TIMESTAMP,
    updated_at TIMESTAMP,
    tax_authority_id BIGINT NOT NULL REFERENCES tax_authority(id),
    company_id BIGINT NOT NULL REFERENCES company(id)
);