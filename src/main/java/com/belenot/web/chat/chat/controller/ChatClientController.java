package com.belenot.web.chat.chat.controller;

import java.util.List;

import com.belenot.web.chat.chat.domain.ChatClient;
import com.belenot.web.chat.chat.service.ChatClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ChatClientController {

    @Autowired
    private ChatClientService chatClientService;

    @GetMapping
    public List<ChatClient> chatClientList() {
        return chatClientService.list();
    }

    @GetMapping("/online")
    public List<ChatClient> chatClientByOnline() {
        return chatClientService.byOnline(true);
    }
}