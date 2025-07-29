package com.internship.exporter.service;

import com.internship.exporter.mapper.CompanyMapper;
import com.internship.exporter.model.CompanyLocation;
import com.internship.exporter.model.CompanyStatus;
import com.internship.exporter.model.Country;
import com.internship.exporter.model.TaxAuthority;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyCacheService {
    private final CompanyMapper mapper;

    @Cacheable(value = "locations", key = "#location.location")
    public CompanyLocation resolveCompanyLocation(CompanyLocation location) {
        Long id = mapper.insertCompanyLocation(location);
        location.setId(id);
        return location;
    }

    @Cacheable(value = "statuses", key = "#status.status")
    public CompanyStatus resolveCompanyStatus(CompanyStatus status) {
        Long id = mapper.insertCompanyStatus(status);
        status.setId(id);
        return status;
    }

    @Cacheable(value = "countries", key = "#country.countryName")
    public Country resolveCountry(Country country) {
        Long id = mapper.insertCountry(country);
        country.setId(id);
        return country;
    }

    @Cacheable(value = "taxAuthorities", key = "#tax.authorityName")
    public TaxAuthority resolveTaxAuthority(TaxAuthority tax) {
        Long id = mapper.insertTaxAuthority(tax);
        tax.setId(id);
        return tax;
    }
}
