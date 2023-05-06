package com.project.domain.users.api;

import com.project.common.TestObjectFactory;
import com.project.common.exception.ErrorCode;
import com.project.common.exception.InvalidValueException;
import com.project.common.handler.RedisHandler;
import com.project.domain.users.dto.TokenDTO;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.dto.UserDTO.SignUpRequest;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenServiceImpl tokenService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private RedisHandler redisHandler;

    @Test
    @DisplayName("유효한 요청에 대한 회원가입이 성공한다.")
    public void signUp_working_test() {
        SignUpRequest signUpRequest = TestObjectFactory.createSingUpRequest("TEST_EAMIL@EXAMPLE.COM", "TEST_NICKNAME", "PASSWORD");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByNickname(anyString())).thenReturn(false);
        when(userRepository.save(any(Users.class))).then(AdditionalAnswers.returnsFirstArg());

        UserDTO.SignUpResponse signUpResponse = authService.signUp(signUpRequest);

        Assertions.assertEquals(signUpRequest.getEmail(), signUpResponse.getEmail());
    }

    @Test
    @DisplayName("중복 이메일에 대한 회원가입이 실패한다.")
    public void email_duplicated_signUp_fail() {
        SignUpRequest signUpRequest = TestObjectFactory.createSingUpRequest("TEST_EAMIL@EXAMPLE.COM", "TEST_NICKNAME", "PASSWORD");
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        InvalidValueException exception = Assertions.assertThrows(InvalidValueException.class, () -> authService.signUp(signUpRequest));

        Assertions.assertEquals(ErrorCode.EMAIL_DUPLICATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("중복 닉네임에 대한 회원가입이 실패한다.")
    public void nickName_duplicated_signUp_fail() {
        SignUpRequest signUpRequest = TestObjectFactory.createSingUpRequest("TEST@EXAMPLE.COM", "TEST_NICKNAME", "PASSWORD");
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByNickname(signUpRequest.getNickname())).thenReturn(true);

        InvalidValueException exception = Assertions.assertThrows(InvalidValueException.class, () -> authService.signUp(signUpRequest));

        Assertions.assertEquals(ErrorCode.NICKNAME_DUPLICATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("정상 로그인 시 액세스 토큰이 발급된다.")
    public void grant_access_token_when_login() {
        UserDTO.LoginRequest loginRequest = TestObjectFactory.createLoginRequest("TEST@EXAMPLE.COM", "PASSWORD");
        TokenDTO tokenDTO = new TokenDTO("ACCESS_TOKEN", "REFRESH_TOKEN");
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(Users.builder().email("TEST_EXAMPLE.COM").password("PASSWORD").build()));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateAccessTokenAndRefreshToken(anyString(), any(Users.class))).thenReturn(tokenDTO);

        UserDTO.LoginResponse loginResponse = authService.login(loginRequest);

        Assertions.assertEquals(tokenDTO.getAccessToken(), loginResponse.getAccessToken());
    }

    @Test
    @DisplayName("이메일 인증 메일이 정상적으로 전송된다.")
    public void send_authentication_email() throws Exception {
        UserDTO.EmailRequest emailRequest = TestObjectFactory.createEmailRequest("TEST@EXAMPLE.COM");
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        authService.sendAuthEmail(emailRequest);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
        verify(redisHandler, times(1)).setDataExpire(anyString(), anyString(), anyLong());
    }
}