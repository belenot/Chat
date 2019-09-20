package com.belenot.web.chat.chat.repository;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Moderator;
import com.belenot.web.chat.chat.domain.Room;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {
    public List<Moderator> findByClient(Client client);
    public List<Moderator> findByRoom(Room room);
    public Moderator findByClientAndRoom(Client client, Room room);
    public void deleteByRoom(Room room);
}