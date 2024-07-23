package com.spring.redsocial.security;


import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;


import java.io.IOException;
import java.util.Collection;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtTokenFilter(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = getTokenFromRequest(request);
        if( jwt != null ){
            DecodedJWT decodedJWT = jwtUtils.validateToken(jwt);
            String username = jwtUtils.getUsername(decodedJWT);
            String authoritiesString = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();
            Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                    .commaSeparatedStringToAuthorityList(authoritiesString);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username, null, authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader( HttpHeaders.AUTHORIZATION );
        if( bearerToken == null) return null;
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer") ){
            return bearerToken.substring(7);
        }
        return bearerToken;
    }
}
