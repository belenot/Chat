package com.belenot.web.chat.chat.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.belenot.web.chat.chat.domain.ChatClient;
import com.belenot.web.chat.chat.domain.ChatMessage;
import com.belenot.web.chat.chat.service.ChatMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/message")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping
    public List<ChatMessage> getAll() {
        return chatMessageService.all();
    }

    @PostMapping
    public ChatMessage add(@RequestBody ChatMessage chatMessage, @SessionAttribute("client") ChatClient client) {
        chatMessage.setClient(client);
        chatMessage.setTime(LocalDateTime.now());
        return chatMessageService.add(chatMessage);
    }
    

}