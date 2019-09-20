package com.belenot.web.chat.chat.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Moderator {
    @Id
    @GeneratedValue
    private int id;
    
    @ManyToOne
    private Room room;
    @ManyToOne
    @NonNull
    private Client client;
}