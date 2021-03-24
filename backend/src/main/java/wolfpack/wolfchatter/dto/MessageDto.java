package wolfpack.wolfchatter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wolfpack.wolfchatter.model.Message;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Integer id;
    private String message;
    public Integer markerId;
    private String user;
    private Timestamp timestamp;
    private Boolean deleted;

    public static MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getMessage(),
                message.getMarker().getId(),
                message.getUser(),
                message.getTimestamp(),
                message.getDeleted()
        );
    }

    public static Message toEntity(MessageDto messageDto) {
        return new Message(
                messageDto.getId(),
                messageDto.getMessage(),
                null,
                messageDto.getUser(),
                messageDto.getTimestamp(),
                messageDto.getDeleted()
        );
    }
}
