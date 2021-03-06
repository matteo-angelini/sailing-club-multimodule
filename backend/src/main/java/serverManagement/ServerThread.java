package serverManagement;

import entities.Member;
import messageManagement.Message;
import messageManagement.Reply;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {
  private static final int MAX = 100;

  private Server server;
  private final Socket socket;

  /**
   * Constructor for initializing the client of connection
   * and the server which created this thread
   * 
   * @param server Server instance
   * @param client Client instance
   */
  public ServerThread(final Server server, final Socket client) {
    this.server = server;
    this.socket = client;
  }

  @Override
  public void run() {
    ObjectInputStream is = null;
    ObjectOutputStream os = null;

    // Tries to establish a connection with the source stream
    try {
      is = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    while (true) {
      try {
        Object i = is.readObject();

        if (i instanceof Message) {
          if (os == null) {
            os = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
          }

          Message message = (Message) i;

          Object o = message.getUser();
          if (o instanceof Member member){
            System.out.println(member.getID());
          }

          Reply replyMessage = message.getRequestType().execute(message);

          os.writeObject(replyMessage);
          os.flush();
        }
      } catch (Exception e) {
        System.out.println("Exc in ServerThread");

        try {
          if (os != null)
            os.close();

          is.close();
          socket.close();
        } catch (Exception ex) {
          System.out.println("Failed closing streams");
        }
        break;
      }
    }
  }
}
