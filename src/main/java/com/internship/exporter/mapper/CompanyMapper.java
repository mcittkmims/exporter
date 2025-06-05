package com.internship.exporter.mapper;

import com.internship.exporter.model.Company;
import com.internship.exporter.model.Industry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompanyMapper {

    Company insertCompany(Company company);

    List<Industry> insertIndustries(@Param("list") List<Industry> industries);

    void insertCompanyIndustry(@Param("company") Company company, @Param("industries")List<Industry> industries);
}
