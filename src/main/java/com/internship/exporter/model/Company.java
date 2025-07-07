package com.internship.exporter.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@ToString
public class Company {
    private Long id;
    private String companyName;
    private String companyNumber;
    private LocalDate dateOfCreation;
    private String companyAuthority;
    private LocalDate terminationDate;


    List<Industry> industries;
    private TaxCompany taxCompany;
    private TaxAuthority taxAuthority;
    private CompanyLocation companyLocation;
    private CompanyStatus companyStatus;
    private Country country;
}
