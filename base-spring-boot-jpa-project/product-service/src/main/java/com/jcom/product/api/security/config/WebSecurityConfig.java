package com.jcom.product.api.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ms.core.common.security.CommonAuthenticationEntryPoint;
import com.ms.core.common.security.CommonAuthenticationTokenFilter;
import com.ms.core.common.security.Permission;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CommonAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public CommonAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new CommonAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.GET, "/municipios/**").permitAll()
                .antMatchers(HttpMethod.GET, "/location/**").permitAll()
                .antMatchers(HttpMethod.GET, "/geocode/**").permitAll()
                .antMatchers(HttpMethod.GET, "/reverse/**").permitAll()
                .antMatchers(HttpMethod.GET, "/products/**").permitAll()
                .antMatchers(HttpMethod.POST, "/products/**").hasRole(Permission.USER_SELLER)
				.antMatchers(HttpMethod.PUT, "/products/**").hasRole(Permission.USER_SELLER)
                
				.antMatchers("/actuator/**", "/v2/api-docs", "/configuration/**").permitAll()
				
                //authenticated requests
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }
    
}
