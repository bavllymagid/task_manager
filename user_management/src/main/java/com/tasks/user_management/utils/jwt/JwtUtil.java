package com.tasks.user_management.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.tasks.user_management.local.models.User;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import org.springframework.stereotype.Component;

import java.util.Date;



@Component
public class JwtUtil {
    public String generateToken(User user, Date expirationDate, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(expirationDate) // set expiration date
                .sign(algorithm);
    }

    public String validateToken(String token, String secret) throws TokenValidationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            throw new TokenValidationException("Invalid Token");
        }
    }
}
