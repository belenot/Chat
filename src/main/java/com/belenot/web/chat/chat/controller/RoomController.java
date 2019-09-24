package com.belenot.web.chat.chat.controller;

import java.util.List;
import java.util.Map;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Moderator;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.domain.support.wrap.MessageWrapper;
import com.belenot.web.chat.chat.domain.support.wrap.RoomWrapper;
import com.belenot.web.chat.chat.filter.RequestContextFilter.ContextParam;
import com.belenot.web.chat.chat.repository.support.OffsetPageable;
import com.belenot.web.chat.chat.security.ClientDetails;
import com.belenot.web.chat.chat.service.ClientService;
import com.belenot.web.chat.chat.service.MessageService;
import com.belenot.web.chat.chat.service.ModeratorService;
import com.belenot.web.chat.chat.service.ParticipantService;
import com.belenot.web.chat.chat.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageWrapper messageWrapper;
    @Autowired
    private RoomWrapper roomWrapper;

    // Validation: Title not null, title not taken
    // Security: client
    @PostMapping
    public Room create(@RequestBody Room room) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        roomService.add(room);
        Moderator moderator = new Moderator(client);
        room.addModerator(moderator);
        moderatorService.add(moderator);
        roomService.update(room);
        return room;
    }

    // Security: Client is moderator of this room
    @PostMapping("/{roomId}/moderator/delete")
    public void delete(@PathVariable("roomId") Room room) {
        roomService.delete(room);
    }

    // Security: joined client
    @GetMapping("/{roomId}/clients")
    public List<Participant> getClients(@PathVariable("roomId") Room room) {
        return participantService.byRoom(room);
    }

    // Security: client not joined
    @PostMapping("/{roomId}/join")
    public boolean join(@PathVariable("roomId") Room room, @RequestBody(required = false) String password) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        Participant participant = participantService.byClientAndRoom(client, room);
        if (room.getPassword() == null || room.getPassword().equals(password))
            participant = participantService.add(room, client);
        return participant!=null;
    }

    // Security: client is joined
    @PostMapping("/{roomId}/leave")
    public void leave(@PathVariable("roomId") Room room) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        Participant participant = participantService.byClientAndRoom(client, room);
        if (participant == null) {
            return;
        }
        participantService.delete(participant, client);
    }

    // Security: active client is room's moderator
    // Validation: params not null
    @PostMapping("/{roomId}/moderator/ban/{clientId}")
    public void ban(@PathVariable("roomId") Room room, @PathVariable("clientId") Client client) {
        Client activeClient = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        Moderator moderator = moderatorService.byClientAndRoom(activeClient, room);
        if (moderator == null) throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        Participant participant = participantService.byClientAndRoom(client, room);
        if (participant == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        participant.setBanned(true);
        participantService.update(participant);
    }

    // Security: any authenticated client
    // Validation: title not null and not empty
    @PostMapping("/search")
    public List<Room> searchRooms(@RequestParam("title") String title) {
        return roomService.byTitleLike(title);
    }

    // Security: client is joined
    @GetMapping("/{roomId}")
    public Room load(@PathVariable("roomId") Room room) {
        return room;
    }

    // Security: any authenticated client 
    @GetMapping("/joined")
    public List<Room> joined() {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        return roomService.joinedByClient(client);
    }
    // Security: any authenticated client
    @GetMapping("/moderated")
    public List<Room> moderated() {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        return roomService.moderatedByClient(client);
    }

    // Security: client is joined
    @GetMapping("/{roomId}/message/page")
    public List<Map<String, Object>> messagePage(@PathVariable("roomId") Room room, Pageable pageable, @RequestParam("offset") long offset) {
        return messageWrapper.wrapUp(messageService.page(room, OffsetPageable.of(pageable, offset)));
    }

}