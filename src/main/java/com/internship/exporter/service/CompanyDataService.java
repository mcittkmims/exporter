package com.internship.exporter.service;

import com.internship.exporter.mapper.CompanyMapper;
import com.internship.exporter.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyDataService {

    private CompanyMapper mapper;

    @Transactional
    protected void insertCompanyData(Company company) {
        List<Industry> industries = company.getIndustries();
        Company newCompany = mapper.insertCompany(company);
        if (!industries.isEmpty() && newCompany.getCompanyNumber() != null) {
            industries = mapper.insertIndustries(industries);
            mapper.insertCompanyIndustry(newCompany, industries);
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
