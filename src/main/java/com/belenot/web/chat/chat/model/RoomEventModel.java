package com.belenot.web.chat.chat.model;

import com.belenot.web.chat.chat.event.RoomEventInfo;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RoomEventModel {
    @NonNull
    private RoomEventInfo info;
    @NonNull
    private String description;
}