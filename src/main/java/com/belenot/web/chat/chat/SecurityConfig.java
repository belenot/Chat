package com.belenot.web.chat.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**", "/signup", "/about").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .successForwardUrl("/chat")
                .defaultSuccessUrl("/chat")
            .and()
                .logout()
                .logoutUrl("/logout")
            .and() //SockJS relax fallback
                .headers()
                .frameOptions().sameOrigin()
            .and() //SockJS relax csrf endpoint
                .csrf()
                .ignoringAntMatchers("/chat/ws/**")
            .and();
    }
    
}