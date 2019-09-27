package com.belenot.web.chat.chat.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;

//@Component
public class WebSocketSubscriptionHolder {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;    
    private Map<Integer, List<Subscription>> subscriptions = new HashMap<>();
    private class Subscription {
        public Subscription(String subscriptionId, String destination, String sessionId) {
            this.subscriptionId = subscriptionId;
            this.destination = destination;
            this.sessionId = sessionId;
        }
        public String subscriptionId;
        public String destination;
        public String sessionId;
    }

    public synchronized void hold(int clientId, String subscriptionId, String destination, String sessionId) {
        List<Subscription> clientSubscriptions = subscriptions.get(clientId);
        if (clientSubscriptions == null) {
            subscriptions.put(clientId, new ArrayList<>());
            clientSubscriptions = subscriptions.get(clientId);
        }
        clientSubscriptions.add(new Subscription(subscriptionId, destination, sessionId));
    }

    public synchronized void release(int clientId, String destination) {
        List<Subscription> clientSubscriptions = subscriptions.get(clientId);
        if (clientSubscriptions != null) {
            List<Integer> subscriptionsForRemoval = new ArrayList<>(clientSubscriptions.size());
            for (int i = 0; i < clientSubscriptions.size(); i++) {
                Subscription subscription = clientSubscriptions.get(i);
                if (!subscription.destination.equals(destination)) continue;
                StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.UNSUBSCRIBE);
                headers.setSubscriptionId(subscription.subscriptionId);
                headers.setSessionId(subscription.sessionId);
                headers.setDestination("/topic");
                Message<?> message = MessageBuilder.withPayload(new byte[0]).setHeaders(headers).build();
                messagingTemplate.send(message);
                subscriptionsForRemoval.add(i);
            }
            for (int removalSubscription : subscriptionsForRemoval) {
                clientSubscriptions.remove(removalSubscription);
            }
            if (clientSubscriptions.size() == 0) {
                subscriptions.remove(clientId);
            }
        }
    }
}