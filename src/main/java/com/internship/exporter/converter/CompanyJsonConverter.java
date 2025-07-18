package com.internship.exporter.converter;

import com.internship.exporter.model.*;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CompanyJsonConverter {

    public Company mapJsonToCompany(String json, CompanyMapping companyMapping, IndustryMapping industryMapping,
                                    TaxAuthorityMapping taxAuthorityMapping, TaxCompanyMapping taxCompanyMapping) {

        DocumentContext doc = JsonPath.parse(json);
        Company company = new Company();

        company.setCompanyName(extractStringIfPresent(doc, companyMapping.getCompanyName()));
        company.setCompanyNumber(extractStringIfPresent(doc, companyMapping.getCompanyNumber()));
        company.setCompanyLocation(extractCompanyLocationIfPresent(doc, companyMapping.getCompanyLocation()));
        company.setCompanyStatus(extractCompanyStatusIfPresent(doc, companyMapping.getCompanyStatus()));
        company.setDateOfCreation(extractDateIfPresent(doc, companyMapping.getDateOfCreation()));
        company.setCompanyAuthority(extractStringIfPresent(doc, companyMapping.getCompanyAuthority()));
        company.setIndustries(extractIndustriesIfPresent(doc, companyMapping.getIndustries(), industryMapping));
        company.setTerminationDate(extractDateIfPresent(doc, companyMapping.getTerminationDate()));
        company.setTaxAuthority(extractTaxAuthorityIfPresent(doc, companyMapping.getTaxInfo(), taxAuthorityMapping));
        company.setTaxCompany(extractTaxInfoIfPresent(doc, companyMapping.getTaxInfo(), taxCompanyMapping));
        company.setCountry(extractCountryIfPresent(companyMapping.getCountry()));

        return company;
    }

    private static String extractStringIfPresent(DocumentContext doc, String jsonPathExpression) {
        if (jsonPathExpression != null && !jsonPathExpression.isEmpty()) {
            try {
                return doc.read(jsonPathExpression);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static LocalDate extractDateIfPresent(DocumentContext doc, String jsonPathExpression) {
        if (jsonPathExpression != null && !jsonPathExpression.isEmpty()) {
            try {
                String dateString = doc.read(jsonPathExpression);
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

    private static List<Industry> extractIndustriesIfPresent(DocumentContext doc, String industriesPath,
                                                             IndustryMapping industryMapping) {
        List<Industry> industries = new ArrayList<>();

        if (industriesPath == null || industriesPath.isEmpty()) {
            return industries;
        }

        try {
            Object industriesObj = doc.read(industriesPath);

            if (industriesObj instanceof List<?>) {
                for (Object industryObj : (List<?>) industriesObj) {
                    DocumentContext indDoc = JsonPath.parse(industryObj);
                    Industry industry = new Industry();
                    industry.setIndustryCode(extractStringFromObject(indDoc, industryMapping.getIndustryCode()));
                    industry.setIndustryName(extractStringFromObject(indDoc, industryMapping.getIndustryName()));
                    industry.setIndustryDescription(extractStringFromObject(indDoc, industryMapping.getIndustryDescription()));
                    industries.add(industry);
                }
            } else if (industriesObj != null) {
                DocumentContext indDoc = JsonPath.parse(industriesObj);
                Industry industry = new Industry();
                industry.setIndustryCode(extractStringFromObject(indDoc, industryMapping.getIndustryCode()));
                industry.setIndustryName(extractStringFromObject(indDoc, industryMapping.getIndustryName()));
                industry.setIndustryDescription(extractStringFromObject(indDoc, industryMapping.getIndustryDescription()));
                industries.add(industry);
            }
        } catch (Exception ignored) {
        }

        return industries;
    }

    private static String extractStringFromObject(DocumentContext doc, String fieldPath) {
        if (fieldPath == null || fieldPath.isEmpty()) {
            return null;
        }
        try {
            return doc.read(fieldPath);
        } catch (Exception e) {
            return null;
        }
    }

    private static TaxAuthority extractTaxAuthorityIfPresent(DocumentContext doc, String taxAuthorityPath,
                                                             TaxAuthorityMapping taxAuthorityMapping) {
        if (taxAuthorityPath == null || taxAuthorityPath.isEmpty()) {
            return null;
        }

        TaxAuthority taxAuthority = new TaxAuthority();
        try {
            Object taxAuthorityObj = doc.read(taxAuthorityPath);
            if (taxAuthorityObj != null) {
                DocumentContext taxDoc = JsonPath.parse(taxAuthorityObj);
                taxAuthority.setAuthorityCode(extractStringFromObject(taxDoc, taxAuthorityMapping.getAuthorityCode()));
                taxAuthority.setAuthorityName(extractStringFromObject(taxDoc, taxAuthorityMapping.getAuthorityName()));
            }
        } catch (Exception ignored) {
        }

        return taxAuthority;
    }

    private static TaxCompany extractTaxInfoIfPresent(DocumentContext doc, String taxInfoPath,
                                                      TaxCompanyMapping taxCompanyMapping) {
        if (taxInfoPath == null || taxInfoPath.isEmpty()) {
            return null;
        }

        TaxCompany taxCompany = new TaxCompany();
        try {
            Object taxInfoObj = doc.read(taxInfoPath);

            if (taxInfoObj != null) {
                DocumentContext taxDoc = JsonPath.parse(taxInfoObj);
                taxCompany.setTaxRegistrationNumber(
                        extractStringFromObject(taxDoc, taxCompanyMapping.getTaxRegistrationNumber()));
                taxCompany.setDateOfRegistration(
                        extractDateIfPresent(taxDoc, taxCompanyMapping.getDateOfRegistration()));
                taxCompany.setTaxPayerType(
                        extractStringFromObject(taxDoc, taxCompanyMapping.getTaxPayerType()));
            }
        } catch (Exception ignored) {
        }

        return taxCompany;
    }

    private static CompanyLocation extractCompanyLocationIfPresent(DocumentContext doc, String companyLocationPath) {
        if (companyLocationPath == null || companyLocationPath.isEmpty()) {
            return null;
        }

        CompanyLocation companyLocation = new CompanyLocation();
        companyLocation.setLocation(extractStringIfPresent(doc, companyLocationPath));

        return companyLocation;
    }

    private static CompanyStatus extractCompanyStatusIfPresent(DocumentContext doc, String companyStatusPath) {
        if (companyStatusPath == null || companyStatusPath.isEmpty()) {
            return null;
        }

        CompanyStatus companyStatus = new CompanyStatus();
        companyStatus.setStatus(extractStringIfPresent(doc, companyStatusPath));

        return companyStatus;
    }

    private static Country extractCountryIfPresent(String countryPath) {
        if (countryPath == null || countryPath.isEmpty()) {
            return null;
        }

        Country country = new Country();
        country.setCountryName(countryPath);

        return country;
    }
}
