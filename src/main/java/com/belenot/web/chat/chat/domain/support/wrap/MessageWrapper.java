package com.belenot.web.chat.chat.domain.support.wrap;

import java.util.HashMap;
import java.util.Map;

import com.belenot.web.chat.chat.domain.Message;
import com.belenot.web.chat.chat.domain.support.ModelWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class MessageWrapper implements ModelWrapper<Message> {

    @Autowired
    private ClientWrapper clientWrapper;

    @Override
    public Map<String, Object> wrapUp(Message message) {
        Map<String, Object> model = new HashMap<>();
        model.put("id", message.getId());
        model.put("text", message.getText());
        model.put("client", clientWrapper.wrapUp(message.getParticipant().getClient()));
        model.put("time", message.getTime());
        return model;
    }
}