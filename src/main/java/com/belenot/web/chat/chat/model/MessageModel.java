package com.belenot.web.chat.chat.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.belenot.web.chat.chat.domain.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MessageModel implements DomainModel<Message> {
    @JsonProperty(access = Access.READ_ONLY)
    private int id;
    private String text;
    private LocalDateTime time;
    private String login;

    public MessageModel(Message message) {
        id = message.getId();
        text = message.getText();
        time = message.getTime();
        login = message.getParticipant().getClient().getLogin();
    }

    public static List<MessageModel> of(List<Message> messages) {
        List<MessageModel> models = new ArrayList<>(messages.size());
        for (Message message : messages) {
            models.add(new MessageModel(message));
        }
        return models;
    }

    @Override
    public Message createDomain() {
        Message message = new Message();
        message.setText(text);
        message.setTime(time);
        return message;
    }
    
    
}