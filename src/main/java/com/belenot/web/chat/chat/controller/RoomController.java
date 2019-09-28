package com.belenot.web.chat.chat.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Message;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.model.LoadedRoomModel;
import com.belenot.web.chat.chat.model.MessageModel;
import com.belenot.web.chat.chat.model.ParticipantClientModel;
import com.belenot.web.chat.chat.model.RoomModel;
import com.belenot.web.chat.chat.repository.support.OffsetPageable;
import com.belenot.web.chat.chat.security.ClientDetails;
import com.belenot.web.chat.chat.service.MessageService;
import com.belenot.web.chat.chat.service.ParticipantService;
import com.belenot.web.chat.chat.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/room")
@Validated
public class RoomController {    

    @Autowired
    private RoomService roomService;
    // @Autowired
    // private ClientService clientService;
    // @Autowired
    // private ModeratorService moderatorService;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private MessageService messageService;

    // Validation: Title not null, title not taken
    // Security: client
    @PostMapping("/create")
    public RoomModel create(@RequestBody @Validated RoomModel roomModel) {
        Room room = roomModel.createDomain();
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        roomModel = new RoomModel(roomService.create(client, room));
        return roomModel;
    }

    // Security: Client is moderator of this room
    @PostMapping("/{roomId}/moderator/delete")
    public void delete(@PathVariable("roomId") @NotNull Room room) {
        roomService.delete(room);
    }

    // Security: joined client
    @GetMapping("/{roomId}/clients")
    public List<ParticipantClientModel> getClients(@PathVariable("roomId") @NotNull Room room) {
        List<Participant> participants = participantService.byRoom(room);
        return ParticipantClientModel.of(participants);
    }

    // Security: client not joined
    @PostMapping("/{roomId}/join")
    public boolean join(@PathVariable(name = "roomId") @NotNull Room room, @RequestBody(required = false) String password) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        return roomService.join(room, client, password) != null;
    }

    // Security: client is joined
    @PostMapping("/{roomId}/leave")
    public void leave(@PathVariable("roomId") @NotNull Room room) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        roomService.leave(room, client);
    }

    // Security: active client is room's moderator
    // Validation: params not null
    @PostMapping("/{roomId}/moderator/ban/{clientId}")
    public void ban(@PathVariable("roomId") @NotNull Room room, @PathVariable("clientId") @NotNull Client client, @RequestParam(name="ban", defaultValue = "true") boolean ban){
        Client activeClient = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        roomService.ban(room, client, activeClient, ban);
    }

    // Security: any authenticated client
    // Validation: title not null and not empty
    @PostMapping("/search")
    public List<RoomModel> searchRooms(@RequestParam("title") @Validated @NotBlank(message = "specify title") String title) {
        List<Room> rooms = roomService.byTitleLike(title);
        return RoomModel.of(rooms);
    }

    // Security: client is joined
    @GetMapping("/{roomId}")
    public LoadedRoomModel load(@PathVariable("roomId") @NotNull Room room) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        Participant participant = participantService.byClientAndRoom(client, room);
        return new LoadedRoomModel(room, participant);
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
    @GetMapping("/{roomId}/messages")
    public List<MessageModel> messagePage(@PathVariable("roomId") @NotNull Room room, Pageable pageable, @RequestParam("offset") @PositiveOrZero long offset) {
        List<Message> messages = messageService.page(room, OffsetPageable.of(pageable, offset));
        return MessageModel.of(messages);
    }
}