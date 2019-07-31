package com.activiti.extension.bean;

import com.activiti.domain.idm.EndpointConfiguration;
import com.activiti.service.exception.BadRequestException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class OAuthService {

    @Value("${mus.hostname}")
    private String backendServiceBaseUrlValue;

    @Autowired
    @Qualifier("oauthRestTemplate")
    private OAuth2RestTemplate restTemplate;

    private URL backendServiceBaseUrl;

    public OAuthService() {
    }

    @PostConstruct
    public void init() throws MalformedURLException {
        backendServiceBaseUrl = new URL(backendServiceBaseUrlValue);
    }

    boolean isEndpointNeedToken(String url) {
        try {
            URL targetUrl = new URL(url);
            boolean needToken = backendServiceBaseUrl.getHost().equals(targetUrl.getHost());
            log.info(url + " needs token : " + needToken);
            return needToken;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    boolean isEndpointNeedToken(EndpointConfiguration endpointConfiguration) {
        return isEndpointNeedToken(endpointConfiguration.getUrl());
    }

    public String getOAuthToken() {
        try {
             OAuth2AccessToken accessToken = restTemplate.getAccessToken();
             return accessToken.getValue();
        } catch (Throwable e) {
            log.error("error retrieving token", e);
            throw new BadRequestException(e.getMessage());
        }
    }

}
