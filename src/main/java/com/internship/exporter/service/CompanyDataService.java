package com.internship.exporter.service;

import com.internship.exporter.mapper.CompanyMapper;
import com.internship.exporter.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyDataService {

    private CompanyMapper mapper;

    @Transactional
    protected void insertCompanyData(Company company) {
        List<Industry> industries = company.getIndustries();
        CompanyLocation companyLocation = company.getCompanyLocation();
        CompanyStatus companyStatus = company.getCompanyStatus();
        Country country = company.getCountry();
        if (companyLocation != null) {
           companyLocation = mapper.insertCompanyLocation(companyLocation);
        }

        if (companyStatus != null) {
            companyStatus = mapper.insertCompanyStatus(companyStatus);
        }

        if (country != null) {
            country = mapper.insertCountry(country);
        }

        company.setCompanyLocation(companyLocation);
        company.setCompanyStatus(companyStatus);
        company.setCountry(country);

        Company newCompany = mapper.insertCompany(company);

        List<Industry> filteredIndustries = industries.stream()
                .filter(industry -> industry.getIndustryCode() != null)
                .collect(Collectors.toList());

        if (!filteredIndustries.isEmpty() && newCompany.getCompanyNumber() != null) {
            filteredIndustries = mapper.insertIndustries(filteredIndustries);
            mapper.insertCompanyIndustry(newCompany, filteredIndustries);
        }

        TaxAuthority taxAuthority = company.getTaxAuthority();
        if (taxAuthority.getAuthorityName() != null) {
            taxAuthority = mapper.insertTaxAuthority(taxAuthority);
            TaxCompany taxCompany = company.getTaxCompany();
            if (taxCompany.getTaxRegistrationNumber() != null) {
                taxCompany.setTaxAuthorityId(taxAuthority.getId());
                taxCompany.setCompanyId(newCompany.getId());
                mapper.insertTaxCompany(taxCompany);
            }
        }
    }
}
