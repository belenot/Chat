package com.belenot.web.chat.chat.service;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Moderator;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.repository.ModeratorRepository;
import com.belenot.web.chat.chat.repository.ParticipantRepository;
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
    @Autowired
    private ParticipantRepository participantRepository;

    public Room add(Room room) {
        if (room.getPassword() == null || room.getPassword().length() == 0) {
            room.setPassword(null);
        }
        return roomRepository.save(room);
    }

    // Room title is not taken and not null
    // Client is persisted
    public Room create(Client client, Room room) {
        if (room.getPassword().length() == 0) {
            room.setPassword(null);
        }
        Moderator moderator = new Moderator();
        client.addModerator(moderator);
        moderatorRepository.save(moderator);
        room.addModerator(moderator);
        roomRepository.save(room);
        moderatorRepository.save(moderator);
        return room;
    }

    // Need query optimization(batching), need jpa query
    // Room is persisted
    public void delete(Room room) {
        roomRepository.delete(room);
    }

    public Room byTitle(String title) {
        return roomRepository.findByTitle(title);
    }
    public List<Room> byTitleLike(String title) {
        return roomRepository.findByTitleLike(title);
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

    public List<Room> joinedByClient(Client client) {
        return roomRepository.findByParticipantsClientAndParticipantsDeleted(client, false);
    }
    public List<Room> moderatedByClient(Client client) {
        return roomRepository.findByModeratorsClient(client);
    }
}