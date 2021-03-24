package wolfpack.wolfchatter.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeMarkerTitleEvent extends ApplicationEvent {
    private final Integer markerId;
    private final String newTitle;

    public ChangeMarkerTitleEvent(Object source, Integer markerId, String newTitle) {
        super(source);
        this.markerId = markerId;
        this.newTitle = newTitle;
    }
}
