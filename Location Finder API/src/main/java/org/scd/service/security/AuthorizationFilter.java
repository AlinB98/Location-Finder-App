package org.scd.service.security;

import com.auth0.jwt.JWT;
import org.scd.model.User;
import org.scd.model.security.CustomUserDetails;
import org.scd.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.scd.constants.SecurityConstants.KEY;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public AuthorizationFilter(AuthenticationManager authManager, UserRepository userRepository){
        super(authManager);
        this.userRepository=userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {

        String header = req.getHeader("Authorization");
        if(header == null){
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken auth = authenticate(req);
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(req,res);

    }


    private UsernamePasswordAuthenticationToken authenticate(HttpServletRequest req) {
        String token = req.getHeader("Authorization").replace("Bearer ","");
        if (token != null) {
            String userName = JWT.require(HMAC512(KEY.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
            if (userName != null) {
                CustomUserDetails userDetails = new CustomUserDetails(this.userRepository.findByEmail(userName));
                return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities() );
            }else{
                return  null;
            }

        }
        return null;
    }

}
