package com.ilyass.admin.filter;

import com.ilyass.admin.constant.SecurityConstant;
import com.ilyass.admin.utility.JWTTokenProvider;
import com.mysql.cj.x.protobuf.Mysqlx;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ilyass.admin.constant.SecurityConstant.TOKEN_PREFIX;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private JWTTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
             if (request.getMethod().equalsIgnoreCase(SecurityConstant.OPTION_HTTP_METHOD)){
                 response.setStatus(HttpStatus.OK.value());
             }
               else {
                   String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                   if(authorizationHeader == null || authorizationHeader.startsWith(TOKEN_PREFIX)){
                       filterChain.doFilter(request,response);
                       return;
                   }
             }
    }
}
