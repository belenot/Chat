package com.belenot.web.chat.chat.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Moderator;
import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.service.ModeratorService;
import com.belenot.web.chat.chat.service.ParticipantService;
import com.belenot.web.chat.chat.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ParticipantAuthoritiesChecker {
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private ModeratorService moderatorService;
    @Autowired
    private RoomService roomService;

    // Try with Room parameter instead of int roomId. Maybe spring data extension works on this
    public boolean isJoined(int roomId, Client client) {
        Room room = roomService.byId(roomId);
        Participant participant = participantService.byClientAndRoom(client, room);
        return participant != null && !participant.isDeleted();
    }

    public boolean isBanned(int roomId, Client client) {
        Room room = roomService.byId(roomId);
        Participant participant = participantService.byClientAndRoom(client, room);
        return participant != null && participant.isBanned();
    }

    public boolean isModerator(int roomId, Client client) {
        Room room = roomService.byId(roomId);
        Moderator moderator = moderatorService.byClientAndRoom(client, room);
        return moderator != null;
    }

    // For http
    public boolean isJoined(int roomId, Authentication authentication) {
        Client client = ((ClientDetails)authentication.getPrincipal()).getClient();
        return isJoined(roomId, client);
    }

    public boolean isBanned(int roomId, Authentication authentication) {
        Client client = ((ClientDetails)authentication.getPrincipal()).getClient();
        return isBanned(roomId, client);
    }

    public boolean isModerator(int roomId, Authentication authentication) {
        Client client = ((ClientDetails)authentication.getPrincipal()).getClient();
        return isModerator(roomId, client);
    }

    // For WebSocket
    private static final String webSocketRoomPathPattern = "^/(topic|app)/room/(?<roomId>\\d+)(/message/new)?$";
    private Pattern pattern = Pattern.compile(webSocketRoomPathPattern);

    public boolean isJoined(Message<?> message, Authentication authentication) {
        Client client = ((ClientDetails)authentication.getPrincipal()).getClient();
        Integer roomId = retrieveRoomId(message);
        if (roomId == null) return false;
        return isJoined(roomId, client);
    }

    public boolean isBanned(Message<?> message, Authentication authentication) {
        Client client = ((ClientDetails)authentication.getPrincipal()).getClient();
        Integer roomId = retrieveRoomId(message);
        if (roomId == null) return false;
        return isBanned(roomId, client);
    }

    public boolean isModerator(Message<?> message, Authentication authentication) {
        Client client = ((ClientDetails)authentication.getPrincipal()).getClient();
        Integer roomId = retrieveRoomId(message);
        if (roomId == null) return false;
        return isModerator(roomId, client);
    }

    private Integer retrieveRoomId(Message<?> message) {
        String destination = SimpMessageHeaderAccessor.wrap(message).getDestination();
        Matcher matcher =   pattern.matcher(destination);
        Integer roomId = null;
        try {
            if (matcher.find()) {
                roomId = Integer.parseInt(matcher.group("roomId"));
            }
        } catch (NullPointerException | NumberFormatException exc) {
            return null;
        }
        return roomId;
    }
}