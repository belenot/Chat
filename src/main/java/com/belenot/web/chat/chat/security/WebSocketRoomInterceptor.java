package com.belenot.web.chat.chat.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.event.ClientSubscribedEventInfo;
import com.belenot.web.chat.chat.event.RoomEvent;
import com.belenot.web.chat.chat.event.RoomEventInfo;
import com.belenot.web.chat.chat.service.ParticipantService;
import com.belenot.web.chat.chat.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;

public class WebSocketRoomInterceptor implements ChannelInterceptor {

    @Autowired
    private RoomService roomService;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    // ^/topic/room/(?<roomId>\\d+)($|/.*$)
    private static final String subscribePathPattern = "^/topic/room/(?<roomId>\\d+)$";
    private static final String sendPathPattern = "^/app/room/(?<roomId>\\d+)/message/new$";

    @Override
    // Note: review throwed exception when defining errors subsystem
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        Room room = null;
        StompCommand command = StompHeaderAccessor.wrap(message).getCommand();
        if (command.equals(StompCommand.SUBSCRIBE)) {
            room = retrieveRoom(message, subscribePathPattern);
        } else if (command.equals(StompCommand.SEND)) {
            room = retrieveRoom(message, sendPathPattern);
        } else {
            return message;
        }
        if (room == null){
            //throw new MessagingException("Room doesn't exists");
            return null;
        }
        Client client = ((ClientDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        if (isClientAllowed(room, client)) {
            return message;
        }
        //throw new MessagingException("Client not allowed to this room");
        return null;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompCommand command = StompHeaderAccessor.wrap(message).getCommand();
        if (sent && command.equals(StompCommand.SUBSCRIBE)) {
            Room room = retrieveRoom(message, subscribePathPattern);
            Client client = ((ClientDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
            fireSubscriptionEvent(room, client);
        }
    }

    private Room retrieveRoom(Message<?> message, String pathPattern) {
        String destination = StompHeaderAccessor.wrap(message).getDestination();
        Pattern roomIdPattern = Pattern.compile(pathPattern);
        Matcher roomIdMatcher = roomIdPattern.matcher(destination);
        if (roomIdMatcher.find()) {
            try {
                int id = Integer.parseInt(roomIdMatcher.group("roomId"));
                return roomService.byId(id);
            } catch (NumberFormatException exc) {
                return null;
            }
        } else {
            return null;
        }
    }

    private void fireSubscriptionEvent(Room room, Client client) {
        RoomEventInfo roomEventInfo = new ClientSubscribedEventInfo(client.getId(), true);
        RoomEvent<RoomEventInfo> roomEvent = new RoomEvent<>(room.getId(), "ClientSubscribed", roomEventInfo);
        eventPublisher.publishEvent(roomEvent);
    }

    private boolean isClientAllowed(Room room, Client client) {
        Participant participant = participantService.byClientAndRoom(client, room);
        if (participant != null && !participant.isBanned() && !participant.isDeleted())
            return true;
        return false;
    }
}