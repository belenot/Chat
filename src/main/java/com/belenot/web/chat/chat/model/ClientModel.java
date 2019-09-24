package com.belenot.web.chat.chat.model;

import java.util.ArrayList;
import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClientModel implements DomainModel<Client> {
    @JsonProperty(access = Access.READ_ONLY)
    private Integer id;
    private String login;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    private String name;
    private String secondName;
    private Integer age;
    @JsonProperty(access = Access.READ_ONLY)
    private boolean deleted;
    @JsonProperty(access = Access.READ_ONLY)
    private String deleter;

    public ClientModel(Client client) {
        id = client.getId();
        login = client.getLogin();
        name = client.getName();
        secondName = client.getSecondName();
        age = client.getAge();
        deleted = client.isDeleted();
        deleter = client.getDeleter() != null ? client.getDeleter().getLogin() : null;
    }

    public static List<ClientModel> of(List<Client> clients) {
        List<ClientModel> models = new ArrayList<>(clients.size());
        for (Client client : clients) {
            models.add(new ClientModel(client));
        }
        return models;
    }

    @Override
    public Client createDomain() {
        Client client = new Client();
        client.setLogin(login);
        client.setPassword(password);
        client.setName(name);
        client.setSecondName(secondName);
        client.setAge(age);
        return client;
    }

    @Override
    public void updateDomain(Client client) {
        client.setName(name != null ? name : client.getName());
        client.setSecondName(secondName != null ? secondName : client.getSecondName());
        client.setAge(age != null ? age : client.getAge());
    }
}