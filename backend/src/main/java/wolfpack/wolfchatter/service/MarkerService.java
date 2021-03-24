package wolfpack.wolfchatter.service;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import wolfpack.wolfchatter.event.ChangeMarkerTitleEvent;
import wolfpack.wolfchatter.event.NewMarkerEvent;
import wolfpack.wolfchatter.model.Marker;
import wolfpack.wolfchatter.repository.MarkerRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MarkerService {
    private final MarkerRepository markerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<Marker> getAllMarkers() {
        return markerRepository
                .findAll();
    }

    public void saveMarker(Marker marker) {
        markerRepository.save(marker);
        publishNewMarkerEvent(marker);
    }

    public void changeMarkerTitle(Integer markerId, String newTitle) {
        Optional<Marker> markerOptional = markerRepository.findById(markerId);
        if (markerOptional.isEmpty()) {
            return;
        }

        Marker marker = markerOptional.get();
        marker.setName(newTitle);
        markerRepository.save(marker);

        publishChangeMarkerTitleEvent(markerId, newTitle);
    }

    public Marker getMarkerById(final Integer markerId) {
        return markerRepository.findById(markerId).orElse(null);
    }

    public void publishNewMarkerEvent(final Marker marker) {
        applicationEventPublisher.publishEvent(new NewMarkerEvent(this, marker));
    }

    public void publishChangeMarkerTitleEvent(final Integer markerId, final String newTitle) {
        applicationEventPublisher.publishEvent(new ChangeMarkerTitleEvent(this, markerId, newTitle));
    }
}
