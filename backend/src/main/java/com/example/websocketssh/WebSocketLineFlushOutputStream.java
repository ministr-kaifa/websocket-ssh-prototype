package com.example.websocketssh;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketLineFlushOutputStream extends OutputStream {

  private final StringBuilder buffer = new StringBuilder();
  private final WebSocketSession session;

  public WebSocketLineFlushOutputStream(WebSocketSession session) {
    this.session = session;
  }

  @Override
  public void write(int b) throws IOException {
    if((char)b == '\n') {
      flush();
    }
    buffer.append((char)b);
  }

  @Override
  public void flush() throws IOException {
    session.sendMessage(new TextMessage(buffer.toString()));
    buffer.delete(0, buffer.length());
  }
  
}
