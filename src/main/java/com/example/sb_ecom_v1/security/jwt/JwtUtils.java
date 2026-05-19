package com.example.sb_ecom_v1.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    // Getting JWT From Header
    public String getJwtFromHeader(HttpServletRequest request){
        // getting header (Authorization) "
        String bearerToken = request.getHeader("Authorization");
        logger.info("Authorization header: {}", bearerToken != null ? "EXISTS" : "MISSING");
        logger.debug("Authorization Header : {} ",bearerToken);
        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7); // Remove Bearer Prefix;
        }
        logger.warn("No Bearer token in header");
        return null;
    }

    // Generating Token from Username
    public String generateTokenFromUsername(UserDetails userDetails){
        // gets the username of the authenticated user
        String username = userDetails.getUsername();
        logger.info("Generating JWT for user: {}", username);

        // starts building the JWT token
        return Jwts.builder()
                // sets the subject of the token (usually username or email)
                .subject(username)
                // sets the date and time when the token was created
                .issuedAt(new Date())
                // sets the expiration date of the token
                // current time + jwtExpirationMs
                .expiration(new Date(new Date().getTime() +jwtExpirationMs))
                // signs the token using the secret key
                // prevents the token from being modified
                .signWith(key())
                // converts everything into the final JWT string
                .compact();
    }

    // Getting Username from JWT Token
    public String getUserNameFromJwtToken(String token){
        logger.info("Extracting username from JWT...");

        return Jwts.parser()

                // sets the secret key used to verify the token signature
                .verifyWith ((SecretKey)key())

                // builds the JWT parser
                .build()

                // parses the JWT token and validates it
                // checks if the signature is correct
                .parseSignedClaims(token)

                // gets the payload/body of the token
                .getPayload()

                // gets the subject from the payload
                // usually the username or email
                .getSubject();
    }
    // Generate Signing Key

    public Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }
    // Validate jwt token

    public boolean validateJwtToken(String authToken) {
        logger.info("Validating JWT token...");
        try {
            System.out.println("Validate");
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
