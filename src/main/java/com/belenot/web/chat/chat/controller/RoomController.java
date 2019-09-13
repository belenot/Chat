package com.belenot.web.chat.chat.controller;

import java.util.List;

import com.belenot.web.chat.chat.domain.Admin;
import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Moderator;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.security.ClientDetails;
import com.belenot.web.chat.chat.service.ClientService;
import com.belenot.web.chat.chat.service.ModeratorService;
import com.belenot.web.chat.chat.service.ParticipantService;
import com.belenot.web.chat.chat.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/room")
public class RoomController {    

    @Autowired
    private RoomService roomService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ModeratorService moderatorService;
    @Autowired
    private ParticipantService participantService;

    @PostMapping("/add")
    public Room add(@RequestBody Room room) {
        Client client = ((ClientDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        if (client != null && !(client instanceof Admin)) {
            roomService.add(room);
            Moderator moderator = new Moderator(client);
            room.addModerator(moderator);
            moderatorService.add(moderator);
            roomService.update(room);
        } else {
            roomService.add(room);
        }
        return room;
    }

    // Maybe it unneccessary?
    @PostMapping("/update")
    public Room update(@RequestBody Room room) {
        return roomService.update(room);
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable("id") Room room) {
        roomService.delete(room);
    }

    @GetMapping
    public List<Room> rooms() {
        return roomService.all();
    }

    @PostMapping("/{roomId}/participant/add/{clientId}")
    public Participant addParticipant(@PathVariable("roomId") Room room, @PathVariable("clientId") Client client) {
        Participant participant = participantService.byClientAndRoom(client, room);
        if (participant == null) {
            participant = participantService.add(room, client);
        }
        return participant;
    }
    // Need secure: only room's moderators and admin are allowed
    @PostMapping("/{roomId}/participant/delete/{clientId}")
    public void deleteParticipant(@PathVariable("roomId") Room room, @PathVariable("clientId") Client client) {
        Client deleter = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        Participant participant = participantService.byClientAndRoom(client, room);
        participantService.delete(participant, deleter);
    }

    @GetMapping("/{roomId}/participant")
    public List<Client> participants(@PathVariable("roomId") Room room) {
        return clientService.byRoom(room);
    }



}