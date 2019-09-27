package com.belenot.web.chat.chat.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.belenot.web.chat.chat.domain.support.Deletable;

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
public class Participant implements Deletable {
    @Id
    @GeneratedValue
    private int id;
    private boolean banned;
    

    // Deletable
    private boolean deleted;
    private LocalDateTime deletedTime;
    @ManyToOne
    private Client deleter;

    @NonNull
    @ManyToOne
    private Client client;
    @NonNull
    @ManyToOne
    private Room room;
    @OneToMany(mappedBy = "participant", cascade = CascadeType.REMOVE)
    private List<Message> messages;
}