package com.example.websocketssh;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Component
public class ShellWebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(ShellWebSocketHandshakeInterceptor.class); 

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
    var endpointSplitted = request.getURI().getPath().split("/+");
    var standId = Long.parseLong(endpointSplitted[3]);
    var shellId = Long.parseLong(endpointSplitted[5]);
    //var user = userService.fromRawHeader(request.getHeaders().get("Authorization"))
    attributes.put("standId", standId);
    attributes.put("shellId", shellId);
    return true;
  }

}
