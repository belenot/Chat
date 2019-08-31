package com.belenot.web.chat.chat.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.NaturalId;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatClient {
    @Id
    @GeneratedValue
    private int id;
    @NaturalId
    private String login;
    @JsonIgnore
    private String password;
    private boolean online;
}