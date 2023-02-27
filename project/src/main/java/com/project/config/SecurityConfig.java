package com.project.config;


import com.project.auth.CustomCorsFilter;
import com.project.auth.CustomOAuth2AuthenticationSuccessHandler;
import com.project.auth.PermissionInterceptor;
import com.project.common.handler.AuthUserResolver;
import com.project.domain.users.api.CustomOAuth2ServiceImpl;
import com.project.domain.users.api.interfaces.CustomOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    private final CustomCorsFilter customCorsFilter;
    private final PermissionInterceptor permissionInterceptor;
    private final AuthUserResolver authUserResolver;
    private final CustomOAuth2Service customOAuth2Service;

    private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((request) ->
                        request.requestMatchers("/**").permitAll()
                );
//                .oauth2Login()
//                .userInfoEndpoint()
//                .userService(customOAuth2Service)
//                .and()
//                .successHandler(customOAuth2AuthenticationSuccessHandler);

        return httpSecurity.build();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserResolver);
    }
}

