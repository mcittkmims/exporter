package com.internship.exporter.controller;

import com.internship.exporter.model.Company;
import com.internship.exporter.model.CompanyMapping;
import com.internship.exporter.model.IndustryMapping;
import com.internship.exporter.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class CompanyController {

    private CompanyService service;

    @PostMapping("/upload")
    public ResponseEntity<String> transformRawCompanyData(@RequestPart("companyMapping") CompanyMapping companyMapping,
            @RequestPart("industryMapping") IndustryMapping industryMapping,
            @RequestPart("data") List<String> data) throws IOException {

        this.service.processJsons(data, companyMapping, industryMapping);
        return ResponseEntity.ok("Data processed successfully");
    }

}
