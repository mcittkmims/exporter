package com.internship.exporter.model;


import lombok.Data;

import java.util.List;

@Data
public class UploadRequest {
    private CompanyMapping companyMapping;
    private IndustryMapping industryMapping;
    private TaxAuthorityMapping taxAuthorityMapping;
    private TaxCompanyMapping taxInfoMapping;
    private List<String> data;
    
}
