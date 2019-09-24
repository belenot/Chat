package com.belenot.web.chat.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableTransactionManagement
public class ChatApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}

}
