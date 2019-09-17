package com.belenot.web.chat.chat.controller;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.security.ClientDetails;
import com.belenot.web.chat.chat.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/update/name")
    public Client name(@RequestParam("name") String name) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        client.setName(name);
        return clientService.update(client);
    }

    @PostMapping("/update/age")
    public Client age(@RequestParam("age") int age) {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        client.setAge(age);
        return clientService.update(client);
    }

    @PostMapping("/update/password")
    public Client age(@RequestParam("password") String password) {
        // Comming soon
        return null;
    }

    @PostMapping("/delete")
    public void delete() {
        Client client = ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient();
        clientService.delete(client);
    }


    
}