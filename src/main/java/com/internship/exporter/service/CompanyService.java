package com.internship.exporter.service;

import com.internship.exporter.converter.CompanyJsonConverter;
import com.internship.exporter.model.Company;
import com.internship.exporter.model.CompanyMapping;
import com.internship.exporter.model.IndustryMapping;
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

//    public Flux<Company> processJsons(Mono<FilePart> filePartMono, CompanyMapping companyMapping, IndustryMapping industryMapping) {
//        try (JsonParser jsonParser = new JsonFactory().createParser(inputStream)) {
//            while (jsonParser.nextToken() != null) {
//                if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
//                    System.out.println(jsonParser.readValueAsTree().toString());
//                    Company company = converter.mapJsonToCompany(jsonParser.readValueAsTree().toString(), companyMapping, industryMapping);
//                    System.out.println(company);
//                    if (company.getCompanyNumber() != null) {
//                        dataService.insertCompanyData(company);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//    }

    public Flux<Company> processJsons(Mono<FilePart> filePartMono, CompanyMapping companyMapping, IndustryMapping industryMapping) {
        return filePartMono.flatMapMany(filePart ->
                splitLines(filePart.content())
                        .filter(line -> !line.isEmpty())
                        .map(line -> processJson(line, companyMapping, industryMapping))
        );
    }

    private Flux<String> splitLines(Flux<DataBuffer> dataBufferFlux) {
        return Flux.defer(() -> {
            StringBuilder partialLine = new StringBuilder();
            return dataBufferFlux
                    .map(dataBuffer -> {
                        String chunk = dataBuffer.toString(StandardCharsets.UTF_8);
                        DataBufferUtils.release(dataBuffer);
                        return chunk;
                    })
                    .concatMap(chunk -> {
                        List<String> lines = new ArrayList<>();
                        String[] split = chunk.split("\n", -1);
                        for (int i = 0; i < split.length; i++) {
                            if (i == 0) {
                                partialLine.append(split[i]);
                            } else {
                                lines.add(partialLine.toString());
                                partialLine.setLength(0);
                                partialLine.append(split[i]);
                            }
                        }
                        return Flux.fromIterable(lines);
                    })
                    .concatWith(Flux.defer(() -> {
                        if (!partialLine.isEmpty()) {
                            return Flux.just(partialLine.toString());
                        } else {
                            return Flux.empty();
                        }
                    }));
        });
    }


    private Company processJson(String s, CompanyMapping companyMapping, IndustryMapping industryMapping) {
        Company company = converter.mapJsonToCompany(s, companyMapping, industryMapping);
        if (company.getCompanyNumber() != null) {
            dataService.insertCompanyData(company);
        }
        return company;
    }


}
