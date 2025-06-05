CREATE TABLE IF NOT EXISTS company (
    id BIGSERIAL PRIMARY KEY,
    company_number TEXT UNIQUE NOT NULL,
    company_name TEXT NOT NULL,
    company_status TEXT,
    company_location TEXT,
    date_of_creation DATE
);