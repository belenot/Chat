package com.belenot.web.chat.chat.service;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Message;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.repository.MessageRepository;
import com.belenot.web.chat.chat.repository.ParticipantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//Implement
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    
    public Message add(Message message) {
        if (message.getText().length() > 0)
            return messageRepository.save(message);
        return null;
    }

    public Message add(@NotBlank String text, Client client, Room room) {
        Message message = new Message();
        Participant participant = participantRepository.findByClientAndRoom(client, room);
        message.setText(text);
        message.setParticipant(participant);
        return messageRepository.save(message);
    }

    public List<Message> byClient(Client client) {
        return messageRepository.findByParticipantClient(client);
    }

    public List<Message> byRoom(Room room) {
        return messageRepository.findByRoom(room);
    }

    public Message update(Message message) {
        if (message.getId() <= 0) return null;
        return messageRepository.save(message);
    }

    public List<Message> all() {
        return messageRepository.findAll();
    }

    public void delete(Message message) {
        messageRepository.delete(message);
    }
}