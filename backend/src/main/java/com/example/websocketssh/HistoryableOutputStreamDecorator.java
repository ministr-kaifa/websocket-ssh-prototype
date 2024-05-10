package com.example.websocketssh;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class HistoryableOutputStreamDecorator extends OutputStream {

  private final OutputStream stream;
  private final ArrayList<Byte> history;

  public List<Byte> getHistory() {
    return history;
  }

  public HistoryableOutputStreamDecorator(OutputStream stream) {
    this.stream = stream;
    this.history = new ArrayList<>();
  }

  @Override
  public void write(int b) throws IOException {
    history.add((byte)b);
    stream.write(b);
  }
  
}
