package ca.sait.aris.lims.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

/**
 * JWT util class
 * Responsible for generating and verifying tokens, as well as securely extracting payload information.
 */
public class TokenUtil {

    // In a production env, this key must be read from environment var or configuration files; here, it's hardcoded for the Sprint 2 demo.
    private static final String SECRET_KEY = "Lims_Super_Secret_Key_Sprint2_2026";
    
    // Token validity period: 24 hours (unit: milliseconds)
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000L;
    
    // introduces HMAC SHA-256 algorithm
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    
    // Pre-built validators improve performance
    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build();

    /**
     * generates JWT Token
     * Payload strictly contains userId, email, role
     */
    public static String generateToken(String userId, String email, String role) {
        return JWT.create()
                .withClaim("userId", userId)
                .withClaim("email", email)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(ALGORITHM);
    }

    /**
     * Verify that the token is valid and not expired
     * @return Returns a DecodedJWT object if valid, otherwise returns null
     */
    public static DecodedJWT verifyToken(String token) {
        try {
            return VERIFIER.verify(token);
        } catch (JWTVerificationException exception) {
            // Token has been tampered with, expired, or is in the wrong format.
            return null;
        }
    }

    /**
     * Extract userId from valid token
     */
    public static String getUserId(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null ? jwt.getClaim("userId").asString() : null;
    }

    /**
     * Extract email from valid token 
     */
    public static String getEmail(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null ? jwt.getClaim("email").asString() : null;
    }

    /**
     * Extract role from valid token
     */
    public static String getRole(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null ? jwt.getClaim("role").asString() : null;
    }
}