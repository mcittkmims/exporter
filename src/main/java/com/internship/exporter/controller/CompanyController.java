package com.internship.exporter.controller;

import com.internship.exporter.model.*;
import com.internship.exporter.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class CompanyController {

    private CompanyService service;


    @PostMapping("/upload")
    public Flux<Company> transformRawCompanyData
            (@RequestPart("companyMapping") CompanyMapping companyMapping,
             @RequestPart("industryMapping") IndustryMapping industryMapping,
             @RequestPart("taxAuthorityMapping") TaxAuthorityMapping taxAuthorityMapping,
             @RequestPart("taxInfoMapping") TaxCompanyMapping taxCompanyMapping,
             @RequestPart("data") Mono<FilePart> fileStream) throws IOException {
        return this.service.processJsons(fileStream, companyMapping, industryMapping,
                taxAuthorityMapping, taxCompanyMapping);
    }
}
