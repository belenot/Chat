package com.belenot.web.chat.chat.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ClientBannedEventInfo implements RoomEventInfo {
    @NonNull
    private int clientId;
    @NonNull
    private boolean banned;
}