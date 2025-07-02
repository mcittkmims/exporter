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
    private String companyStatus;
    private String companyLocation;
    private LocalDate dateOfCreation;
    private String companyAuthority;
    private LocalDate terminationDate;


    List<Industry> industries;
    private TaxInfo taxInfo;
    private TaxAuthority taxAuthority;
}
