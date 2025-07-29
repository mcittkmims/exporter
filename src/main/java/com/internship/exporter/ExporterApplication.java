package com.internship.exporter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.internship.exporter.mapper")
@EnableCaching
public class ExporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExporterApplication.class, args);
	}

}
