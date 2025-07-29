package com.internship.exporter.mapper;

import com.internship.exporter.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompanyMapper {
    void insertIndustries(@Param("list") List<Industry> industries);

    void insertCompanyIndustry(@Param("company") Company company, @Param("industries")List<Industry> industries);

    Long insertTaxCompany(TaxCompany taxCompany);

    Long insertTaxAuthority(TaxAuthority taxAuthority);
    Long insertCountry(Country country);
    Long insertCompanyLocation(CompanyLocation companyLocation);
    Long insertCompanyStatus(CompanyStatus companyStatus);

    List<Company> insertCompanies(@Param("list") List<Company> companies);


    List<Industry> selectIndustriesByCodes(List<String> codes);
}
