package com.belenot.web.chat.chat.service;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.repository.ClientRepository;
import com.belenot.web.chat.chat.repository.ModeratorRepository;
import com.belenot.web.chat.chat.repository.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ModeratorRepository moderatorRepository;

    public Room add(Room room) {
        return roomRepository.save(room);
    }

    public Room byTitle(String title) {
        return roomRepository.findByTitle(title);
    }

    public List<Room> byClient(Client client) {
        return roomRepository.findByParticipantsClient(client);
    }

    public Room update(Room room) {
        if (room.getId() <= 0) return null;
        return roomRepository.save(room);
    }

    public List<Room> all() {
        return roomRepository.findAll();
    }

    public Room byId(int id) {
        return roomRepository.findById(id).orElse(null);
    }
    // Need query optimization(batching)
    @Transactional
    public void delete(Room room) {
        moderatorRepository.deleteByRoom(room);
        roomRepository.delete(room);

    }
}