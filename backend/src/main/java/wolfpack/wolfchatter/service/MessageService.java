package wolfpack.wolfchatter.service;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import wolfpack.wolfchatter.constant.MessageEventTypeEnum;
import wolfpack.wolfchatter.event.MessageEvent;
import wolfpack.wolfchatter.model.Marker;
import wolfpack.wolfchatter.model.Message;
import wolfpack.wolfchatter.repository.MarkerRepository;
import wolfpack.wolfchatter.repository.MessageRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MarkerRepository markerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void saveMessage(Message message, Integer markerId) {
        Marker marker = markerRepository.findById(markerId).orElse(null);
        message.setMarker(marker);
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));
        message.setDeleted(false);
        messageRepository.save(message);
        publishMessageEvent(message, MessageEventTypeEnum.CREATE);
    }

    public void editMessage(Integer messageId, String newMessage) {
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isEmpty()) {
            return;
        }

        Message message = messageOptional.get();
        message.setMessage(newMessage);
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));
        messageRepository.save(message);

        publishMessageEvent(message, MessageEventTypeEnum.UPDATE);
    }

    public void deleteMessage(Integer messageId) {
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isEmpty()) {
            return;
        }
        Message message = messageOptional.get();
        message.setDeleted(true);
        messageRepository.save((message));

        publishMessageEvent(message, MessageEventTypeEnum.DELETE);
    }

    public List<Message> getAllMessagesByMarker(Marker marker) {
        return messageRepository.findAllByMarker(marker);
    }

    public void publishMessageEvent(final Message message, MessageEventTypeEnum messageEventType) {
        applicationEventPublisher.publishEvent(new MessageEvent(this, message, messageEventType));
    }
}
