package com.belenot.web.chat.chat.model;

import com.belenot.web.chat.chat.domain.Participant;
import com.belenot.web.chat.chat.domain.Room;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(allowGetters = true)
public class LoadedRoomModel implements DomainModel<Room> {

    private int id;
    private String title;
    private Boolean banned = null;
    private Boolean joined = false;

    public LoadedRoomModel(Room room, Participant participant) {
        id = room.getId();
        title = room.getTitle();
        if (participant == null || participant.isDeleted()) {
            joined = false;
        } else {
            joined = true;
            banned = participant.isBanned();
        }
    }

    // Not supported for recieving. Only sended to response
    @Override
    public Room createDomain() {
        
        return null;
    }
    
}