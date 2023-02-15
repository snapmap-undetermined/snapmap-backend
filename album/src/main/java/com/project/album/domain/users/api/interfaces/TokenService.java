package com.project.album.domain.users.api.interfaces;

import com.project.album.domain.users.dto.TokenDTO;
import com.project.album.domain.users.entity.Users;
import com.project.album.common.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;

public interface TokenService {

    JwtBuilder generateTokenBuilderByEmailAndExpiration(String email, Long expiredAt);
    TokenDTO generateAccessTokenAndRefreshToken(String email, Users user);
    void verifyToken(String authToken, Boolean isRefreshToken);
    Long getUserId(String authToken);

    Role getUserRole(String authToken);

    String getUserEmail(String authToken);
    Jws<Claims> parse(String authToken);

}
