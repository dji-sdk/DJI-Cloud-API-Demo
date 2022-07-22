package com.dji.sample.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dji.sample.common.model.CustomClaim;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class JwtUtil {

    private static String issuer;

    private static String subject;

    private static long age;

    private static String secret;

    private static Algorithm algorithm;

    @Value("${jwt.issuer: DJI}")
    private void setIssuer(String issuer) {
        JwtUtil.issuer = issuer;
    }

    @Value("${jwt.subject: CloudApiSample}")
    private void setSubject(String subject) {
        JwtUtil.subject = subject;
    }

    @Value("${jwt.age: 86400}")
    private void setAge(long age) {
        JwtUtil.age = age * 1000;
    }

    @Value("${jwt.secret: CloudApiSample}")
    private void setSecret(String secret) {
        JwtUtil.secret = secret;
        setAlgorithm();
    }

    private void setAlgorithm() {
        JwtUtil.algorithm = Algorithm.HMAC256(secret);
    }

    private JwtUtil() {

    }

    /**
     * Create a token based on custom information.
     * @param claims custom information
     * @return token
     */
    public static String createToken(Map<String, String> claims) {
        Date now = new Date();
        JWTCreator.Builder builder = JWT.create();
        // Add custom information to the token's payload segment.
        claims.forEach(builder::withClaim);
        String token = builder.withIssuer(issuer)
                .withSubject(subject)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + age))
                .withNotBefore(now)
                .sign(algorithm);
        log.debug("token created. " + token);
        return token;
    }

    /**
     * Verify that the token is valid.
     * @param token
     * @return
     * @throws TokenExpiredException
     */
    public static DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parses the custom information in the token into a CustomClaim object.
     * @param token
     * @return custom claim
     */
    public static Optional<CustomClaim> parseToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt == null ? Optional.empty() : Optional.of(new CustomClaim(jwt.getClaims()));
    }
}
