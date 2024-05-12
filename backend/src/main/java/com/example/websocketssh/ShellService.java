package com.example.websocketssh;

import java.io.IOException;
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
      var shell = stands.get(standId).shells().get(shellId);
      if (shell.isLaunched()) {
        shell.getOutStream().setSession(socketSession);
      } else {
        var channel = createChannel(stands.get(standId).address());
        channel.setPty(true); //понятия не имею что это меняет
        channel.setPtySize(116, 100, 1080, 720);  //значения как на фронте
        //channel.setExtOutputStream(outStream); что это вообще

        shell.setChannel(channel);
        var outStream = 
          new WebSocketDirectOutputStream(socketSession);
        var writeableInputStream = new WriteableInputStream();

        channel.setInputStream(writeableInputStream);
        channel.setOutputStream(outStream);
        shell.setWriteableInputStream(writeableInputStream);
        shell.setOutStream(outStream);
        shell.launch();
        shell.getChannel().connect();
      }
    } catch(JSchException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendToShell(long standId, long shellId, byte[] data) {
    stands.get(standId).shells().get(shellId).getWriteableInputStream()
      .write(ByteUtils.boxed(data));
  }

  public void resizeShellWindow(long standId, long shellId, int rows, int columns) {
    stands.get(standId).shells().get(shellId).getChannel()
      .setPtySize(columns, rows, 1000, 1000);
  }

  public void createShell(long standId) {
    stands.get(standId).shells()
      .put(stands.get(standId).shells().size() + 1L, new Shell());
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
