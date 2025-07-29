package com.internship.exporter.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Industry {
    private Long id;
    private String industryCode;
    private String industryName;
    private String industryDescription;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Industry industry = (Industry) o;
        return Objects.equals(industryCode, industry.industryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(industryCode);
    }
}
