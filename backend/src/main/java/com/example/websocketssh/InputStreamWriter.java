package com.example.websocketssh;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputStreamWriter {

  private static final Logger LOGGER = LoggerFactory.getLogger(InputStreamWriter.class); 

  private String history = "";
  private StringBuilder buffer = new StringBuilder();
  private final Object monitor = new Object();

  public void write(String newData) {
    synchronized (monitor) {
      buffer.append(newData);
      history += buffer.toString();
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
        lockSelfIfUnavailable();
        return popBeginBuffer();
      }

      @Override
      public int read(byte[] b, int off, int len) throws IOException {
        lockSelfIfUnavailable();
        for (int i = 0; i < len; i++) {
          if(available() == 0) {
            return i;
          }
          b[off + i] = (byte)popBeginBuffer();
        }
        throw new IOException("this exception never be thrown");
      }

      @Override
      public int available() throws IOException {
        return buffer.length();
      }

      private int popBeginBuffer() {
        var result = buffer.charAt(0);
        buffer.deleteCharAt(0);
        return result;
      }

      private void lockSelfIfUnavailable() {
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


  // public InputStream getInputStream() {
  //   return new InputStream() {

  //     String echo = "echo hello\n";
  //     int cursor = 0;

  //     @Override
  //     public int read() throws IOException {
  //       cursor++;
  //       return echo.charAt((cursor - 1) % echo.length());
  //     }

  //     @Override
  //     public int available() throws IOException {
  //       return 1;
  //     }
  //   };
  // }
