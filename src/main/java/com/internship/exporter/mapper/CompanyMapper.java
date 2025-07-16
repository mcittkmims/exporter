package com.internship.exporter.mapper;

import com.internship.exporter.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompanyMapper {

    Company insertCompany(Company company);

    List<Industry> insertIndustries(@Param("list") List<Industry> industries);

    void insertCompanyIndustry(@Param("company") Company company, @Param("industries")List<Industry> industries);

    TaxAuthority insertTaxAuthority(TaxAuthority taxAuthority);

    void insertTaxCompany(TaxCompany taxCompany);

    Country insertCountry(Country country);
    CompanyLocation insertCompanyLocation(CompanyLocation companyLocation);
    CompanyStatus insertCompanyStatus(CompanyStatus companyStatus);

    Country getCountryByName(@Param("countryName") String countryName);
    CompanyStatus getCompanyStatusByStatus(@Param("status") String statusName);
    CompanyLocation getCompanyLocationByLocation(@Param("location") String locationName);
    TaxAuthority getTaxAuthorityByCode(@Param("authority_code") String authorityCode);
    List<Industry> getIndustriesByCodes(@Param("codes") List<String> codes);
}