package wolfpack.wolfchatter.handler;

import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import wolfpack.wolfchatter.dto.MarkerDto;
import wolfpack.wolfchatter.event.NewMarkerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WebSocketMarkerHandler extends AbstractWebSocketHandler implements ApplicationListener<NewMarkerEvent> {
    Map<String, WebSocketSession> sessions = new HashMap<>();

    @SneakyThrows
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        switch (message.getPayload()) {
            case "start":
                sessions.put(session.getId(), session);
                session.sendMessage(new TextMessage(session.getId()));
                break;
            case "stop":
                session.close();
                sessions.remove(session.getId());
                break;
        }
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(NewMarkerEvent event) {
        TextMessage textMessage = new TextMessage(MarkerDto.toJson(MarkerDto.toDto(event.getMarker())));
        List<WebSocketSession> tempSessions = new ArrayList<>(sessions.values());
        for (WebSocketSession session : tempSessions) {
            if (session.isOpen()) {
                session.sendMessage(textMessage);
            } else {
                sessions.remove(session.getId());
                session.close();
            }
        }
    }
}
