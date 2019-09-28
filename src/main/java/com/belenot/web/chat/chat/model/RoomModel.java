package com.belenot.web.chat.chat.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.belenot.web.chat.chat.domain.Room;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;;

@NoArgsConstructor
@Getter
@Setter
public class RoomModel implements DomainModel<Room> {
    @JsonProperty(access = Access.READ_ONLY)
    private int id;
    @NotBlank(message = "You should specify title")
    private String title;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    public RoomModel(Room room) {
        id = room.getId();
        title = room.getTitle();
    }

    public static List<RoomModel> of(List<Room> rooms) {
        List<RoomModel> models = new ArrayList<>(rooms.size());
        for (Room room : rooms) {
            models.add(new RoomModel(room));
        }
        return models;
    }

    @Override
    public Room createDomain() {
        Room room = new Room();
        room.setTitle(title);
        room.setPassword(password);
        return room;
    }
    
}