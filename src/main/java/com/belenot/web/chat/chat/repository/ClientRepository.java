package com.belenot.web.chat.chat.repository;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>{
    public Client findByLogin(String login);
    public List<Client> findByParticipantsRoom(Room room);
    public List<Client> findByParticipantsRoomAndParticipantsDeleted(Room room, boolean deleted);
    public List<Client> findByParticipantsRoomAndParticipantsBanned(Room room, boolean banned);
    public List<Client> findByModeratorsRoom(Room room);

}