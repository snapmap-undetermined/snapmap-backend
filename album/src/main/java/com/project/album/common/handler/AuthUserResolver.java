package com.project.album.common.handler;

import com.project.album.common.annotation.AuthUser;
import com.project.album.common.domain.users.api.interfaces.TokenService;
import com.project.album.common.domain.users.entity.Users;
import com.project.album.common.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthUserResolver implements HandlerMethodArgumentResolver {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(AuthUser.class);
        boolean isUserType = Users.class.isAssignableFrom(parameter.getParameterType());

        return hasAnnotation && isUserType;

    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jwt = webRequest.getHeader("Authorization");

        return userRepository.findById(tokenService.getUserId(jwt)).orElseThrow(
                () -> new Exception("인증되지 않은 사용자입니다.")
        );
    }
}
