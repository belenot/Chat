package com.belenot.web.chat.chat.service;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Message;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    
    public Message add(Message message) {
        if (message.getText().length() > 0)
            return messageRepository.save(message);
        return null;
    }

    public List<Message> byClient(Client client) {
        return messageRepository.findByClient(client);
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