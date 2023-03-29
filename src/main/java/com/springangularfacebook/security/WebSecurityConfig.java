package com.springangularfacebook.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springangularfacebook.security.facebookConfig.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Configuration
public class WebSecurityConfig {

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//    @Autowired
//    private TokenStore tokenStore;
    @Autowired
    private AccessToken token;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable().cors().and()
                .requiresChannel(channel ->
                        channel.anyRequest().requiresSecure())
                .authorizeRequests()
                .antMatchers( "/h2-console/**").permitAll()
                .antMatchers("/oauth2/**").permitAll()
               // .antMatchers("/test").permitAll()
                // all other requests need to be authenticated
                .anyRequest().authenticated().and()
                .oauth2Login()
                .authorizationEndpoint()
                .authorizationRequestRepository( new InMemoryRequestRepository() )
                .and()
                .successHandler( this::successHandler )
                .and()
                .exceptionHandling()
                .authenticationEntryPoint( this::authenticationEntryPoint )
                .and()
                //pentru a putea acesa h2-console
                .headers()
                .frameOptions()
                .disable();
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods( Collections.singletonList( "*" ) );
        config.setAllowedOrigins( Collections.singletonList( "*" ) );
        config.setAllowedHeaders( Collections.singletonList( "*" ) );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", config );
        return source;
    }
    private void successHandler(HttpServletRequest request,
                                HttpServletResponse response, Authentication authentication ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(token.getAccessToken());
        System.out.println("uuid= " + token.getUuid());
        response.getWriter().write(mapper.writeValueAsString( Collections.singletonMap( "accessToken", token.getAccessToken()) ));
    }
    private void authenticationEntryPoint( HttpServletRequest request, HttpServletResponse response,
                                           AuthenticationException authException) throws IOException {
        String uuid = request.getParameter("uuid");
        if(uuid != null){
            response.setStatus(HttpServletResponse.SC_OK);
            System.out.println("uuid= " + uuid);
        }else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("error: Unauthenticated");
        }
    }
}
