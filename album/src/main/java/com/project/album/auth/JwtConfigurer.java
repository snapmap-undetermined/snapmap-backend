package com.project.album.auth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfigurer {

    @Value("${spring.security.jwt.header}")
    String header;

    @Value("${spring.security.jwt.secret}")
    String secret;

    @Value("${spring.security.jwt.access-token-expired-at}")
    long accessTokenExp;

    @Value("${spring.security.jwt.refresh-token-expired-at}")
    long refreshTokenExp;

    @Value("${spring.security.jwt.prefix}")
    String prefix;

}
