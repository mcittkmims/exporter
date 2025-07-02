package com.internship.exporter.model;


import lombok.Data;

@Data
public class TaxInfoMapping {
    private String taxRegistrationNumber;
    private String dateOfRegistration;
    private String taxPayerType;
    private String taxAuthority;
    private String company;
}
