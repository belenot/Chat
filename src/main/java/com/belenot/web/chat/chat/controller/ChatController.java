package com.belenot.web.chat.chat.controller;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Message;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.event.MessageCreatedEvent;
import com.belenot.web.chat.chat.event.RoomEvent;
import com.belenot.web.chat.chat.event.RoomEventInfo;
import com.belenot.web.chat.chat.model.MessageModel;
import com.belenot.web.chat.chat.model.RoomEventModel;
import com.belenot.web.chat.chat.security.ClientDetails;
import com.belenot.web.chat.chat.service.ClientService;
import com.belenot.web.chat.chat.service.MessageService;
import com.belenot.web.chat.chat.service.ParticipantService;
import com.belenot.web.chat.chat.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

@Controller
@MessageMapping("/chat")
public class ChatController implements ApplicationListener<RoomEvent<? extends RoomEventInfo>> {
    @Autowired
    private MessageService messageService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private WebSocketMessageBrokerStats wsmbs;
    @Autowired
    private SimpMessagingTemplate smt;

    // Security: Client is joined
    // Validation: text is not null and not empty
    @MessageMapping("/room/{roomId}/message/new")
    public void send(@Payload String text, @DestinationVariable("roomId") int roomId) {
        Room room = roomService.byId(roomId);
        if (room == null)
            throw new MessagingException("Room doesn't exists");
        Client client = ((ClientDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getClient();
        Participant participant = participantService.byClientAndRoom(client, room);
        if (participant == null || participant.isDeleted() || participant.isBanned()) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }
        messageService.add(text, client, room);
    }

    // @Override
    // public void onApplicationEvent(MessageCreatedEvent event) {
    //     Message message = ((Message) event.getSource());
    //     Room room = message.getParticipant().getRoom();
    //     MessageModel messageModel = new MessageModel(message);
    //     smt.convertAndSend("/topic/chat/room/" + room.getId() + "/message", messageModel);
    // }
    
    @Override
    public void onApplicationEvent(RoomEvent<?> event) {
        String description = event.getDescription();
        int roomId = event.getRoomId();
        RoomEventInfo info = event.getSource();
        RoomEventModel eventModel = new RoomEventModel(info, description);
        smt.convertAndSend("/topic/chat/room/" + roomId, eventModel);
    }
    

}