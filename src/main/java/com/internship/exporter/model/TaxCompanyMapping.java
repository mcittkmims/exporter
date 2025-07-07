package com.internship.exporter.model;


import lombok.Data;

@Data
public class TaxCompanyMapping {
    private String taxRegistrationNumber;
    private String dateOfRegistration;
    private String taxPayerType;
    private String taxAuthority;
    private String company;
}
