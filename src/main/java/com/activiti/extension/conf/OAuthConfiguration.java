package com.activiti.extension.conf;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@EnableOAuth2Client
@Configuration
public class OAuthConfiguration {
    @Value("${clpservices.oauth2.client.client-id}")
    private String clientId;
    @Value("${clpservices.oauth2.client.client-secret}")
    private String clientSecret;
    @Value("${clpservices.oauth2.client.access-token-uri}")
    private String accessTokenUri;

    @Bean
    @Qualifier("oauthRestTemplate")
    public OAuth2RestTemplate oauthRestTemplate() {
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setAccessTokenUri(accessTokenUri);
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);

        AccessTokenRequest accessTokenRequest = new DefaultAccessTokenRequest();
        return new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext(accessTokenRequest));
    }
}
