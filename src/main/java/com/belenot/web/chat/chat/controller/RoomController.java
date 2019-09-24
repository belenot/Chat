package com.belenot.web.chat.chat.controller;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Message;
import com.belenot.web.chat.chat.domain.Moderator;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.model.MessageModel;
import com.belenot.web.chat.chat.model.ParticipantClientModel;
import com.belenot.web.chat.chat.model.RoomModel;
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

    // Validation: Title not null, title not taken
    // Security: client
    // Note: possibly move to service layer? ...
    @PostMapping
    public RoomModel create(@RequestBody RoomModel roomModel) {
        Room room = roomModel.createDomain();
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        // roomService.add(room);
        // Moderator moderator = new Moderator(client);
        // room.addModerator(moderator);
        // moderatorService.add(moderator);
        // roomService.update(room);
        // roomModel = new RoomModel(room);
        roomModel = new RoomModel(roomService.create(client, room));
        return roomModel;
    }

    // Security: Client is moderator of this room
    @PostMapping("/{roomId}/moderator/delete")
    public void delete(@PathVariable("roomId") Room room) {
        roomService.delete(room);
    }

    // Security: joined client
    @GetMapping("/{roomId}/clients")
    public List<ParticipantClientModel> getClients(@PathVariable("roomId") Room room) {
        List<Participant> participants = participantService.byRoom(room);
        return ParticipantClientModel.of(participants);
    }

    // Security: client not joined
    // Note: possibly move to service layer? ...
    @PostMapping("/{roomId}/join")
    public boolean join(@PathVariable("roomId") Room room, @RequestBody(required = false) String password) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        // Participant participant = participantService.byClientAndRoom(client, room);
        // if (room.getPassword() == null || room.getPassword().equals(password))
        //     participant = participantService.add(room, client);
        // return participant!=null;// In future return will be void
        return roomService.join(room, client, password) != null;
    }

    // Security: client is joined
    // Note: possibly move to service layer? ...
    @PostMapping("/{roomId}/leave")
    public void leave(@PathVariable("roomId") Room room) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        // Participant participant = participantService.byClientAndRoom(client, room);
        // if (participant == null) {
        //     return;
        // }
        // participantService.delete(participant, client);
        roomService.leave(room, client);
    }

    // Security: active client is room's moderator
    // Validation: params not null
    // Note: possibly move to service layer? ...
    @PostMapping("/{roomId}/moderator/ban/{clientId}")
    public void ban(@PathVariable("roomId") Room room, @PathVariable("clientId") Client client, @RequestParam(name="ban", defaultValue = "true") boolean ban){
        Client activeClient = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        // Moderator moderator = moderatorService.byClientAndRoom(activeClient, room);
        // if (moderator == null) throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        // Participant participant = participantService.byClientAndRoom(client, room);
        // if (participant == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        // participant.setBanned(true);
        // participantService.update(participant);
        roomService.ban(room, client, activeClient, ban);
    }

    // Security: any authenticated client
    // Validation: title not null and not empty
    @PostMapping("/search")
    public List<RoomModel> searchRooms(@RequestParam("title") String title) {
        List<Room> rooms = roomService.byTitleLike(title);
        return RoomModel.of(rooms);
    }

    // Security: client is joined
    @GetMapping("/{roomId}")
    public RoomModel load(@PathVariable("roomId") Room room) {
        return new RoomModel(room);
    }

    // Security: any authenticated client 
    @GetMapping("/joined")
    public List<RoomModel> joined() {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        List<Room> rooms = roomService.joinedByClient(client);
        return RoomModel.of(rooms);
    }
    // Security: any authenticated client
    @GetMapping("/moderated")
    public List<RoomModel> moderated() {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        List<Room> rooms = roomService.moderatedByClient(client);
        return RoomModel.of(rooms);
    }

    // Security: client is joined
    @GetMapping("/{roomId}/message/page")
    public List<MessageModel> messagePage(@PathVariable("roomId") Room room, Pageable pageable, @RequestParam("offset") long offset) {
        List<Message> messages = messageService.page(room, OffsetPageable.of(pageable, offset));
        return MessageModel.of(messages);
    }

}