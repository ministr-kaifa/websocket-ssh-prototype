package com.example.websocketssh;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WriteableInputStream extends InputStream {

  private List<Byte> buffer = new ArrayList<>();

  public void write(Byte[] newData) {
    synchronized (buffer) {
      buffer.addAll(List.of(newData));
      buffer.notifyAll();    
    }
  }

  @Override
  public int read() throws IOException {
    synchronized (buffer) {
      waitIfNoAvailable();
      return buffer.remove(0);
    }
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    synchronized (buffer) {
      waitIfNoAvailable();
      for (int i = 0; i < len; i++) {
        if(available() == 0) {
          return i;
        }
        b[off + i] = buffer.remove(0);
      }
    }
    throw new IOException("this exception never be thrown");
  }

  @Override
  public int available() throws IOException {
    return buffer.size();
  }

  private void waitIfNoAvailable() {
    synchronized (buffer) {
      try {
        while (available() == 0) {
          buffer.wait();
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }  catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
  


}
