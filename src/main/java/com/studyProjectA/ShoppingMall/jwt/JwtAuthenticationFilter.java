package com.studyProjectA.ShoppingMall.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        try{
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            System.out.println("Principal Details : " + getAuthDetails(authenticationManager
                    .authenticate(getToken(user))));
            return authenticationManager.authenticate(getToken(user));
        }
        catch (IOException e){
            System.out.println("[ERROR] 인증 실패");
            throw new RuntimeException(e);
        }
    }

    private PrincipalDetails getAuthDetails(Authentication authentication){
        return (PrincipalDetails) authentication.getPrincipal();
    }

    private UsernamePasswordAuthenticationToken getToken(User user){
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){
        String jwtToken = JWT.create()
                .withSubject("입장토큰")
                .withExpiresAt(calculateExpirationDate())
                .withClaim("id", getIDFromAuth(authentication))
                .withClaim("username", getUserNameFromAuth(authentication))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
    }

    private Date calculateExpirationDate(){
        return new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME);
    }

    private Long getIDFromAuth(Authentication authentication){
        return getAuthDetails(authentication).getUser().getId();
    }

    private String getUserNameFromAuth(Authentication authentication){
        return getAuthDetails(authentication).getUser().getUsername();
    }
}
