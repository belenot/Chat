package com.belenot.web.chat.chat.repository;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

    public List<Participant> findByClient(Client client);
    public List<Participant> findByRoom(Room room);
    public Participant findByClientAndRoom(Client client, Room room);
    public void deleteByRoom(Room room);

}