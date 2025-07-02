package com.internship.exporter.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TerminationInfo {
    private Long id;
    private LocalDate terminationDate;
    private String terminationRegistrationNumber;
    private String terminationDescription;
    private Long companyId;
}
