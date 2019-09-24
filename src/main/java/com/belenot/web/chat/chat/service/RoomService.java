package com.belenot.web.chat.chat.service;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Moderator;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.event.ClientBannedEventInfo;
import com.belenot.web.chat.chat.event.ClientJoinedEventInfo;
import com.belenot.web.chat.chat.event.ClientLeavedEventInfo;
import com.belenot.web.chat.chat.event.RoomDeletedEventInfo;
import com.belenot.web.chat.chat.event.RoomEvent;
import com.belenot.web.chat.chat.event.RoomEventInfo;
import com.belenot.web.chat.chat.model.ClientModel;
import com.belenot.web.chat.chat.repository.ClientRepository;
import com.belenot.web.chat.chat.repository.ModeratorRepository;
import com.belenot.web.chat.chat.repository.ParticipantRepository;
import com.belenot.web.chat.chat.repository.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ModeratorRepository moderatorRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private ModeratorService moderatorService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public Room add(Room room) {
        if (room.getPassword() == null || room.getPassword().length() == 0) {
            room.setPassword(null);
        }
        return roomRepository.save(room);
    }

    // Room title is not taken and not null
    // Client is persisted
    @Transactional
    public Room create(Client client, Room room) {
        client = clientRepository.findById(client.getId()).orElse(null);
        if (room.getPassword().length() == 0) {
            room.setPassword(null);
        }
        Moderator moderator = new Moderator();
        moderatorRepository.save(moderator);
        client.addModerator(moderator);
        // moderatorRepository.save(moderator);
        room.addModerator(moderator);
        roomRepository.save(room);
        moderatorRepository.save(moderator);
        return room;
    }

    public void delete(Room room) {
        int remainedRoomId = room.getId();
        roomRepository.delete(room);
        RoomEventInfo roomEventInfo = new RoomDeletedEventInfo(remainedRoomId);
        RoomEvent<RoomEventInfo> roomEvent = new RoomEvent<>(remainedRoomId, "RoomDeleted", roomEventInfo);
        eventPublisher.publishEvent(roomEvent);
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
    // Security: current client not joined
    public Participant join(Room room, Client client, String password) {
        Participant participant = participantService.byClientAndRoom(client, room);
        if (participant == null) {
            if (room.getPassword() == null || room.getPassword().equals(password)) {
                Participant newParticipant = participantService.add(room, client);
                RoomEventInfo roomEventInfo = new ClientJoinedEventInfo(new ClientModel(client));
                RoomEvent<RoomEventInfo> roomEvent = new RoomEvent<>(room.getId(), "ClientJoined", roomEventInfo);
                eventPublisher.publishEvent(roomEvent);
                return newParticipant;
            } else {
                // Throw JoinException("Wrong password")
                return null;
            }
        } else if (participant.isDeleted()) {
            participant.setDeleted(false);
            participantService.update(participant);
            RoomEventInfo roomEventInfo = new ClientJoinedEventInfo(new ClientModel(client));
            RoomEvent<RoomEventInfo> roomEvent = new RoomEvent<>(room.getId(), "ClientJoined", roomEventInfo);
            eventPublisher.publishEvent(roomEvent);
            return participant;
        } else {
            // Throw JoinException("Already joined")
            return null;
        }
    }
    // Security: current client is participant
    public Participant leave(Room room, Client client) {
        Participant participant = participantService.byClientAndRoom(client, room);
        if (participant == null) {
            // Throw LeaveException("Not joined")
            return null;
        } else if (participant.isDeleted()) {
            // Throw LeaveException("Already leaved")
            return null;
        } else {
            participant.setDeleted(true);
            RoomEventInfo roomEventInfo = new ClientLeavedEventInfo(client.getId());
            RoomEvent<RoomEventInfo> roomEvent = new RoomEvent<RoomEventInfo>(room.getId(), "ClientLeaved", roomEventInfo);
            eventPublisher.publishEvent(roomEvent);
            return participantService.update(participant);
        }
    }
    // Security: current client is moderator of room
    public Participant ban(Room room, Client client, Client deleter, boolean ban) {
        Participant participant = participantService.byClientAndRoom(client, room);
        if (participant == null) {
            /// Throw BanException("Client not participant")
            return null;
        }
        participant.setBanned(ban);
        participantService.update(participant);
        RoomEventInfo roomEventInfo = new ClientBannedEventInfo(client.getId(), ban);
        RoomEvent<RoomEventInfo> roomEvent = new RoomEvent<>(room.getId(), "ClientBanned", roomEventInfo);
        eventPublisher.publishEvent(roomEvent);
        return participant;
    }
}