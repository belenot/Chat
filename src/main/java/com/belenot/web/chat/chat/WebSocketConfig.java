package com.belenot.web.chat.chat;

import com.belenot.web.chat.chat.security.WebSocketRoomInterceptor;
import com.belenot.web.chat.chat.security.WebSocketSubscriptionHolder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic");
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                // .simpDestMatchers("/app/room/**").hasRole("USER")
                // .simpSubscribeDestMatchers("/topic/room/**").hasRole("USER");
                // .simpDestMatchers("patterns").access("review ws security with this feature");
                .nullDestMatcher().authenticated()
                .simpSubscribeDestMatchers("/topic/room/{roomId}").access("isAuthenticated() and @participantAuthoritiesChecker.isJoined(message, authentication) and not @participantAuthoritiesChecker.isBanned(message, authentication)")
                .simpDestMatchers("/app/room/{roomId}/**").access("isAuthenticated() and @participantAuthoritiesChecker.isJoined(message, authentication) and not @participantAuthoritiesChecker.isBanned(message, authentication)");
                
        
    }

    @Override
    protected void customizeClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketRoomInterceptor());
    }

    @Bean
    public WebSocketRoomInterceptor webSocketRoomInterceptor() {
        return new WebSocketRoomInterceptor();
    }
    @Bean
    public WebSocketSubscriptionHolder webSocketSubscriptionHolder() {
        return new WebSocketSubscriptionHolder();
    }


    

    
    
}