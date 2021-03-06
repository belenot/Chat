package com.belenot.web.chat.chat.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@NotNull
public class Room {
    @Id
    @GeneratedValue
    private int id;
    @NaturalId
    private String title;
    private String password;
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<Participant> participants = new ArrayList<>();
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<Moderator> moderators = new ArrayList<>();
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<Participant> banned = new ArrayList<>();

    public void addModerator(Moderator moderator) {
        moderators.add(moderator);
        moderator.setRoom(this);
    }

    public void removeModerator(Moderator moderator) {
        moderators.removeIf(m -> m.getId() == moderator.getId());
        moderator.setRoom(null);
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.setRoom(this);
    }
    
    public void removeParticipant(Participant participant) {
        participants.removeIf(p ->p.getId() == participant.getId());
        participant.setRoom(null);
    }

    
}