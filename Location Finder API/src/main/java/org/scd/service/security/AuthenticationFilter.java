package org.scd.service.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.scd.model.User;
import org.scd.model.dto.UserLoginDTO;
import org.scd.model.security.CustomUserDetails;
import org.scd.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.scd.constants.SecurityConstants.EXPIRATION_TIME;
import static org.scd.constants.SecurityConstants.KEY;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManager;
    private UserRepository userRepository;
    private Gson gson = new Gson();

    public AuthenticationFilter( AuthenticationManager authManager, UserRepository userRepository){
        this.authManager = authManager;
        this.userRepository=userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {

        try {
            UserLoginDTO userLoginDTO = new ObjectMapper().readValue(req.getInputStream(), UserLoginDTO.class);
            return authManager.authenticate( new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword(), new ArrayList<>()));

        } catch ( IOException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {

        CustomUserDetails principals =(CustomUserDetails) auth.getPrincipal();
        String currentUser = this.gson.toJson(principals.getUser());
        String token = JWT.create()
                .withSubject(principals.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(KEY.getBytes()));

        res.addHeader("Access-Control-Expose-Headers", "Authentication");
        res.addHeader("Authentication", token);
        res.getWriter().print(currentUser);


    }

}
