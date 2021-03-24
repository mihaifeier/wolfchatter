package wolfpack.wolfchatter.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wolfpack.wolfchatter.dto.MessageDto;
import wolfpack.wolfchatter.model.Marker;
import wolfpack.wolfchatter.service.MarkerService;
import wolfpack.wolfchatter.service.MessageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class ChatWindowController {
    private final MarkerService markerService;
    private final MessageService messageService;

    @RequestMapping(value = "/get-chat-window-details/{markerId}", method = RequestMethod.GET)
    public Map<String, Object> getChatWindowDetails(@PathVariable Integer markerId) {
        Map<String, Object> response = new HashMap<>();

        String name;
        List<MessageDto> messages;

        Marker marker = markerService.getMarkerById(markerId);
        if (marker == null) {
            name = "";
            messages = new ArrayList<>();
        } else {
            name = marker.getName();
            messages = messageService
                    .getAllMessagesByMarker(marker)
                    .stream()
                    .map(MessageDto::toDto)
                    .collect(Collectors.toList());
        }

        response.put("name", name);
        response.put("messages", messages);

        return response;
    }
}
