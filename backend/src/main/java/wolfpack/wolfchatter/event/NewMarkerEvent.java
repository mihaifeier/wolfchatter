package wolfpack.wolfchatter.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import wolfpack.wolfchatter.model.Marker;

@Getter
public class NewMarkerEvent extends ApplicationEvent {
    private final Marker marker;

    public NewMarkerEvent(Object source, Marker marker) {
        super(source);
        this.marker = marker;
    }
}
