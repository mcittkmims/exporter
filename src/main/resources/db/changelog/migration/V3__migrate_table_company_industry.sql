CREATE TABLE IF NOT EXISTS company_industry (
    id         BIGSERIAL PRIMARY KEY,
    company_id BIGINT REFERENCES company(id),
    industry_id BIGINT REFERENCES industry(id),
    inserted_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uq_company_product UNIQUE (company_id, industry_id)
);