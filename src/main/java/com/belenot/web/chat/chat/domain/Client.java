package com.belenot.web.chat.chat.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.belenot.web.chat.chat.domain.support.Deletable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.NaturalId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Client implements Deletable {
    @Id
    @GeneratedValue
    @NonNull
    private int id;
    @NaturalId
    @NonNull
    private String login;
    private String name;
    private String secondName;
    private Integer age;
    @JsonIgnore
    @NonNull
    private String password;

    // Deletable
    private boolean deleted;
    private LocalDateTime deletedTime;
    @ManyToOne
    @JsonIgnore
    private Client deleter;

    @OneToMany(mappedBy = "client", orphanRemoval = true)
    @JsonIgnore
    private List<Participant> participants;
    @OneToMany(mappedBy = "client", orphanRemoval = true)
    @JsonIgnore
    private List<Moderator> moderators;

    public void addModerator(Moderator moderator) {
        moderators.add(moderator);
        moderator.setClient(this);
    }
    public void removeModerator(Moderator moderator) {
        moderators.remove(moderator);
        moderator.setClient(null);
    }
    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.setClient(this);
    }
    public void removeParticipant(Participant participant) {
        participants.remove(participant);
        participant.setClient(null);
    }

}