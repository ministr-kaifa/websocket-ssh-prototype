package com.example.websocketssh;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputStreamWriter {

  private static final Logger LOGGER = LoggerFactory.getLogger(InputStreamWriter.class); 

  private String history = "";
  private List<Byte> buffer = new ArrayList<>();
  private final Object monitor = new Object();

  public void write(String newData) {
    synchronized (monitor) {
      for (Byte byte1 : newData.getBytes(StandardCharsets.UTF_8)) {
        buffer.add(byte1);
      }
      monitor.notifyAll();    
    }
  }

  public String getHistory() {
    return history;
  }

  public InputStream getInputStream() {
    return new InputStream() {

      @Override
      public int read() throws IOException {
        waitIfNoAvailable();
        return buffer.remove(0);
      }

      @Override
      public int read(byte[] b, int off, int len) throws IOException {
        waitIfNoAvailable();
        for (int i = 0; i < len; i++) {
          if(available() == 0) {
            return i;
          }
          b[off + i] = buffer.remove(0);
        }
        throw new IOException("this exception never be thrown");
      }

      @Override
      public int available() throws IOException {
        return buffer.size();
      }

      private void waitIfNoAvailable() {
        synchronized (monitor) {
          try {
            while (available() == 0) {
              monitor.wait();
            }
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
          }  catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
      
    };
  }
}
