package com.internship.exporter.service;

import com.internship.exporter.mapper.CompanyMapper;
import com.internship.exporter.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CompanyDataService {

    private final CompanyMapper mapper;

    @Transactional
    public void insertCompanyDataBatch(List<Company> companies) {
        List<CompanyLocation> locations = companies.stream()
                .map(Company::getCompanyLocation)
                .filter(loc -> loc != null && loc.getLocation() != null)
                .distinct()
                .toList();
        Map<String, CompanyLocation> locationMap = batchInsertAndFetchLocations(locations);

        List<CompanyStatus> statuses = companies.stream()
                .map(Company::getCompanyStatus)
                .filter(st -> st != null && st.getStatus() != null)
                .distinct()
                .toList();
        Map<String, CompanyStatus> statusMap = batchInsertAndFetchStatuses(statuses);

        List<Country> countries = companies.stream()
                .map(Company::getCountry)
                .filter(c -> c != null && c.getCountryName() != null)
                .distinct()
                .toList();
        Map<String, Country> countryMap = batchInsertAndFetchCountries(countries);

        List<Industry> allIndustries = companies.stream()
                .flatMap(c -> c.getIndustries() != null ? c.getIndustries().stream() : Stream.empty())
                .filter(i -> i.getIndustryCode() != null)
                .distinct()
                .toList();
        Map<String, Industry> industryMap = batchInsertAndFetchIndustries(allIndustries);

        List<TaxAuthority> authorities = companies.stream()
                .map(Company::getTaxAuthority)
                .filter(a -> a != null && a.getAuthorityName() != null)
                .distinct()
                .toList();
        Map<String, TaxAuthority> authorityMap = batchInsertAndFetchTaxAuthorities(authorities);

        // Replace company references with fully populated ones containing DB IDs
        for (Company company : companies) {
            if (company.getCompanyLocation() != null && company.getCompanyLocation().getLocation() != null) {
                company.setCompanyLocation(locationMap.get(company.getCompanyLocation().getLocation().toLowerCase()));
            }
            if (company.getCompanyStatus() != null && company.getCompanyStatus().getStatus() != null) {
                company.setCompanyStatus(statusMap.get(company.getCompanyStatus().getStatus().toLowerCase()));
            }
            if (company.getCountry() != null && company.getCountry().getCountryName() != null) {
                company.setCountry(countryMap.get(company.getCountry().getCountryName().toLowerCase()));
            }
            if (company.getIndustries() != null) {
                List<Industry> resolvedIndustries = company.getIndustries().stream()
                        .filter(i -> i.getIndustryCode() != null)
                        .map(i -> industryMap.get(i.getIndustryCode().toLowerCase()))
                        .filter(Objects::nonNull)
                        .toList();
                company.setIndustries(resolvedIndustries);
            }
            if (company.getTaxAuthority() != null && company.getTaxAuthority().getAuthorityName() != null) {
                company.setTaxAuthority(authorityMap.get(company.getTaxAuthority().getAuthorityName().toLowerCase()));
            }
        }

        // Insert companies and related entities
        for (Company company : companies) {
            Company newCompany = mapper.insertCompany(company);

            if (company.getIndustries() != null && !company.getIndustries().isEmpty() && newCompany.getCompanyNumber() != null) {
                mapper.insertCompanyIndustry(newCompany, company.getIndustries());
            }

            TaxAuthority taxAuthority = company.getTaxAuthority();
            if (taxAuthority != null && taxAuthority.getAuthorityName() != null) {
                TaxCompany taxCompany = company.getTaxCompany();
                if (taxCompany != null && taxCompany.getTaxRegistrationNumber() != null) {
                    taxCompany.setTaxAuthorityId(taxAuthority.getId());
                    taxCompany.setCompanyId(newCompany.getId());
                    mapper.insertTaxCompany(taxCompany);
                }
            }
        }
    }

    private Map<String, CompanyLocation> batchInsertAndFetchLocations(List<CompanyLocation> locations) {
        Map<String, CompanyLocation> result = new HashMap<>();
        for (CompanyLocation loc : locations) {
            CompanyLocation found = mapper.getCompanyLocationByLocation(loc.getLocation());
            if (found == null) {
                found = mapper.insertCompanyLocation(loc);
            }
            result.put(loc.getLocation().toLowerCase(), found);
        }
        return result;
    }

    private Map<String, CompanyStatus> batchInsertAndFetchStatuses(List<CompanyStatus> statuses) {
        Map<String, CompanyStatus> result = new HashMap<>();
        for (CompanyStatus status : statuses) {
            CompanyStatus found = mapper.getCompanyStatusByStatus(status.getStatus());
            if (found == null) {
                found = mapper.insertCompanyStatus(status);
            }
            result.put(status.getStatus().toLowerCase(), found);
        }
        return result;
    }

    private Map<String, Country> batchInsertAndFetchCountries(List<Country> countries) {
        Map<String, Country> result = new HashMap<>();
        for (Country country : countries) {
            Country found = mapper.getCountryByName(country.getCountryName());
            if (found == null) {
                found = mapper.insertCountry(country);
            }
            result.put(country.getCountryName().toLowerCase(), found);
        }
        return result;
    }

    private Map<String, Industry> batchInsertAndFetchIndustries(List<Industry> industries) {
        Map<String, Industry> result = new HashMap<>();

        // Fetch existing industries by code in batch
        List<String> codes = industries.stream()
                .map(i -> i.getIndustryCode().toLowerCase())
                .toList();
        List<Industry> foundInDb = mapper.getIndustriesByCodes(codes);
        Map<String, Industry> foundMap = foundInDb.stream()
                .collect(Collectors.toMap(
                        i -> i.getIndustryCode().toLowerCase(),
                        i -> i
                ));

        List<Industry> toInsert = new ArrayList<>();
        for (Industry ind : industries) {
            if (!foundMap.containsKey(ind.getIndustryCode().toLowerCase())) {
                toInsert.add(ind);
            }
        }

        List<Industry> inserted = new ArrayList<>();
        if (!toInsert.isEmpty()) {
            inserted = mapper.insertIndustries(toInsert);
        }

        for (Industry i : foundInDb) {
            result.put(i.getIndustryCode().toLowerCase(), i);
        }
        for (Industry i : inserted) {
            result.put(i.getIndustryCode().toLowerCase(), i);
        }

        return result;
    }

    private Map<String, TaxAuthority> batchInsertAndFetchTaxAuthorities(List<TaxAuthority> authorities) {
        Map<String, TaxAuthority> result = new HashMap<>();
        for (TaxAuthority taxAuthority : authorities) {
            TaxAuthority found = mapper.getTaxAuthorityByCode(taxAuthority.getAuthorityCode());
            if (found == null) {
                found = mapper.insertTaxAuthority(taxAuthority);
            }
            result.put(taxAuthority.getAuthorityCode().toLowerCase(), found);
        }
        return result;
    }
}
