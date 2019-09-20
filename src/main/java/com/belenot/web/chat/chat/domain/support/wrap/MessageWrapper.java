package com.belenot.web.chat.chat.domain.support.wrap;

import java.util.HashMap;
import java.util.Map;

import com.belenot.web.chat.chat.domain.Message;
import com.belenot.web.chat.chat.domain.support.ModelWrapper;

import org.springframework.stereotype.Component;

@Component
public class MessageWrapper implements ModelWrapper<Message> {

    @Override
    public Map<String, Object> wrapUp(Message message) {
        Map<String, Object> model = new HashMap<>();
        model.put("id", message.getId());
        model.put("text", message.getText());
        model.put("login", message.getParticipant().getClient().getLogin());
        model.put("time", message.getTime());
        return model;
    }
}