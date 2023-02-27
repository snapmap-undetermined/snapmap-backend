package com.project.domain.users.api;

import com.project.domain.users.api.interfaces.CustomOAuth2Service;
import com.project.domain.users.api.interfaces.TokenService;
import com.project.domain.users.dto.OAuthAttributes;
import com.project.domain.users.dto.TokenDTO;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2ServiceImpl implements CustomOAuth2Service {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.
                of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Users user = saveOrUpdate(attributes);
        tokenService.generateAccessTokenAndRefreshToken(user.getEmail(), user);
//        return new UserDTO.LoginResponse(user, tokenDTO.getAccessToken(), tokenDTO.getRefreshToken());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private Users saveOrUpdate(OAuthAttributes attributes) {
        Users user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.updateByOAuth(attributes.getName(),attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }

}
