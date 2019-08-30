package com.belenot.web.chat.chat.service;

import java.util.List;

import com.belenot.web.chat.chat.domain.ChatClient;
import com.belenot.web.chat.chat.repository.ChatClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatClientService {
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
}