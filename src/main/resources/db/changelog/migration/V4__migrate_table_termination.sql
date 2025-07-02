CREATE TABLE IF NOT EXISTS termination (
    id BIGSERIAL PRIMARY KEY,
    termination_date DATE,
    termination_registration_number TEXT UNIQUE NOT NULL,
    termination_description TEXT,
    company_id BIGINT REFERENCES company(id),
    CONSTRAINT uq_company_termination UNIQUE (company_id)
);