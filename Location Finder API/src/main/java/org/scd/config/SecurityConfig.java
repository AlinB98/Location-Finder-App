package org.scd.config;

import org.scd.repository.UserRepository;
import org.scd.service.security.AuthenticationFilter;
import org.scd.service.security.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.scd.constants.SecurityConstants.ADMIN_ROLE;
import static org.scd.constants.SecurityConstants.BASIC_USER_ROLE;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(@Qualifier("customUserDetailsService") final UserDetailsService userDetailsService, final BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository=userRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.bCryptPasswordEncoder);
    }

    public AuthenticationFilter getAuthenticationFilter() throws Exception {
        final AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(),this.userRepository);
        authenticationFilter.setFilterProcessesUrl("/users/login");
        return authenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .cors()
                .and()
                .addFilter(getAuthenticationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager(), this.userRepository))
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users", "/users/login","/users/register").permitAll()
                .antMatchers(HttpMethod.GET, "/location").hasAnyRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.GET, "/location/{id}").authenticated()
                .antMatchers(HttpMethod.POST,"/location/current-location").authenticated()
                .antMatchers(HttpMethod.PUT,"/location/current-location/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/location/{id}").authenticated()
                .antMatchers("/users/me").authenticated()
                .antMatchers("/users").hasAnyRole(ADMIN_ROLE)
                .anyRequest()
                .authenticated();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        /* To allow Pre-flight [OPTIONS] request from browser */
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
