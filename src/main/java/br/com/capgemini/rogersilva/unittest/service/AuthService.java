package br.com.capgemini.rogersilva.unittest.service;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.capgemini.rogersilva.unittest.dto.AccessTokenDto;
import br.com.capgemini.rogersilva.unittest.model.User;
import io.jsonwebtoken.Jwts;

@Service
@Transactional
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public AccessTokenDto createAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        String accessToken = Jwts.builder().setIssuer("Process Manager API").setSubject(user.getId().toString())
                .setIssuedAt(now).setExpiration(expirationDate).signWith(HS256, secret).compact();

        return AccessTokenDto.builder().accessToken(accessToken).tokenType("Bearer").build();
    }

    public boolean isAccessTokenValid(String accessToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(accessToken);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public Long getUserId(String accessToken) {
        return Long.parseLong(Jwts.parser().setSigningKey(secret).parseClaimsJws(accessToken).getBody().getSubject());
    }
}