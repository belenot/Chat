package com.belenot.web.chat.chat.service;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Moderator;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.repository.ModeratorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModeratorService {

    @Autowired
    private ModeratorRepository moderatorRepository;

    public Moderator add(Moderator moderator) {
        return moderatorRepository.save(moderator);
    }

    public Moderator add(Client client, Room room) {
        Moderator moderator = new Moderator(client);
        room.addModerator(moderator);
        return add(moderator);
    }

    public void delete(Moderator moderator) {
        moderatorRepository.delete(moderator);
    }

    public void deleteByRoom(Room room) {
        moderatorRepository.deleteByRoom(room);
        // room.clearModerators()?
    }

    public List<Moderator> byClient(Client client) {
        return moderatorRepository.findByClient(client);
    }

    public List<Moderator> byRoom(Room room) {
        return moderatorRepository.findByRoom(room);
    }

    public Moderator byClientAndRoom(Client client, Room room) {
        return moderatorRepository.findByClientAndRoom(client, room);
    }

}