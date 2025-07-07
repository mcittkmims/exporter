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
}
