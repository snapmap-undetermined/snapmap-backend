package com.project.domain.users.api;

import com.project.auth.JwtConfigurer;
import com.project.domain.users.api.interfaces.TokenService;
import com.project.domain.users.dto.TokenDTO;
import com.project.domain.users.entity.RefreshToken;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.RefreshTokenRepository;
import com.project.domain.users.repository.UserRepository;
import com.project.common.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfigurer jwtConfigurer;


    @Override
    public JwtBuilder generateTokenBuilderByEmailAndExpiration(String email, Long expiredAt) {
        Claims claims = Jwts.claims();

        final SecretKey signingKey = Keys.hmacShaKeyFor(jwtConfigurer.getSecret().getBytes());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiredAt);

        return Jwts.builder()
                .signWith(signingKey)
                .setClaims(claims)
                .setSubject(email)
                .setExpiration(expiryDate)
                .setIssuedAt(now);
    }

    @Override
    public TokenDTO generateAccessTokenAndRefreshToken(String email, Users user) {
        JwtBuilder accessTokenBuilder = generateTokenBuilderByEmailAndExpiration(email, jwtConfigurer.getAccessTokenExp());
        JwtBuilder refreshTokenBuilder = generateTokenBuilderByEmailAndExpiration(email, jwtConfigurer.getRefreshTokenExp());
        String accessToken = accessTokenBuilder.setAudience(user.getEmail()).claim("type", "access").compact();
        String refreshToken = refreshTokenBuilder.setAudience(user.getEmail()).claim("type", "refresh").compact();

        refreshTokenRepository.save(RefreshToken.builder().token(refreshToken).build());
        return new TokenDTO(accessToken, refreshToken);
    }

    @Override
    public void verifyToken(String authToken, Boolean isRefreshToken) {

    }

    @Override
    public Long getUserId(String authToken) {
        String email = parse(authToken).getBody().getSubject();

        return userRepository.findByEmail(email).orElseThrow(
                IllegalArgumentException::new
        ).getId();
    }

    @Override
    public Role getUserRole(String authToken) {
        String email = parse(authToken).getBody().getSubject();

        return userRepository.findByEmail(email).orElseThrow(
                IllegalArgumentException::new
        ).getRole();
    }

    @Override
    public String getUserEmail(String authToken) {
        return parse(authToken).getBody().getSubject();
    }

    @Override
    public Jws<Claims> parse(String authToken) {
        final SecretKey signingKey = Keys.hmacShaKeyFor(jwtConfigurer.getSecret().getBytes());
        String token = authToken.replace(jwtConfigurer.getPrefix(), "").trim();
        try {
            return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new AccessDeniedException(e.getMessage());
        }
    }
}
