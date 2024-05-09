package com.example.websocketssh;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.jcraft.jsch.Channel;
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
        new HistoryableOutputStreamDecorator(new WebSocketDirectOutputStream(socketSession));
      channel.setOutputStream(outStream);
      
      var inStreamWriter = new InputStreamWriter();
      channel.setInputStream(inStreamWriter.getInputStream());

      var shell = stands.get(standId).shells().get(shellId);
      shell.setChannel(channel);
      shell.setInStreamWriter(inStreamWriter);
      shell.setOutStream(outStream);

      channel.connect(); 
    } catch(JSchException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendToShell(long standId, long shellId, String data) {
    stands.get(standId).shells().get(shellId).getInStreamWriter().write(data);
  }

  public void createShell(long standId) {
    stands.get(standId).shells().put(stands.get(standId).shells().size() + 1L, new Shell());
  }

  private Channel createChannel(String address) {
    try {
      Session session = jsch.getSession(USER, address);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();
      return session.openChannel("shell");
    } catch (JSchException e) {
      throw new RuntimeException(e);
    }
  }

}
