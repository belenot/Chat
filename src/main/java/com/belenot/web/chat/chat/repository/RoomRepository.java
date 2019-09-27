package com.belenot.web.chat.chat.repository;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    public List<Room> findByParticipantsClient(Client client);
    public List<Room> findByParticipantsClientAndParticipantsDeleted(Client client, boolean deleted);
    public List<Room> findByModeratorsClient(Client client);
    public List<Room> findByTitleLike(String title);
    public Room findByTitle(String title);


}