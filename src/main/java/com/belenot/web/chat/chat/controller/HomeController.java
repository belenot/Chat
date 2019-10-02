package com.belenot.web.chat.chat.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.belenot.web.chat.chat.domain.Admin;
import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.model.ClientModel;
import com.belenot.web.chat.chat.security.ClientDetails;
import com.belenot.web.chat.chat.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping( "/" )
@Validated
public class HomeController {

    @Autowired
    private ClientService clientService;

    // Security: Any non authenticated
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Security: Any non authenticated
    @PostMapping("/signup")
    public void signup(@Valid ClientModel clientModel, @RequestParam("adminkey") String key, HttpServletResponse response) 
        throws ServletException, IOException {
        Client client = clientModel.createDomain();
        if (key.equals("admin")) {
            Admin admin = new Admin();
            admin.setLogin(client.getLogin());
            admin.setPassword(client.getPassword());
            clientService.add(admin);
        } else {
            clientService.add(client);
        }
        response.sendRedirect("/login");
    }

    // Security: authenticated client
    @GetMapping("/chat")
    public String chat(Model model) {
        model.addAttribute("client", ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient());
        return "chat";
    }
    

}