package com.internship.exporter.controller;

import com.internship.exporter.model.*;
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
    public ResponseEntity<String> transformRawCompanyData(@RequestBody UploadRequest request) throws IOException {
        this.service.processJsons(request.getData(), request.getCompanyMapping(),
                request.getIndustryMapping(), request.getTaxAuthorityMapping(),
                request.getTaxInfoMapping());
        return ResponseEntity.ok("Data processed successfully");
    }
}
