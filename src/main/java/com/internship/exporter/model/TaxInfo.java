package com.internship.exporter.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaxInfo {
    private Long id;
    private String taxRegistrationNumber;
    private LocalDate dateOfRegistration;
    private String taxPayerType;
    private Long taxAuthorityId;
    private Long companyId;

}
