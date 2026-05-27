package com.atifstudios.store.products;

import com.atifstudios.store.common.SecurityRules;
import com.atifstudios.store.users.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ProductSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.GET,"/products/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/products/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT,"/products/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE,"/products/**").hasRole(Role.ADMIN.name());
    }
}
