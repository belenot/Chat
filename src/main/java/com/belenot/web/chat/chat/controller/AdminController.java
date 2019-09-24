package com.belenot.web.chat.chat.controller;

import com.belenot.web.chat.chat.security.ClientDetails;
import com.belenot.web.chat.chat.service.ClientService;
import com.belenot.web.chat.chat.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private RoomService roomService;
    @GetMapping
    public String admin(Model model) {
        model.addAttribute("admin", (ClientDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("clients", clientService.all());
        model.addAttribute("rooms", roomService.all());
        return "admin";
    }
}