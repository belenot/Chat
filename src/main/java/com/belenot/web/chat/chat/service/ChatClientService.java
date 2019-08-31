package com.belenot.web.chat.chat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.belenot.web.chat.chat.domain.ChatClient;
import com.belenot.web.chat.chat.repository.ChatClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Service
public class ChatClientService implements ApplicationListener<SessionConnectEvent> {
    private Map<String, ChatClient> wsClientList = new HashMap<>();
    @Autowired
    private ChatClientRepository chatClientRepository;

    public List<ChatClient> list() {
        return chatClientRepository.findAll();
    }

    public ChatClient add(ChatClient client) {
        return chatClientRepository.save(client);
    }

    public ChatClient byAccount(String login, String password) {
        ChatClient client = chatClientRepository.findByLogin(login);
        if (client != null && client.getPassword().equals(password)) {
            return client;
        }
        return null;
    }

    public List<ChatClient> byOnline(boolean online) {
        return chatClientRepository.findByOnline(online);
    }

    public ChatClient byId(int id) {
        return chatClientRepository.findById(id).get();
    }

    public void addWsClient(String key, ChatClient client) {
        wsClientList.put(key, client);
    }

    public ChatClient getWsClient(String key) {
        return wsClientList.get(key);
    }

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        MessageHeaderAccessor accessor = new MessageHeaderAccessor(event.getMessage());
        ChatClient client = (ChatClient)((Map<String, Object>)accessor.getHeader("simpSessionAttributes")).get("client");
        addWsClient((String)accessor.getHeader("simpSessionId"), client);
    }

    
}