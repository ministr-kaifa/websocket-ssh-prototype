package com.example.websocketssh;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketTimerFlushOutputStream extends OutputStream {

  private final StringBuilder buffer = new StringBuilder();
  private final Lock bufferLock = new ReentrantLock();
  private final WebSocketSession session;

  public WebSocketTimerFlushOutputStream(WebSocketSession session) {
    this.session = session;
    CompletableFuture.runAsync(() -> {
      while (true) {
        try {
          TimeUnit.MILLISECONDS.sleep(100);
          flush();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  @Override
  public void write(int b) throws IOException {
    bufferLock.lock();
    buffer.append((char)b);
    bufferLock.unlock();
  }

  @Override
  public void flush() throws IOException {
    bufferLock.lock();
    session.sendMessage(new TextMessage(buffer.toString()));
    buffer.delete(0, buffer.length());
    bufferLock.unlock();
  }
  
}
