package com.belenot.web.chat.chat.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.service.ParticipantService;
import com.belenot.web.chat.chat.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;

public class WebSocketRoomSubscribeInterceptor implements ChannelInterceptor {

    @Autowired
    private RoomService roomService;
    @Autowired
    private ParticipantService participantService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // If not subscription
        if (!StompHeaderAccessor.wrap(message).getCommand().equals(StompCommand.SUBSCRIBE)){
            return message;
        }
        Room room = retrieveRoom(message);
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        // If wrong room id
        if (room == null) throw new MessagingException(message, "Room doesn't exists");
        Participant participant = participantService.byClientAndRoom(client, room);
        // If client isn't participate in this room
        if (participant == null) throw new MessagingException(message, "Client not allowed to this room");
        return message;
    }

    private Room retrieveRoom(Message<?> message) {
        String destination = StompHeaderAccessor.wrap(message).getDestination();
        Pattern roomIdPattern = Pattern.compile("^/topic/chat/room/(?<roomId>\\d+)($|/.*$)");
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

    
    



}