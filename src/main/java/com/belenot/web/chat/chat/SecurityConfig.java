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
        http
            .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**", "/signup", "/about").permitAll()
                .antMatchers("/login", "/signup").anonymous()// WHA is ananamas?
                .antMatchers("/room/{roomId}/join")
                    .access("isAuthenticated() and not @participantAuthoritiesChecker.isJoined(#roomId, authentication)")
                .antMatchers("/room/{roomId}/clients", "/room/{roomId}/messages")
                    .access("isAuthenticated() and @participantAuthoritiesChecker.isJoined(#roomId, authentication) and not @participantAuthoritiesChecker.isBanned(#roomId, authentication)")
                .antMatchers("/room/{roomId}/moderator/**")
                    .access("isAuthenticated() and @participantAuthoritiesChecker.isModerator(#roomId, authentication)")
                .antMatchers("/chat/**", "/room/create", "/room/search", "/room/joined", "/room/moderated", "/client/**", "/room/{roomId}").authenticated()
                .antMatchers("/room/{roomId}/leave")
                    .access("isAuthenticated() and @participantAuthoritiesChecker.isJoined(#roomId, authentication)")
                .anyRequest().denyAll()
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
    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http.authorizeRequests()
    //             .antMatchers("/css/**", "/js/**", "/img/**", "/signup", "/about").permitAll()
    //             .antMatchers("/admin").hasRole("ADMIN")
    //             .anyRequest().authenticated()
    //         .and()
    //             .formLogin()
    //             .loginPage("/login").permitAll()
    //             .successForwardUrl("/chat")
    //             .defaultSuccessUrl("/chat")
    //         .and()
    //             .logout()
    //             .logoutUrl("/logout")
    //         .and() //SockJS relax fallback
    //             .headers()
    //             .frameOptions().sameOrigin()
    //         .and() //SockJS relax csrf endpoint
    //             .csrf()
    //             .ignoringAntMatchers("/chat/ws/**")
    //         .and();
    // }
    
    
}