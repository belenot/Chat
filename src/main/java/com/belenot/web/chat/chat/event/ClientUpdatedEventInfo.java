package com.belenot.web.chat.chat.event;

import com.belenot.web.chat.chat.model.ClientModel;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
@Setter
public class ClientUpdatedEventInfo implements RoomEventInfo {
    @NonNull
    private ClientModel clientModel;
}