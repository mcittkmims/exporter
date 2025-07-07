package com.internship.exporter.service;

import com.internship.exporter.converter.CompanyJsonConverter;
import com.internship.exporter.model.*;
import lombok.AllArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

    private CompanyJsonConverter converter;
    private CompanyDataService dataService;

    public Flux<Company> processJsons(Mono<FilePart> filePartMono, CompanyMapping companyMapping,
                                      IndustryMapping industryMapping,
                                      TaxAuthorityMapping taxAuthorityMapping, TaxCompanyMapping taxCompanyMapping) {
        return filePartMono.flatMapMany(filePart ->
                splitLines(filePart.content())
                        .filter(line -> !line.isEmpty())
                        .map(line -> processJson(line, companyMapping, industryMapping,
                                taxAuthorityMapping, taxCompanyMapping))
        );
    }

    private Flux<String> splitLines(Flux<DataBuffer> dataBufferFlux) {
        return Flux.defer(() -> {
            StringBuilder partialLine = new StringBuilder();
            return dataBufferFlux
                    .map(this::convertDataBufferToString)
                    .concatMap(chunk -> processChunk(chunk, partialLine))
                    .concatWith(getRemainingLine(partialLine));
        });
    }

    private String convertDataBufferToString(DataBuffer dataBuffer) {
        String chunk = dataBuffer.toString(StandardCharsets.UTF_8);
        DataBufferUtils.release(dataBuffer);
        return chunk;
    }

    private Flux<String> processChunk(String chunk, StringBuilder partialLine) {
        List<String> lines = new ArrayList<>();
        String[] split = chunk.split("\n", -1);

        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                partialLine.append(split[i]);
            } else {
                lines.add(partialLine.toString());
                resetPartialLine(partialLine, split[i]);
            }
        }
        return Flux.fromIterable(lines);
    }

    private void resetPartialLine(StringBuilder partialLine, String newContent) {
        partialLine.setLength(0);
        partialLine.append(newContent);
    }

    private Flux<String> getRemainingLine(StringBuilder partialLine) {
        return Flux.defer(() -> {
            if (!partialLine.isEmpty()) {
                return Flux.just(partialLine.toString());
            } else {
                return Flux.empty();
            }
        });
    }

    private Company processJson(String s, CompanyMapping companyMapping, IndustryMapping industryMapping,
                                TaxAuthorityMapping taxAuthorityMapping, TaxCompanyMapping taxCompanyMapping) {
        Company company = converter.mapJsonToCompany(s, companyMapping, industryMapping, taxAuthorityMapping, taxCompanyMapping);
        if (company.getCompanyNumber() != null) {
            dataService.insertCompanyData(company);
        }
        return company;
    }
}
