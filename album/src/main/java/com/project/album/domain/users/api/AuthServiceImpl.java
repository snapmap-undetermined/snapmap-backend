package com.project.album.domain.users.api;

import com.project.album.common.exception.ErrorCode;
import com.project.album.common.exception.InvalidValueException;
import com.project.album.domain.users.api.interfaces.TokenService;
import com.project.album.domain.users.dto.UserDTO;
import com.project.album.domain.users.api.interfaces.AuthService;
import com.project.album.domain.users.dto.TokenDTO;
import com.project.album.domain.users.entity.Users;
import com.project.album.domain.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    @Transactional
    public UserDTO.SignUpResponse signUp(UserDTO.SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()){
            throw new InvalidValueException("이미 가입된 이메일입니다.", ErrorCode.EMAIL_DUPLICATION);
        }

        if (userRepository.findByNickname(signUpRequest.getNickname()).isPresent()) {
            throw new InvalidValueException("이미 존재하는 닉네임입니다.", ErrorCode.NICKNAME_DUPLICATION);
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
                new InvalidValueException("이메일 혹은 비밀번호를 확인해주세요.", ErrorCode.LOGIN_INPUT_INVALID));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidValueException("이메일 혹은 비밀번호를 확인해주세요.", ErrorCode.LOGIN_INPUT_INVALID);
        }

        // 토큰 발급
        TokenDTO tokenDTO = tokenService.generateAccessTokenAndRefreshToken(email, user);

        return new UserDTO.LoginResponse(user, tokenDTO.getAccessToken(), tokenDTO.getRefreshToken());
    }


}
