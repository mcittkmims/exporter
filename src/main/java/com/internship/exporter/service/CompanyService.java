package com.internship.exporter.service;

import com.internship.exporter.converter.CompanyJsonConverter;
import com.internship.exporter.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

    private CompanyJsonConverter converter;
    private CompanyDataService dataService;

    public List<Company> processJsons(List<String> data, CompanyMapping companyMapping,
            IndustryMapping industryMapping,
            TaxAuthorityMapping taxAuthorityMapping, TaxCompanyMapping taxCompanyMapping) throws IOException {
        List<Company> companies = new ArrayList<>();

        for (String line : data) {
            if (line != null && !line.trim().isEmpty()) {
                try {
                    Company company = processJson(line.trim(), companyMapping, industryMapping,
                            taxAuthorityMapping, taxCompanyMapping);
                    if (company != null) {
                        companies.add(company);
                    }
                } catch (Exception e) {
                    // Log the error and continue processing other lines
                    System.err.println("Error processing JSON line: " + line);
                    e.printStackTrace();
                }
            }
        }

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
