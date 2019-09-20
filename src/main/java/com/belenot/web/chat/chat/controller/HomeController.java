package com.belenot.web.chat.chat.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.belenot.web.chat.chat.domain.Admin;
import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.security.ClientDetails;
import com.belenot.web.chat.chat.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping( "/" )
public class HomeController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/signup")
    public void signup(Client client, @RequestParam("adminkey") String key, HttpServletResponse response) 
        throws ServletException, IOException {
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

    @GetMapping("/chat")
    public String chat(Model model) {
        model.addAttribute("client", ((ClientDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClient());
        return "chat";
    }
    

}