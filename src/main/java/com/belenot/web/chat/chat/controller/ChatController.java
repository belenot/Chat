package com.belenot.web.chat.chat.controller;

import com.belenot.web.chat.chat.domain.ChatClient;
import com.belenot.web.chat.chat.service.ChatMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService;
    
    @GetMapping
    public String chatPage(@SessionAttribute("client") ChatClient client, Model model) {
        model.addAttribute("client", client);
        model.addAttribute("messages", chatMessageService.all());
        return "chat";
    }
}