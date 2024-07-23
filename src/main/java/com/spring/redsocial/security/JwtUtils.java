package com.spring.redsocial.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.spring.redsocial.exception.RedSocialExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${security.jwt.private.key}")
    private String privateKey;
    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    public String createToken(Authentication authentication){
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
        String email = authentication.getPrincipal().toString();
        String authorities = authentication.getAuthorities()
                .stream()
                .map( grantedAuthority -> grantedAuthority.getAuthority() )
                .collect(Collectors.joining(","));
        String jwt = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(email)
                .withClaim("authorities", authorities)
                .sign(algorithm);
        return jwt;
    }

    public DecodedJWT validateToken(String jwt){
        try{
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();
            return jwtVerifier.verify(jwt);
        }catch(Exception e){
            throw new RedSocialExceptionHandler("Token Invalido");
        }
    }

    public String getUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claim){
        return decodedJWT.getClaim(claim);
    }

}
