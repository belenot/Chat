package com.belenot.web.chat.chat.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatMessage {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ChatClient client;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime time;
    private String text;


}