package com.belenot.web.chat.chat.controller;

import com.belenot.web.chat.chat.domain.ChatClient;
import com.belenot.web.chat.chat.domain.ChatMessage;
import com.belenot.web.chat.chat.repository.ChatClientRepository;
import com.belenot.web.chat.chat.service.ChatClientService;
import com.belenot.web.chat.chat.service.ChatMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.socket.WebSocketSession;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private ChatClientService chatClientService;
    
    @GetMapping
    public String chatPage(@SessionAttribute("client") ChatClient client, Model model) {
        model.addAttribute("client", client);
        model.addAttribute("messages", chatMessageService.all());
        return "chat";
    }
    // Redo within separete class for ws
    @MessageMapping("/message")
    @SendTo("/topic/message")
    public ChatMessage addMessage(ChatMessage message, SimpMessageHeaderAccessor accessor) {
        ChatClient client = (ChatClient) accessor.getSessionAttributes().get("client");
        message.setClient(client);
        return chatMessageService.add(message);
    }
    @MessageMapping("/client")
    @SendTo("/topic/client")
    public ChatClient changeClientStatus(String online, SimpMessageHeaderAccessor accessor) {
        ChatClient client = (ChatClient) accessor.getSessionAttributes().get("client");
        if (!online.equals("offline") && !online.equals("online")) return client;
        client.setOnline(online.equals("online"));
        return chatClientService.add(client);
    }
}