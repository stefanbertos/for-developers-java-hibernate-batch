package com.example.demo;

import com.example.demo.service.DataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    final
    DataService dataService;

    @Value("${filePath}")
    private String filePath;

    public DemoApplication(DataService dataService) {
        this.dataService = dataService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        dataService.processData(filePath);
    }
}
