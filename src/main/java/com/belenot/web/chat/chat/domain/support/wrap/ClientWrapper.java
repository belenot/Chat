package com.belenot.web.chat.chat.domain.support.wrap;

import java.util.Map;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.support.ModelWrapper;

import org.springframework.stereotype.Component;

@Component
public class ClientWrapper implements ModelWrapper<Client> {

    @Override
    public Map<String, Object> wrapUp(Client client) {
        
        return null;
    }
    
}