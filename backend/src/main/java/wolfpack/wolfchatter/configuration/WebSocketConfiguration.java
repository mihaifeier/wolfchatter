package wolfpack.wolfchatter.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import wolfpack.wolfchatter.handler.WebSocketChangeMarkerTitleHandler;
import wolfpack.wolfchatter.handler.WebSocketMarkerHandler;
import wolfpack.wolfchatter.handler.WebSocketMessageHandler;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer implements WebSocketConfigurer {
    private final WebSocketMarkerHandler markerHandler;
    private final WebSocketChangeMarkerTitleHandler markerTitleHandler;
    private final WebSocketMessageHandler messageHandler;

    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(1024 * 1024 * 1024);
        container.setMaxBinaryMessageBufferSize(1024 * 1024 * 1024);
        return container;
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(1024 * 1024 * 1024);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(markerHandler, "/new-marker").setAllowedOrigins("*");
        registry.addHandler(markerTitleHandler, "/new-marker-title").setAllowedOrigins("*");
        registry.addHandler(messageHandler, "/message").setAllowedOrigins("*");
    }
}
