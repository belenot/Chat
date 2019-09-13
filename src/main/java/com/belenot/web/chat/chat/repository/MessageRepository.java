package com.belenot.web.chat.chat.repository;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Message;
import com.belenot.web.chat.chat.domain.Room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
    public List<Message> findByParticipantClient(Client client);
    public List<Message> findByRoom(Room room);
    public List<Message> findByRoomAndParticipantClient(Room room, Client client);
}