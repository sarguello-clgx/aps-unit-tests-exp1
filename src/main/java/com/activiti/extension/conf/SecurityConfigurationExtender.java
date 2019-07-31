package com.activiti.extension.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(-3)
public class SecurityConfigurationExtender extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {

        String apiPath = "/api";

        // @formatter:off
        http.csrf()
                .disable()
                .antMatcher(apiPath + "/**")
                .authorizeRequests()
                .antMatchers(apiPath + "/enterprise/**").access("@ActivitiOauth2RequestHeaderService.checkForTokenHeader(request)")
                .antMatchers(apiPath + "/runtime/**").access("@ActivitiOauth2RequestHeaderService.checkForTokenHeader(request)")
                .antMatchers(apiPath + "/query/**").access("@ActivitiOauth2RequestHeaderService.checkForTokenHeader(request)")
                .antMatchers(apiPath + "/**").access("@restAuthorizationService.hasPermission(request)")
                .and()
                .httpBasic();
        // @formatter:on
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Being used for health check, so disable auth
        web.ignoring().antMatchers("/api/enterprise/app-version");
        web.ignoring().antMatchers("/api/api-explorer.html");
        web.ignoring().antMatchers("/api/swagger-resources/**");
    }
}
