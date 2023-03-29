package com.springangularfacebook.security;
import com.springangularfacebook.security.facebookConfig.AccessToken;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TokenStore {

    private final Map< String, Authentication > cache = new HashMap<>();

    @Bean
    @RequestScope
    public AccessToken token(OAuth2AuthorizedClientService clientService ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String accessToken = null;
        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
            OAuth2AuthenticationToken oauthToken =
                    (OAuth2AuthenticationToken) authentication;
            String clientRegistrationId =
                    oauthToken.getAuthorizedClientRegistrationId();
            if (clientRegistrationId.equals("facebook")) {
                OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                        clientRegistrationId, oauthToken.getName());
                accessToken = client.getAccessToken().getTokenValue();
            }
        }
        AccessToken token = new AccessToken();
        token.setAccessToken(accessToken);
        return token;
    }

    public Authentication getAuth( String token ) {
        return cache.getOrDefault( token, null );
    }
}