package wolfpack.wolfchatter.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wolfpack.wolfchatter.dto.MarkerDto;
import wolfpack.wolfchatter.dto.MarkerTitleDto;
import wolfpack.wolfchatter.service.MarkerService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class MarkerController {
    private final MarkerService markerService;

    @RequestMapping(value = "/get-markers", method = RequestMethod.GET)
    public List<MarkerDto> getMarkers() {
        return markerService
                .getAllMarkers()
                .stream()
                .map(MarkerDto::toDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/save-marker", method = RequestMethod.POST)
    public ResponseEntity<?> saveMarker(@RequestBody MarkerDto markerDto) {
        markerService.saveMarker(MarkerDto.toEntity(markerDto));
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/change-marker-title", method = RequestMethod.PUT)
    public ResponseEntity<?> changeTitle(@RequestBody MarkerTitleDto markerTitleDto) {
        markerService.changeMarkerTitle(markerTitleDto.getMarkerId(), markerTitleDto.getNewTitle());
        return ResponseEntity.ok().build();
    }

}
