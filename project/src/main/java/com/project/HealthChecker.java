package com.project;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "헬스체커 API", description = "Health Check Controller")
@RestController
@RequestMapping("/api/health-check")
public class HealthChecker {

    @GetMapping()
    public String healthcheck() {
        return "OK";
    }
}