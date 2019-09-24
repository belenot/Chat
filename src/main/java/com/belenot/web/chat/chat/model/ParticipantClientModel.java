package com.belenot.web.chat.chat.model;

import java.util.ArrayList;
import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Participant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(allowGetters = true)
public class ParticipantClientModel implements DomainModel<Participant> {
    private int clientId;
    private String login;
    private String name;
    private String secondName;
    private Integer age;
    private boolean deleted;
    private boolean banned;
    private boolean leaved;

    public ParticipantClientModel(Participant participant) {
        Client client = participant.getClient();
        if (client != null) {
            clientId = client.getId();
            login = client.getLogin();
            name = client.getName();
            secondName = client.getSecondName();
            age = client.getAge();
            deleted = client.isDeleted();
        }
        banned = participant.isBanned();
        leaved = participant.isDeleted();
    }

    public static List<ParticipantClientModel> of(List<Participant> participants) {
        List<ParticipantClientModel> models = new ArrayList<>(participants.size());
        for (Participant participant : participants) {
            models.add(new ParticipantClientModel(participant));
        }
        return models;
    }

    // Not supported for recieving. Only sended to response
    @Override
    public Participant createDomain() {
        return null;
    }
}