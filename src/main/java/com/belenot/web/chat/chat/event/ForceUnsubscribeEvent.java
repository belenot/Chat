package com.belenot.web.chat.chat.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.messaging.Message;

public class ForceUnsubscribeEvent extends ApplicationEvent {
    public ForceUnsubscribeEvent(Message<?> message) {
        super(message);
    }

    @Override
    public Message<?> getSource() {
        return (Message<?>)super.getSource();
    }
    
}