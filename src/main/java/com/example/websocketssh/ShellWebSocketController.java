package com.example.websocketssh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ShellWebSocketController extends TextWebSocketHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ShellWebSocketController.class); 
  private final ShellService shellService;

  public ShellWebSocketController(ShellService shellService) {
    this.shellService = shellService;
  }

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
    var standId = (long) session.getAttributes().get("standId");
    var shellId = (long) session.getAttributes().get("shellId");

    LOGGER.info("recieved message on stand[" + standId + "].shell[" + shellId + "], content = " + message.getPayload());
    shellService.sendToShell(standId, shellId, message.getPayload());
    LOGGER.info("message sent");
	}

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    var standId = (long) session.getAttributes().get("standId");
    var shellId = (long) session.getAttributes().get("shellId");

    LOGGER.info("connection " + session.getId() + " established");
    shellService.attachSocketSessionToShell(standId, shellId, session);
  }

}
