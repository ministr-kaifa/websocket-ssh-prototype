package com.example.websocketssh;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Service
public class ShellService {

  private static final String KEY_FILE = "/key"; 
  private static final String USER = "user";

  private final Map<Long, Stand> stands;
  private final JSch jsch;

  public ShellService() {
    jsch = new JSch();
    try {
      //new ClassPathResource("key").getPath();
      jsch.addIdentity("rsa", getClass().getResourceAsStream(KEY_FILE).readAllBytes(), null, null);
    } catch (JSchException | IOException e) {
      throw new RuntimeException(e);
    }

    stands = Map.of(
      1L, new Stand(1, "176.109.100.59", new HashMap<>())
    );
    createShell(1);
  }

  public void attachSocketSessionToShell(long standId, long shellId, WebSocketSession socketSession) {
    try {
      var channel = createChannel(stands.get(standId).address());
      
      var outStream = 
        new WebSocketDirectOutputStream(socketSession);
      channel.setOutputStream(outStream);
      
      var writeableInputStream = new WriteableInputStream();
      channel.setInputStream(writeableInputStream);

      var shell = stands.get(standId).shells().get(shellId);
      shell.setChannel(channel);
      shell.setWriteableInputStream(writeableInputStream);
      shell.setOutStream(outStream);

      channel.connect(); 
    } catch(JSchException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendToShell(long standId, long shellId, String data) {
    var primiteiveBinaryData = data.getBytes(StandardCharsets.UTF_8);
    var binaryData = new Byte[primiteiveBinaryData.length];
    Arrays.setAll(binaryData, i -> primiteiveBinaryData[i]);
    stands.get(standId).shells().get(shellId).getWriteableInputStream().write(binaryData);
  }

  public void resizeShellWindow(long standId, long shellId, int rows, int columns) {
    stands.get(standId).shells().get(shellId).getChannel().setPtySize(columns, rows, 1000, 1000);
  }

  public void createShell(long standId) {
    stands.get(standId).shells().put(stands.get(standId).shells().size() + 1L, new Shell());
  }

  private ChannelShell createChannel(String address) {
    try {
      Session session = jsch.getSession(USER, address);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();
      return (ChannelShell)session.openChannel("shell");
    } catch (JSchException e) {
      throw new RuntimeException(e);
    }
  }

}
