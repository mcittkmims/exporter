package com.internship.exporter.converter;


import com.internship.exporter.model.*;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CompanyJsonConverter {

    public Company mapJsonToCompany(String json, CompanyMapping companyMapping, IndustryMapping industryMapping,
                                    TaxAuthorityMapping taxAuthorityMapping, TaxInfoMapping taxInfoMapping) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        Company company = new Company();
        company.setCompanyName(extractStringIfPresent(document,companyMapping.getCompanyName()));
        company.setCompanyNumber(extractStringIfPresent(document,companyMapping.getCompanyNumber()));
        company.setCompanyLocation(extractStringIfPresent(document, companyMapping.getCompanyLocation()));
        company.setCompanyStatus(extractStringIfPresent(document, companyMapping.getCompanyStatus()));
        company.setDateOfCreation(extractDateIfPresent(document, companyMapping.getDateOfCreation()));
        company.setCompanyAuthority(extractStringIfPresent(document, companyMapping.getCompanyAuthority()));
        company.setIndustries(extractIndustriesIfPresent(document, companyMapping.getIndustries(), industryMapping));
        company.setTerminationDate(extractDateIfPresent(document, companyMapping.getTerminationDate()));
        company.setTaxAuthority(extractTaxAuthorityIfPresent(document, companyMapping.getTaxInfo(), taxAuthorityMapping));
        company.setTaxInfo(extractTaxInfoIfPresent(document, companyMapping.getTaxInfo(), taxInfoMapping));
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
                String dateString = JsonPath.read(json, jsonPathExpression);

                if (dateString == null || dateString.isEmpty()) {
                    return null;
                }

                String trimmed = dateString.contains(";") ? dateString.split(";")[0].trim() : dateString.trim();

                try {
                    return LocalDate.parse(trimmed);
                } catch (Exception e) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    return LocalDate.parse(trimmed, formatter);
                }

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

    private static TaxAuthority extractTaxAuthorityIfPresent(Object json, String taxAuthorityPath, TaxAuthorityMapping taxAuthorityMapping) {
        if (taxAuthorityPath == null || taxAuthorityPath.isEmpty()) {
            return null;
        }

        TaxAuthority taxAuthority = new TaxAuthority();
        try {
            Object taxAuthorityObj = JsonPath.read(json, taxAuthorityPath);
            if (taxAuthorityObj != null) {
                taxAuthority.setAuthorityCode(extractStringFromObject(taxAuthorityObj, taxAuthorityMapping.getAuthorityCode()));
                taxAuthority.setAuthorityName(extractStringFromObject(taxAuthorityObj, taxAuthorityMapping.getAuthorityName()));
            }
        } catch (Exception ignored){
        }

        return taxAuthority;
    }

    private static TaxInfo extractTaxInfoIfPresent(Object json, String taxInfoPath, TaxInfoMapping taxInfoMapping) {
        if (taxInfoPath == null || taxInfoPath.isEmpty()) {
            return null;
        }

        TaxInfo taxInfo = new TaxInfo();
        try {
            Object taxInfoObj = JsonPath.read(json, taxInfoPath);

            if (taxInfoObj != null) {
                taxInfo.setTaxRegistrationNumber(extractStringFromObject(taxInfoObj, taxInfoMapping.getTaxRegistrationNumber()));
                taxInfo.setDateOfRegistration(extractDateIfPresent(taxInfoObj, taxInfoMapping.getDateOfRegistration()));
                taxInfo.setTaxPayerType(extractStringFromObject(taxInfoObj, taxInfoMapping.getTaxPayerType()));
            }
        } catch (Exception ignored){}

        return taxInfo;
    }
}





