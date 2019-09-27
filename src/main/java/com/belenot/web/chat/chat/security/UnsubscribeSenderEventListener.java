package com.belenot.web.chat.chat.security;

import com.belenot.web.chat.chat.event.ForceUnsubscribeEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

// Because of circular dependencies from webSocketSubscriptionHolder, sending responsibilities was moved to UnsubscribeSenderEventListener;
@Component
public class UnsubscribeSenderEventListener implements ApplicationListener<ForceUnsubscribeEvent> {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void onApplicationEvent(ForceUnsubscribeEvent event) {
        messagingTemplate.send(event.getSource());
    }

}