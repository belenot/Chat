package com.belenot.web.chat.chat.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.NaturalId;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue
    private int id;
    @NaturalId
    private String title;
    private String password;
    @ManyToMany
    private List<Client> clients = new ArrayList<>();
    @ManyToMany
    private List<Moderator> moderators = new ArrayList<>();
    @ManyToMany
    private List<Client> banned = new ArrayList<>();
}