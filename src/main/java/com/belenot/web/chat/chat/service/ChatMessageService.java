package com.belenot.web.chat.chat.service;

import java.util.List;

import com.belenot.web.chat.chat.domain.ChatClient;
import com.belenot.web.chat.chat.domain.ChatMessage;
import com.belenot.web.chat.chat.repository.ChatMessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    public ChatMessage add(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> byClient(ChatClient chatClient) {
        return chatMessageRepository.findByClient(chatClient);
    }

    public List<ChatMessage> all() {
        return chatMessageRepository.findAll();
    }

}