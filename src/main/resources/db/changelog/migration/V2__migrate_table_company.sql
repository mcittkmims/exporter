CREATE TABLE IF NOT EXISTS company (
    id BIGSERIAL PRIMARY KEY,
    company_number TEXT UNIQUE NOT NULL,
    company_name TEXT,
    company_status TEXT,
    company_location TEXT,
    date_of_creation DATE,
    company_authority TEXT,
    termination_date DATE
);