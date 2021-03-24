package wolfpack.wolfchatter.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import wolfpack.wolfchatter.constant.MessageEventTypeEnum;
import wolfpack.wolfchatter.model.Message;

@Getter
public class MessageEvent extends ApplicationEvent {
    private final Message message;
    private final MessageEventTypeEnum messageEventType;

    public MessageEvent(Object source, Message message, MessageEventTypeEnum messageEventType) {
        super(source);
        this.message = message;
        this.messageEventType = messageEventType;
    }
}
