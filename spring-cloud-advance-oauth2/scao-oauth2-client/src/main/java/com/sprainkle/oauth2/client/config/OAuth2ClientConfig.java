package com.sprainkle.oauth2.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;

/**
 * @author sprainkle
 * @date 2022/1/29
 */
@Configuration
@EnableOAuth2Client
public class OAuth2ClientConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ResourceServerProperties resourceServerProperties;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/error**").permitAll()
                .anyRequest().authenticated()
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
                .and().addFilterBefore(oauth2ClientFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate(AuthorizationCodeResourceDetails githubClient,
                                                 OAuth2ClientContext oauth2ClientContext) {
        return new OAuth2RestTemplate(githubClient, oauth2ClientContext);
    }

    private Filter oauth2ClientFilter() {
        OAuth2ClientAuthenticationProcessingFilter oauth2ClientFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/github");
        oauth2ClientFilter.setRestTemplate(restTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(resourceServerProperties.getUserInfoUri(), resourceServerProperties.getClientId());
        tokenServices.setRestTemplate(restTemplate);
        oauth2ClientFilter.setTokenServices(tokenServices);
        return oauth2ClientFilter;
    }
}
