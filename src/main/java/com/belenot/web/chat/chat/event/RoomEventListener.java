package com.belenot.web.chat.chat.event;

import java.util.List;

import com.belenot.web.chat.chat.domain.Client;
import com.belenot.web.chat.chat.domain.Room;
import com.belenot.web.chat.chat.security.WebSocketSubscriptionHolder;
import com.belenot.web.chat.chat.service.ClientService;
import com.belenot.web.chat.chat.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RoomEventListener implements ApplicationListener<RoomEvent<? extends RoomEventInfo>> {

    @Autowired
    private WebSocketSubscriptionHolder subscriptionHolder;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ClientService clientService;
    private static final String destinationPrefix = "/topic/room/";
    
    @Override
    public void onApplicationEvent(RoomEvent<? extends RoomEventInfo> event) {
        int roomId = event.getRoomId();
        if (event.getSource() instanceof ClientBannedEventInfo && ((ClientBannedEventInfo)event.getSource()).isBanned()) {
            int clientId = ((ClientBannedEventInfo)event.getSource()).getClientId();
            subscriptionHolder.forceUnsubscribe(clientId, destinationPrefix + roomId);
        } else if (event.getSource() instanceof ClientLeavedEventInfo) {
            int clientId = ((ClientLeavedEventInfo)event.getSource()).getClientId();
            subscriptionHolder.forceUnsubscribe(clientId, destinationPrefix + roomId);
        } else if (event.getSource() instanceof RoomDeletedEventInfo) {
            Room room = roomService.byId(roomId);
            if (room != null) {
                List<Client> clients = clientService.byRoom(room);
                for (Client client : clients) {
                    subscriptionHolder.forceUnsubscribe(client.getId(), destinationPrefix + roomId);
                }
            }
        }

    }
    
}