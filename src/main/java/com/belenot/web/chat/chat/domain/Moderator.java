package com.belenot.web.chat.chat.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Moderator extends Client {
    public Moderator() {
        super();
    }
    @ManyToMany(mappedBy = "moderators")
    private List<Room> moderatedRooms = new ArrayList<>();
}