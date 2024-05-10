package com.example.websocketssh;

import com.jcraft.jsch.Channel;

public class Shell {
  private HistoryableOutputStreamDecorator outStream;
  private InputStreamWriter inStreamWriter;
  private Channel channel;
  private String webSocketSessionId;

  public void setWebSocketSessionId(String webSocketSessionId) {
    this.webSocketSessionId = webSocketSessionId;
  }

  public void setOutStream(HistoryableOutputStreamDecorator outStream) {
    this.outStream = outStream;
  }

  public void setInStreamWriter(InputStreamWriter inStreamWriter) {
    this.inStreamWriter = inStreamWriter;
  }
  
  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  public HistoryableOutputStreamDecorator getOutStream() {
    return outStream;
  }

  public InputStreamWriter getInStreamWriter() {
    return inStreamWriter;
  }

  public Channel getChannel() {
    return channel;
  }

  public String getWebSocketSessionId() {
    return webSocketSessionId;
  }

}
