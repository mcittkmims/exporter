package com.internship.exporter.service;

import com.internship.exporter.converter.CompanyJsonConverter;
import com.internship.exporter.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

    private CompanyJsonConverter converter;
    private CompanyDataService dataService;

    public List<Company> processJsons(List<String> data, CompanyMapping companyMapping,
                                      IndustryMapping industryMapping,
                                      TaxAuthorityMapping taxAuthorityMapping, TaxCompanyMapping taxCompanyMapping) {
        List<Company> companies = new ArrayList<>();

        for (String line : data) {
            if (line != null && !line.trim().isEmpty()) {
                try {
                    Company company = converter.mapJsonToCompany(line.trim(), companyMapping, industryMapping,
                            taxAuthorityMapping, taxCompanyMapping);
                    if (company != null) {
                        companies.add(company);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        dataService.insertCompanyDataBatch(companies);
        return companies;
    }
}