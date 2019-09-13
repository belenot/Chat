package com.belenot.web.chat.chat.controller;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Message;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.security.ClientDetails;
import com.belenot.web.chat.chat.service.ClientService;
import com.belenot.web.chat.chat.service.MessageService;
import com.belenot.web.chat.chat.service.ParticipantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;

@Controller
public class ChatController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ParticipantService participantService;
    
    @MessageMapping("/message/{roomId}")
    public Message send(String text, @PathVariable("roomId") Room room) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        Participant participant = participantService.byClientAndRoom(client, room);
        if (participant == null) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }
        return messageService.add(text, client, room);
    }
}