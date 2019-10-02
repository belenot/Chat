package com.belenot.web.chat.chat.event;

import com.belenot.web.chat.chat.model.MessageModel;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class MessageSendedEventInfo implements RoomEventInfo {
    @NonNull
    private MessageModel messageModel;
}