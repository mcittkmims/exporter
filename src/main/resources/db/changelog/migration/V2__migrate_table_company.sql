CREATE TABLE IF NOT EXISTS company (
    id BIGSERIAL PRIMARY KEY,
    company_number TEXT UNIQUE NOT NULL,
    company_name TEXT,
    date_of_creation DATE,
    company_authority TEXT,
    termination_date DATE,
    inserted_at TIMESTAMP,
    updated_at TIMESTAMP,
    country_id BIGINT REFERENCES country(id),
    location_id BIGINT REFERENCES company_location(id),
    status_id BIGINT REFERENCES company_status(id)
);