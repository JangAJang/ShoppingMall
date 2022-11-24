package com.studyProjectA.ShoppingMall.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFoundException;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import com.studyProjectA.ShoppingMall.auth.PrincipalDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
        printJWTHeader(jwtHeader);
        if(isJwtHeaderNotValidated(jwtHeader)){
            System.out.println("[ERROR] Jwt Filter did not work with JWT token");
            chain.doFilter(request, response);
            return;
        }
        String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build().verify(replaceWordOnToken(request)).getClaim("username").asString();
        if(username!=null){
            User tmpEntity = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
            PrincipalDetails principalDetails = new PrincipalDetails(tmpEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
    }

    private void printJWTHeader(String jwtHeader){
        System.out.println(jwtHeader);
    }

    private String replaceWordOnToken(HttpServletRequest request){
        return request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
    }

    private boolean isJwtHeaderNotValidated(String jwtHeader){
        return jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX);
    }
}
