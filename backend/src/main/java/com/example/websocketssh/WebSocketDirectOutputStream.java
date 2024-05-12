package com.example.websocketssh;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketDirectOutputStream extends OutputStream {

  private WebSocketSession session;
  private final List<byte[]> history = new ArrayList<>();

  public WebSocketDirectOutputStream(WebSocketSession session) {
    this.session = session;
  }

  @Override
  public void write(int b) throws IOException {
    history.add(new byte[]{(byte)b});
    session.sendMessage(new BinaryMessage(new byte[]{(byte)b}));
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    var data = Arrays.copyOfRange(b, off, off + len);
    history.add(data);
    session.sendMessage(new BinaryMessage(data));
  }

  @Override
  public void write(byte[] b) throws IOException {
    history.add(b);
    session.sendMessage(new BinaryMessage(b));
  }

  public void setSession(WebSocketSession session) {
    this.session = session;
    try {
      write(ByteUtils.unboxed(historyConcated()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Byte[] historyConcated() {
    return history.stream()
      .flatMap(primiteiveBinaryData -> {
        var binaryData = new Byte[primiteiveBinaryData.length];
        Arrays.setAll(binaryData, i -> primiteiveBinaryData[i]);
        return Stream.of(binaryData);
      })
      .toArray(size -> new Byte[size]);
  }
  
}
