package com.project.domain.users.api;

import com.project.common.exception.ErrorCode;
import com.project.common.exception.InvalidValueException;
import com.project.common.handler.RedisHandler;
import com.project.domain.users.api.interfaces.TokenService;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.api.interfaces.AuthService;
import com.project.domain.users.dto.TokenDTO;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final JavaMailSender javaMailSender;
    private final RedisHandler redisHandler;

    @Override
    @Transactional
    public UserDTO.SignUpResponse signUp(UserDTO.SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new InvalidValueException("Already registered email.", ErrorCode.EMAIL_DUPLICATION);
        }

        if (userRepository.findByNickname(signUpRequest.getNickname()).isPresent()) {
            throw new InvalidValueException("Already existing nickname.", ErrorCode.NICKNAME_DUPLICATION);
        }

        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        Users user = userRepository.save(signUpRequest.toEntity());

        // 토큰 발급
        TokenDTO tokenDTO = tokenService.generateAccessTokenAndRefreshToken(signUpRequest.getEmail(), user);

        return new UserDTO.SignUpResponse(user, tokenDTO.getAccessToken(), tokenDTO.getRefreshToken());
    }

    @Override
    public UserDTO.LoginResponse login(UserDTO.LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Users user = userRepository.findByEmail(email).orElseThrow(() ->
                new InvalidValueException("Email or password is invalid.", ErrorCode.LOGIN_INPUT_INVALID));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidValueException("Email or password is invalid.", ErrorCode.LOGIN_INPUT_INVALID);
        }

        // 토큰 발급
        TokenDTO tokenDTO = tokenService.generateAccessTokenAndRefreshToken(email, user);

        return new UserDTO.LoginResponse(user, tokenDTO.getAccessToken(), tokenDTO.getRefreshToken());
    }

    @Override
    public void authEmail(UserDTO.EmailRequest emailRequest) throws Exception {

        Random random = new Random();
        String authEmailKey = String.valueOf(random.nextInt(888888) + 111111);

        sendAuthEmail(emailRequest.getEmail(), authEmailKey);
    }

    @Override
    public Boolean validateAuthEmail(UserDTO.ValidateEmailRequest validateEmailRequest) throws Exception {

        return validateEmailRequest.getAuthEmailKey().equals(redisHandler.getData(validateEmailRequest.getAuthEmailKey()));
    }

    private void sendAuthEmail(String email, String authEmailKey) {
        String emailTitle = "[Pinnit]회원가입을 위한 인증번호 안내";
        String text = "Pinnit 에 오신 걸 환영합니다! <br/> 회원 가입을 위한 인증번호는 " + authEmailKey + "입니다. <br/>";


        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(emailTitle);
            helper.setText(text, true);
            helper.setFrom("pinnit@naver.com");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        redisHandler.setDataExpire(authEmailKey, email, 60 * 5L);

    }
}
