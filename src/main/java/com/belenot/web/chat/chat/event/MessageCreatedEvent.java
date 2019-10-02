package com.belenot.web.chat.chat.event;

import com.belenot.web.chat.chat.domain.Message;

import org.springframework.context.ApplicationEvent;

public class MessageCreatedEvent extends ApplicationEvent {
    /**
	 *
	 */
	private static final long serialVersionUID = -1558997906488297629L;

	public MessageCreatedEvent(Message message) {
        super(message);
    }

}