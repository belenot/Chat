package com.belenot.web.chat.chat.domain.support.wrap;

import java.util.HashMap;
import java.util.Map;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.support.ModelWrapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class ClientWrapper implements ModelWrapper<Client> {

    @Override
    public Map<String, Object> wrapUp(Client client) {
        Map<String, Object> model = new HashMap<>();
        model.put("id", client.getId());
        model.put("name", client.getName());
        model.put("secondName", client.getSecondName());
        model.put("login", client.getLogin());
        model.put("deleted", client.isDeleted());
        return model;
    }
    
}