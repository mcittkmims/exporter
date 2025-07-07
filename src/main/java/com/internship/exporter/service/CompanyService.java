package com.internship.exporter.service;

import com.internship.exporter.converter.CompanyJsonConverter;
import com.internship.exporter.model.Company;
import com.internship.exporter.model.CompanyMapping;
import com.internship.exporter.model.IndustryMapping;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

    private CompanyJsonConverter converter;
    private CompanyDataService dataService;

    public List<Company> processJsons(List<String> ndjsonLines, CompanyMapping companyMapping,
            IndustryMapping industryMapping) {
        List<Company> companies = new ArrayList<>();

        for (String jsonLine : ndjsonLines) {
            if (jsonLine != null && !jsonLine.trim().isEmpty()) {
                try {
                    Company company = converter.mapJsonToCompany(jsonLine.trim(), companyMapping, industryMapping);
                    if (company != null && company.getCompanyNumber() != null) {
                        dataService.insertCompanyData(company);
                        companies.add(company);
                    }
                } catch (Exception e) {
                    // Log the error and continue processing other lines
                    System.err.println("Error processing JSON line: " + jsonLine);
                    e.printStackTrace();
                }
            }
        }

        return companies;
    }
}
