package com.belenot.web.chat.chat.event;

import org.springframework.context.ApplicationEvent;

public class RoomEvent<T extends RoomEventInfo> extends ApplicationEvent {
    private static final long serialVersionUID = -6163808874027880710L;

    private int roomId;
    private String description;

    public RoomEvent(T info) {
        super(info);
    }
    public RoomEvent(int roomId, String description, T info) {
        super(info);
        this.roomId = roomId;
        this.description = description;
    }

    public int getRoomId() { return roomId; }
    public String getDescription() { return description; }
    @Override
    public RoomEventInfo getSource() {
        Object source = super.getSource();
        if (source instanceof RoomEventInfo) {
            return (RoomEventInfo) source;
        } else {
            return null;
        }
    }

}