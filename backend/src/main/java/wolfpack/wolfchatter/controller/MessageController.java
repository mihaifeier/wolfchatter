package wolfpack.wolfchatter.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wolfpack.wolfchatter.dto.EditMessageDto;
import wolfpack.wolfchatter.dto.MessageDto;
import wolfpack.wolfchatter.service.MessageService;

@RestController
@AllArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(value = "/save-message", method = RequestMethod.POST)
    public void saveMessage(@RequestBody MessageDto messageDto) {
        messageService.saveMessage(MessageDto.toEntity(messageDto), messageDto.getMarkerId());
    }

    @RequestMapping(value = "/edit-message", method = RequestMethod.PUT)
    public ResponseEntity<?> editMessage(@RequestBody EditMessageDto editMessageDto) {
        messageService.editMessage(editMessageDto.getMessageId(), editMessageDto.getMessage());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "delete-message/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }
}
