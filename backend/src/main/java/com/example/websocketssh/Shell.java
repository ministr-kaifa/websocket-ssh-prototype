package com.example.websocketssh;

import com.jcraft.jsch.ChannelShell;

public class Shell {
  private WebSocketDirectOutputStream outStream;
  private WriteableInputStream writeableInputStream;
  private ChannelShell channel;
  private String webSocketSessionId;
  private boolean isLaunched = false;

  public boolean isLaunched() {
    return isLaunched;
  }

  public void launch() {
    isLaunched = true;
  }

  public void setWebSocketSessionId(String webSocketSessionId) {
    this.webSocketSessionId = webSocketSessionId;
  }

  public void setOutStream(WebSocketDirectOutputStream outStream) {
    this.outStream = outStream;
  }

  public void setWriteableInputStream(WriteableInputStream inStreamWriter) {
    this.writeableInputStream = inStreamWriter;
  }
  
  public void setChannel(ChannelShell channel) {
    this.channel = channel;
  }

  public WebSocketDirectOutputStream getOutStream() {
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
