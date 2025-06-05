package com.internship.exporter.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompanyMapping {
    private String companyName;
    private String companyNumber;
    private String companyStatus;
    private String companyLocation;
    private String dateOfCreation;
    private String industries;
}
