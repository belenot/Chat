package com.belenot.web.chat.chat.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.belenot.web.chat.chat.domain.ChatClient;
import com.belenot.web.chat.chat.service.ChatClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class AuthorizationController {

    @Autowired
    private ChatClientService chatClientService;

    @GetMapping("/authorization")
    public String authorization() {
        return "authorization";
    }

    @PostMapping("/authorization")
    public void authorization(@RequestParam("login") String login, @RequestParam("password") String password, 
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        ChatClient client = chatClientService.byAccount(login, password);
        if (client == null) return;
        client.setOnline(true);
        chatClientService.add(client);
        request.getSession().setAttribute("client", client);
        response.sendRedirect("/chat");        
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping(path = "/registration")
    public void registration(@RequestParam("login") String login, @RequestParam("password") String password, 
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        ChatClient client = new ChatClient();
        client.setLogin(login);
        client.setPassword(password);
        client.setOnline(true);
        chatClientService.add(client);
        request.getSession().setAttribute("client", client);
        response.sendRedirect("/chat");
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ChatClient client = (ChatClient) session.getAttribute("client");
            client.setOnline(false);
            chatClientService.add(client);
            session.removeAttribute("client");
            session.invalidate();
        }
        response.sendRedirect("authorization");
    }

}