package com.project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health-check")
public class HealthChecker {

    @GetMapping()
    public String healthcheck() {
        return "OK";
    }
}