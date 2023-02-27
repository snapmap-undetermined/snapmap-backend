package com.project.auth;

import com.project.common.exception.ErrorCode;
import com.project.common.exception.InvalidValueException;
import com.project.domain.users.api.interfaces.TokenService;
import com.project.domain.users.dto.OAuthAttributes;
import com.project.domain.users.dto.TokenDTO;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Map<String, Object> kakao_account = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        String email = (String) kakao_account.get("email");

        Users user = userRepository.findByEmail(email).orElseThrow(() ->
                new InvalidValueException("이메일 혹은 비밀번호를 확인해주세요.", ErrorCode.LOGIN_INPUT_INVALID));


        // 토큰 발급
        TokenDTO tokenDTO = tokenService.generateAccessTokenAndRefreshToken(email, user);

        String targetUrl = UriComponentsBuilder.fromUriString("/home")
                .queryParam("token", tokenDTO)
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
