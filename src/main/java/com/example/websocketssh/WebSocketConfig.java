package com.example.websocketssh;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final ShellWebSocketController handler;
  private final HandshakeInterceptor[] interceptors;

  public WebSocketConfig(ShellWebSocketController handler, HandshakeInterceptor[] interceptors) {
    this.handler = handler;
    this.interceptors = interceptors;
  }

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(handler, "/api/stands/{standId}/shells/{shellId}")
      .addInterceptors(interceptors);
	}

}
