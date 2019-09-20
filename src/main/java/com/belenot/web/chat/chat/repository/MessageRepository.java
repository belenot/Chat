package com.belenot.web.chat.chat.repository;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Message;
import com.belenot.web.chat.chat.domain.Room;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
    public List<Message> findByParticipantClient(Client client);
    public List<Message> findByParticipantRoom(Room room);
    public List<Message> findByParticipantRoomAndParticipantClient(Room room, Client client);

    public List<Message> findTop10ByParticipantRoomAndIdLessThanOrderByIdDesc(Room room, int messageId);
    public List<Message> findTop50ByParticipantRoomAndIdLessThanOrderByIdDesc(Room room, int messageId);
    public List<Message> findTop10ByParticipantRoomOrderByIdDesc(Room room);
    public List<Message> findTop50ByParticipantRoomOrderByIdDesc(Room room);

    public List<Message> findByParticipantRoom(Room room, Pageable pageable);
}