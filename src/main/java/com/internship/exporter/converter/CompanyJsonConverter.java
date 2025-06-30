package com.internship.exporter.converter;


import com.internship.exporter.model.Company;
import com.internship.exporter.model.CompanyMapping;
import com.internship.exporter.model.Industry;
import com.internship.exporter.model.IndustryMapping;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CompanyJsonConverter {

    public Company mapJsonToCompany(String json, CompanyMapping companyMapping, IndustryMapping industryMapping) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        Company company = new Company();
        company.setCompanyName(extractStringIfPresent(document,companyMapping.getCompanyName()));
        company.setCompanyNumber(extractStringIfPresent(document,companyMapping.getCompanyNumber()));
        company.setCompanyLocation(extractStringIfPresent(document, companyMapping.getCompanyLocation()));
        company.setCompanyStatus(extractStringIfPresent(document, companyMapping.getCompanyStatus()));
        company.setDateOfCreation(extractDateIfPresent(document, companyMapping.getDateOfCreation()));
        company.setIndustries(extractIndustriesIfPresent(document, companyMapping.getIndustries(), industryMapping));
        return company;

    }

    private static String extractStringIfPresent(Object json, String jsonPathExpression) {
        if (jsonPathExpression != null && !jsonPathExpression.isEmpty()) {
            try {
                return JsonPath.read(json, jsonPathExpression);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static LocalDate extractDateIfPresent(Object json, String jsonPathExpression) {
        if (jsonPathExpression != null && !jsonPathExpression.isEmpty()) {
            try {

                return LocalDate.parse(JsonPath.read(json, jsonPathExpression));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static List<Industry> extractIndustriesIfPresent(Object json, String industriesPath, IndustryMapping industryMapping) {
        List<Industry> industries = new ArrayList<>();

        if (industriesPath == null || industriesPath.isEmpty()) {
            return industries;
        }

        try {
            Object industriesObj = JsonPath.read(json, industriesPath);

            if (industriesObj instanceof List<?>) {
                for (Object industryObj : (List<?>) industriesObj) {
                    Industry industry = new Industry();
                    industry.setIndustryCode(extractStringFromObject(industryObj, industryMapping.getIndustryCode()));
                    industry.setIndustryName(extractStringFromObject(industryObj, industryMapping.getIndustryName()));
                    industry.setIndustryDescription(extractStringFromObject(industryObj, industryMapping.getIndustryDescription()));
                    industries.add(industry);
                }
            } else if (industriesObj != null) {
                // Single object case
                Industry industry = new Industry();
                industry.setIndustryCode(extractStringFromObject(industriesObj, industryMapping.getIndustryCode()));
                industry.setIndustryName(extractStringFromObject(industriesObj, industryMapping.getIndustryName()));
                industry.setIndustryDescription(extractStringFromObject(industriesObj, industryMapping.getIndustryDescription()));
                industries.add(industry);
            }
        } catch (Exception ignored) {
        }

        return industries;
    }
    
    private static String extractStringFromObject(Object obj, String fieldPath) {
        if (fieldPath == null || fieldPath.isEmpty()) {
            return null;
        }
        try {
            return JsonPath.read(obj, fieldPath);
        } catch (Exception e) {
            return null;
        }
    }
}





