package com.belenot.web.chat.chat.event;

import com.belenot.web.chat.chat.security.WebSocketSubscriptionHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class SessionUnsubscribeRoomEventListener implements ApplicationListener<SessionUnsubscribeEvent> {
    @Autowired
    private WebSocketSubscriptionHolder subscriptionHolder;

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {
        Message<?> message = event.getMessage();
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
        String subscriptionId = headers.getSubscriptionId();
        subscriptionHolder.unhold(subscriptionId);
    }
    
}