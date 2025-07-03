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
            TaxInfo taxInfo = company.getTaxInfo();
            if (taxInfo.getTaxRegistrationNumber() != null) {
                taxInfo.setTaxAuthorityId(taxAuthority.getId());
                taxInfo.setCompanyId(newCompany.getId());
                mapper.insertTaxInfo(taxInfo);
            }
        }
    }
}
