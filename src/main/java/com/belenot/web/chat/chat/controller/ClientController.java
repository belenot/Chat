package com.belenot.web.chat.chat.controller;

import com.belenot.web.chat.chat.domain.Client;
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
    public Client update(@RequestBody Client updatedClient) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        if (updatedClient.getAge() > 0) client.setAge(updatedClient.getAge());
        if (updatedClient.getName() != null && !updatedClient.getName().equals("")) client.setName(updatedClient.getName());
        if (updatedClient.getSecondName() != null && !updatedClient.getSecondName().equals("")) client.setSecondName(updatedClient.getSecondName());
        return clientService.update(client);
    }

    // Security: authenticated clients
    @PostMapping("/delete")
    public void delete() {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        clientService.delete(client);
    }


    
}