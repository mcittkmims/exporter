package com.internship.exporter.service;

import com.internship.exporter.mapper.CompanyMapper;
import com.internship.exporter.model.*;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyDataService {

    private CompanyMapper mapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    protected void insertCompanyData(Company company) {
        List<Industry> industries = company.getIndustries();
        CompanyLocation companyLocation = company.getCompanyLocation();
        CompanyStatus companyStatus = company.getCompanyStatus();
        Country country = company.getCountry();
        if (companyLocation != null && companyLocation.getLocation() != null) {
            String key = "location:" + companyLocation.getLocation().toLowerCase();
            CompanyLocation cached = (CompanyLocation) redisTemplate.opsForValue().get(key);

            if (cached == null) {
                companyLocation = mapper.insertCompanyLocation(companyLocation);
                redisTemplate.opsForValue().set(key, companyLocation);
            } else {
                companyLocation = cached;
            }
        }

        if (companyStatus != null && companyStatus.getStatus() != null) {
            String key = "status:" + companyStatus.getStatus().toLowerCase();
            CompanyStatus cached = (CompanyStatus) redisTemplate.opsForValue().get(key);

            if (cached == null) {
                companyStatus = mapper.insertCompanyStatus(companyStatus);
                redisTemplate.opsForValue().set(key, companyStatus);
            } else {
                companyStatus = cached;
            }
        }

        if (country != null && country.getCountryName() != null) {
            String key = "country:" + country.getCountryName().toLowerCase();
            Country cached = (Country) redisTemplate.opsForValue().get(key);

            if (cached== null) {
                country = mapper.insertCountry(country);
                redisTemplate.opsForValue().set(key, country);
            } else {
                country = cached;
            }
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
        if (taxAuthority != null && taxAuthority.getAuthorityName() != null) {
            String key = "tax:" + taxAuthority.getAuthorityName().toLowerCase();
            TaxAuthority cached = (TaxAuthority) redisTemplate.opsForValue().get(key);

            if (cached == null) {
                taxAuthority = mapper.insertTaxAuthority(taxAuthority);
                redisTemplate.opsForValue().set(key, taxAuthority);
            } else {
                taxAuthority = cached;
            }

            TaxCompany taxCompany = company.getTaxCompany();
            if (taxCompany.getTaxRegistrationNumber() != null) {
                taxCompany.setTaxAuthorityId(taxAuthority.getId());
                taxCompany.setCompanyId(newCompany.getId());
                mapper.insertTaxCompany(taxCompany);
            }
        }
    }
}
