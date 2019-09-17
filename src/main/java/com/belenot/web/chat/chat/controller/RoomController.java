package com.belenot.web.chat.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;


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

    @PostMapping
    public Room create(@RequestBody Room room) {
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

    @PostMapping("/{roomId}/moderator/delete")
    public void delete(@PathVariable("roomId") Room room) {
        roomService.delete(room);
    }

    @GetMapping
    public List<Room> rooms() {
        return roomService.all();
    }

    @PostMapping("/{roomId}/join")
    public Participant join(@PathVariable("roomId") Room room) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        Participant participant = participantService.byClientAndRoom(client, room);
        if (participant == null) {
            participant = participantService.add(room, client);
        }
        return participant;
    }
    @PostMapping("/{roomId}/leave")
    public void leave(@PathVariable("roomId") Room room) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        Participant participant = participantService.byClientAndRoom(client, room);
        if (participant == null) {
            return;
        }
        participantService.delete(participant, client);
    }
    // Need secure: only room's moderators and admin are allowed
    @PostMapping("/{roomId}/moderator/ban/{clientId}")
    public Participant  ban(@PathVariable("roomId") Room room, @PathVariable("clientId") Client client) {
        Client moderatorClient = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        if (moderatorClient == null) throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        Moderator moderator = moderatorService.byClientAndRoom(moderatorClient, room);
        if (moderator == null) throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        Participant participant = participantService.byClientAndRoom(client, room);
        if (participant == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        participant.setBanned(true);
        return participantService.update(participant);
    }

    @GetMapping("/{roomId}/moderator/participant")
    public List<Client> participants(@PathVariable("roomId") Room room) {
        return clientService.byRoom(room);
    }

    @GetMapping("/search")
    public Room searchedRooms(@RequestParam("title") String title) {
        return roomService.byTitle(title);
    }

    @GetMapping("/{roomId}")
    public Map<String, Object> load(@PathVariable("roomId") Room room) {
        Map<String, Object> loaded = new HashMap<>();
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        Participant participant = participantService.byClientAndRoom(client, room);
        loaded.put("participant", participant);
        loaded.put("room", room);
        return loaded;
    }

    @GetMapping("/joined")
    public List<Room> joined() {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        return roomService.byClient(client);
    }

}