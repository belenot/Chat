package com.belenot.web.chat.chat.service;

import java.time.LocalDateTime;
import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.repository.ParticipantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public Participant add(Participant participant) {
        return participantRepository.save(participant);
    }

    public Participant add(Room room, Client client) {
        return participantRepository.save(new Participant(client, room));
    }

    public List<Participant> byClient(Client client) {
        return participantRepository.findByClient(client);
    }

    public List<Participant> byRoom(Room room) {
        return participantRepository.findByRoom(room);
    }    

    public Participant byClientAndRoom(Client client, Room room) {
        return participantRepository.findByClientAndRoom(client, room);
    }

    public void delete(Participant participant, Client deleter) {
        participant.setDeleted(true);
        participant.setDeleter(deleter);
        participant.setDeletedTime(LocalDateTime.now());
        participantRepository.save(participant);
    }

    public Participant update(Participant participant) {
        return participantRepository.save(participant);
    }

}