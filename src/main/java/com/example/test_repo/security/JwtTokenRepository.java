package com.example.test_repo.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;

@Repository
public class JwtTokenRepository implements CsrfTokenRepository {

    private String secret;
    private int jwtExpirationInSeconds;

    public String getSecret() {
        return secret;
    }

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.jwtExpirationInSeconds}")
    public void setJwtExpirationInMs(int jwtExpirationInSeconds) {
        this.jwtExpirationInSeconds = jwtExpirationInSeconds;
    }

    public JwtTokenRepository() {
        this.secret = getSecret();
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        String id = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
        Date expiredDate = Date.from(LocalDateTime.now().plusSeconds(jwtExpirationInSeconds)
                .atZone(ZoneId.systemDefault()).toInstant());

        String token = "";
        try {
            token = Jwts.builder()
                    .setId(id)
                    .setIssuedAt(now)
                    .setNotBefore(now)
                    .setExpiration(expiredDate)
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
        } catch (JwtException e) {
            e.printStackTrace();
            //ignore
        }
        return new DefaultCsrfToken("x-csrf-token", "_csrf", token);
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        if (Objects.nonNull(token)) {
            if (!response.getHeaderNames().contains(ACCESS_CONTROL_EXPOSE_HEADERS))
                response.addHeader(ACCESS_CONTROL_EXPOSE_HEADERS, token.getHeaderName());

            if (response.getHeaderNames().contains(token.getHeaderName()))
                response.setHeader(token.getHeaderName(), token.getToken());
            else
                response.addHeader(token.getHeaderName(), token.getToken());
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

    public void clearToken(HttpServletResponse response) {
        if (response.getHeaderNames().contains("x-csrf-token"))
            response.setHeader("x-csrf-token", "");
    }

}
