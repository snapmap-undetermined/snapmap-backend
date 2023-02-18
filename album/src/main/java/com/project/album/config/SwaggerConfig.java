package com.project.album.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Couple API",
                description = "couple api 명세",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

//    @Bean
//    public GroupedOpenApi chatOpenApi() {
//        String[] paths = {"/v1/**"};
//
//        return GroupedOpenApi.builder()
//                .group("채팅서비스 API v1")
//                .pathsToMatch(paths)
//                .build();
//    }
}