package com.internship.exporter.model;

import lombok.Data;

@Data
public class CompanyMapping {
    private String companyName;
    private String companyNumber;
    private String companyStatus;
    private String companyLocation;
    private String dateOfCreation;
    private String industries;
    private String companyAuthority;
    private String terminationDate;
    private String taxInfo;
    private String taxAuthority;
}
