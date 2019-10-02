package com.belenot.web.chat.chat.service;

import java.time.LocalDateTime;
import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.event.ClientDeletedEventInfo;
import com.belenot.web.chat.chat.event.ClientUpdatedEventInfo;
import com.belenot.web.chat.chat.event.RoomEvent;
import com.belenot.web.chat.chat.event.RoomEventInfo;
import com.belenot.web.chat.chat.model.ClientModel;
import com.belenot.web.chat.chat.repository.ClientRepository;
import com.belenot.web.chat.chat.repository.RoomRepository;
import com.belenot.web.chat.chat.security.ClientDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientService implements UserDetailsService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public Client add(Client client) {
        return clientRepository.save(client);
    }

    public Client byLogin(String login) {
        return clientRepository.findByLogin(login);
    }

    public List<Client> byRoom(Room room) {
        return clientRepository.findByParticipantsRoomAndParticipantsDeleted(room, false);
    }

    public List<Client> all() {
        return clientRepository.findAll();
    }

    public Client update(Client client) {
        client = clientRepository.save(client);
        RoomEventInfo roomEventInfo = new ClientUpdatedEventInfo(new ClientModel(client));
        List<Room> joinedRooms = roomRepository.findByParticipantsClient(client);
        for (Room room : joinedRooms) {
            RoomEvent<RoomEventInfo> roomEvent = new RoomEvent<>(room.getId(), "ClientUpdated", roomEventInfo);
            eventPublisher.publishEvent(roomEvent);
        }
        return client;
    }

    // Security: admin or client itself
    public void delete(Client client) {
        client.setDeleted(true);
        client.setDeletedTime(LocalDateTime.now());
        client.setDeleter(((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient());
        clientRepository.save(client);
        RoomEventInfo roomEventInfo = new ClientDeletedEventInfo(client.getId());
        List<Room> joinedRooms = roomRepository.findByParticipantsClient(client);
        for (Room room : joinedRooms) {
            RoomEvent<RoomEventInfo> roomEvent = new RoomEvent<>(room.getId(), "ClientDeleted", roomEventInfo);
            eventPublisher.publishEvent(roomEvent);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Client client = clientRepository.findByLogin(login);
        if (client == null) return null;
        ClientDetails clientDetails = new ClientDetails(client);
        return clientDetails;
    }

    
}