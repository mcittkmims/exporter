package com.internship.exporter.service;

import com.internship.exporter.mapper.CompanyMapper;
import com.internship.exporter.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyDataService {

    private final CompanyMapper mapper;
    private final CompanyCacheService cacheService;
    private static final Object INDUSTRY_LOCK = new Object();

    public void insertCompanyDataBatch(List<Company> companies) {
        for (Company company : companies) {
            if (company.getCompanyLocation() != null && company.getCompanyLocation().getLocation() != null) {
                company.setCompanyLocation(cacheService.resolveCompanyLocation(company.getCompanyLocation()));
            }

            if (company.getCompanyStatus() != null && company.getCompanyStatus().getStatus() != null) {
                company.setCompanyStatus(cacheService.resolveCompanyStatus(company.getCompanyStatus()));
            }

            if (company.getCountry() != null && company.getCountry().getCountryName() != null) {
                company.setCountry(cacheService.resolveCountry(company.getCountry()));
            }

            if (company.getTaxAuthority() != null && company.getTaxAuthority().getAuthorityName() != null) {
                company.setTaxAuthority(cacheService.resolveTaxAuthority(company.getTaxAuthority()));
            }
        }

        List<Company> persistedCompanies = mapper.insertCompanies(companies);

        Map<String, Company> companyByNumber = persistedCompanies.stream()
                .collect(Collectors.toMap(Company::getCompanyNumber, c -> c));

        for (int i = 0; i < companies.size(); i++) {
            Company c = companies.get(i);
            Company dbCompany = companyByNumber.get(c.getCompanyNumber());
            if (dbCompany != null) {
                dbCompany.setIndustries(c.getIndustries());
                dbCompany.setTaxCompany(c.getTaxCompany());
                dbCompany.setTaxAuthority(c.getTaxAuthority());

                companies.set(i, dbCompany);
            }
        }

        List<Industry> allIndustries     = companies.stream()
                .flatMap(company -> company.getIndustries() != null ? company.getIndustries().stream() : List.<Industry>of().stream())
                .filter(ind -> ind.getIndustryCode() != null)
                .distinct()
                .toList();

        if (!allIndustries.isEmpty()) {
            System.out.println("Attempting to acquire INDUSTRY_LOCK...");
            synchronized (INDUSTRY_LOCK) {
                System.out.println("Acquired INDUSTRY_LOCK.");
                mapper.insertIndustries(allIndustries);
            }
            System.out.println("Released INDUSTRY_LOCK.");
        }

        for (Company company : companies) {
            List<Industry> industries = company.getIndustries();
            if (industries != null && !industries.isEmpty()) {
                List<Industry> filtered = industries.stream()
                        .filter(ind -> ind.getIndustryCode() != null)
                        .toList();

                if (!filtered.isEmpty()) {
                    List<Industry> insertedIndustries = mapper.selectIndustriesByCodes(
                            filtered.stream().map(Industry::getIndustryCode).toList());
                    mapper.insertCompanyIndustry(company, insertedIndustries);
                }
            }
        }

        for (Company company : companies) {
            TaxCompany taxCompany = company.getTaxCompany();
            TaxAuthority taxAuthority = company.getTaxAuthority();

            if (taxCompany != null && taxCompany.getTaxRegistrationNumber() != null
                    && taxAuthority != null && taxAuthority.getId() != null
                    && company.getId() != null) {
                taxCompany.setTaxAuthorityId(taxAuthority.getId());
                taxCompany.setCompanyId(company.getId());
                mapper.insertTaxCompany(taxCompany);
            }
        }
    }

}
