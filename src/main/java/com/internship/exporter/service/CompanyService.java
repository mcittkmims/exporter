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

        data.parallelStream()
                .filter(line -> line != null && !line.trim().isEmpty())
                .forEach(line -> {
                    try {
                        Company company = processJson(line.trim(), companyMapping, industryMapping,
                                taxAuthorityMapping, taxCompanyMapping);
                        if (company != null) {
                        }
                    } catch (Exception ignored) {}
                });

        return companies;
    }

    private Company processJson(String jsonLine, CompanyMapping companyMapping, IndustryMapping industryMapping,
            TaxAuthorityMapping taxAuthorityMapping, TaxCompanyMapping taxCompanyMapping) {
        Company company = converter.mapJsonToCompany(jsonLine, companyMapping, industryMapping, taxAuthorityMapping,
                taxCompanyMapping);
        if (company != null && company.getCompanyNumber() != null) {
            dataService.insertCompanyData(company);
        }
        return company;
    }
}
