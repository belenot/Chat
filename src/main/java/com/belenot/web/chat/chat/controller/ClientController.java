package com.belenot.web.chat.chat.controller;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.model.ClientModel;
import com.belenot.web.chat.chat.security.ClientDetails;
import com.belenot.web.chat.chat.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Security: authenticated clients
    @PostMapping(path="/update", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientModel update(@RequestBody ClientModel clientModel) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        clientModel.updateDomain(client);
        client = clientService.update(client);
        clientModel = new ClientModel(client);
        return clientModel;
    }

    // Security: authenticated clients
    @PostMapping("/delete")
    public void delete() {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        clientService.delete(client);
    }


    
}