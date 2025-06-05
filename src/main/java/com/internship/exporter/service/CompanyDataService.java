package com.internship.exporter.service;

import com.internship.exporter.mapper.CompanyMapper;
import com.internship.exporter.model.Company;
import com.internship.exporter.model.Industry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyDataService {

    private CompanyMapper mapper;

    @Transactional
    protected void insertCompanyData(Company company) {
        List<Industry> industries = company.getIndustries();
        company = mapper.insertCompany(company);
        if (!industries.isEmpty() && company.getCompanyNumber() != null) {
            industries = mapper.insertIndustries(industries);
            mapper.insertCompanyIndustry(company, industries);
        }
    }
}
