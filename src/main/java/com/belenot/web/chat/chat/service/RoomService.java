package com.belenot.web.chat.chat.service;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.repository.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public Room add(Room room) {
        return roomRepository.save(room);
    }

    public Room byTitle(String title) {
        return roomRepository.findByTitle(title);
    }

    public List<Room> byClient(Client client) {
        return roomRepository.findByClients(client);
    }

    public Room update(Room room) {
        if (room.getId() <= 0) return null;
        return roomRepository.save(room);
    }

    public List<Room> all() {
        return roomRepository.findAll();
    }

    public void delete(Room room) {
        roomRepository.delete(room);
    }
}