package com.example.websocketssh;

import java.io.OutputStream;

import com.jcraft.jsch.ChannelShell;

public class Shell {
  private OutputStream outStream;
  private WriteableInputStream writeableInputStream;
  private ChannelShell channel;
  private String webSocketSessionId;

  public void setWebSocketSessionId(String webSocketSessionId) {
    this.webSocketSessionId = webSocketSessionId;
  }

  public void setOutStream(OutputStream outStream) {
    this.outStream = outStream;
  }

  public void setWriteableInputStream(WriteableInputStream inStreamWriter) {
    this.writeableInputStream = inStreamWriter;
  }
  
  public void setChannel(ChannelShell channel) {
    this.channel = channel;
  }

  public OutputStream getOutStream() {
    return outStream;
  }

  public WriteableInputStream getWriteableInputStream() {
    return writeableInputStream;
  }

  public ChannelShell getChannel() {
    return channel;
  }

  public String getWebSocketSessionId() {
    return webSocketSessionId;
  }

}
