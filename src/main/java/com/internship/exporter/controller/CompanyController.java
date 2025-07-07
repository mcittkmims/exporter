package com.internship.exporter.controller;

import com.internship.exporter.model.*;
import com.internship.exporter.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class CompanyController {

    private CompanyService service;

    @PostMapping("/upload")
    public ResponseEntity<String> transformRawCompanyData
            (@RequestPart("companyMapping") CompanyMapping companyMapping,
             @RequestPart("industryMapping") IndustryMapping industryMapping,
             @RequestPart("taxAuthorityMapping") TaxAuthorityMapping taxAuthorityMapping,
             @RequestPart("taxInfoMapping") TaxCompanyMapping taxCompanyMapping,
             @RequestPart("data") MultipartFile fileStream) throws IOException {
        this.service.processJsons(fileStream, companyMapping, industryMapping,
                taxAuthorityMapping, taxCompanyMapping);
        return ResponseEntity.ok("Data processed successfully");
    }

}
