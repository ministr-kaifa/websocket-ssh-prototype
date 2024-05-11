package com.example.websocketssh;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketDirectOutputStream extends OutputStream {

  private final WebSocketSession session;

  public WebSocketDirectOutputStream(WebSocketSession session) {
    this.session = session;
  }

  @Override
  public void write(int b) throws IOException {
    session.sendMessage(new BinaryMessage(new byte[]{(byte)b}));
  }

  // @Override
  // public void write(byte[] b, int off, int len) throws IOException {
  //   session.sendMessage(new BinaryMessage(Arrays.copyOfRange(b, off, len)));
  // }

  // @Override
  // public void write(byte[] b) throws IOException {
  //   session.sendMessage(new BinaryMessage(b));
  // }
  
}
