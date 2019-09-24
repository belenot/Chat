package com.belenot.web.chat.chat.domain.support.wrap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Message;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.domain.support.ModelWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class RoomWrapper implements ModelWrapper<Room> {
    @Autowired
    private MessageWrapper messageWrapper;
    @Autowired
    private ClientWrapper clientWrapper;

    @Override
    public Map<String, Object> wrapUp(Room room) {
        Map<String, Object> model = new HashMap<>();
        model.put("id", room.getId());
        model.put("title", room.getTitle());
        return model;
    }

    public Map<String, Object> loadedRoomWrapUp(Room room, List<Client> clients) {
        Map<String, Object> model = new HashMap<>();
        model.put("room", wrapUp(room));
        model.put("clients", clientWrapper.wrapUp(clients));
        return model;
    }
    
}