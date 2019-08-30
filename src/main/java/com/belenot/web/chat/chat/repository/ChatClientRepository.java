package com.belenot.web.chat.chat.repository;

import java.util.List;

import com.belenot.web.chat.chat.domain.ChatClient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatClientRepository extends JpaRepository<ChatClient, Integer>{

    public ChatClient findByLogin(String login);
    public List<ChatClient> findByOnline(boolean online);
}