package com.belenot.web.chat.chat.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

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
public class Client {
    @Id
    @GeneratedValue
    @NonNull
    private int id;
    @NaturalId
    @NonNull
    private String login;
    @JsonIgnore
    @NonNull
    private String password;
    @ManyToMany(mappedBy = "clients")
    private List<Room> rooms = new ArrayList<>();
    @ManyToMany(mappedBy = "banned")
    private List<Room> bannedRooms = new ArrayList<>();
}