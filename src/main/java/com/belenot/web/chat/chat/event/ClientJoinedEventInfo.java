package com.belenot.web.chat.chat.event;

import com.belenot.web.chat.chat.model.ClientModel;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ClientJoinedEventInfo implements RoomEventInfo {
    @NonNull
    private ClientModel clientModel;
}