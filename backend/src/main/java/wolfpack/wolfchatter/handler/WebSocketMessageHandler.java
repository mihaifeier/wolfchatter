package wolfpack.wolfchatter.handler;

import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import wolfpack.wolfchatter.dto.MessageChangeDto;
import wolfpack.wolfchatter.event.MessageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WebSocketMessageHandler
        extends AbstractWebSocketHandler
        implements ApplicationListener<MessageEvent> {
    Map<Integer, Map<String, WebSocketSession>> sessions = new HashMap<>();

    @SneakyThrows
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String command = message.getPayload().split(":")[0];
        Integer markerId = Integer.valueOf(message.getPayload().split(":")[1]);

        switch (command) {
            case "start":
                sessions.computeIfAbsent(markerId, k -> new HashMap<>());
                sessions.get(markerId).put(session.getId(), session);
                session.sendMessage(new TextMessage(session.getId()));
                break;
            case "stop":
                session.close();
                if (sessions.get(markerId) != null) {
                    sessions.get(markerId).remove(session.getId());
                }
                break;
        }
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(MessageEvent messageEvent) {
        TextMessage textMessage = new TextMessage(MessageChangeDto.eventToJson(messageEvent));
        Integer markerId = messageEvent.getMessage().getMarker().getId();
        if (sessions.containsKey(markerId)) {
            List<WebSocketSession> tempSessions = new ArrayList<>(
                    sessions.get(markerId).values()
            );
            for (WebSocketSession session : tempSessions) {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                } else {
                    session.close();
                    if (sessions.get(markerId) != null) {
                        sessions.get(markerId).remove(session.getId());
                    }
                }
            }
        }

    }
}
