package wolfpack.wolfchatter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;
import wolfpack.wolfchatter.constant.MessageEventTypeEnum;
import wolfpack.wolfchatter.event.MessageEvent;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class MessageChangeDto {
    private Integer id;
    private String message;
    public Integer markerId;
    private String user;
    private Timestamp timestamp;
    private MessageEventTypeEnum messageEventType;

    public static String eventToJson(MessageEvent messageEvent) {
        JSONObject messageChangeDto = new JSONObject();
        messageChangeDto.put("id", messageEvent.getMessage().getId());
        messageChangeDto.put("user", messageEvent.getMessage().getUser());
        messageChangeDto.put("message", messageEvent.getMessage().getMessage());
        messageChangeDto.put("marker", messageEvent.getMessage().getMarker().getId());
        messageChangeDto.put("timestamp", messageEvent.getMessage().getTimestamp());
        messageChangeDto.put("deleted", messageEvent.getMessage().getDeleted());
        messageChangeDto.put("eventType", messageEvent.getMessageEventType());
        return messageChangeDto.toString();
    }
}
