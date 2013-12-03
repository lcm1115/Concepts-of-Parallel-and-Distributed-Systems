import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Class for listening to events on a view of Shut The Box.
 *
 * @author Liam Morris (lcm1115)
 */
public class ViewProxy extends Thread {
    /**
     * Stream for reading in Objects.
     */
    private ObjectInputStream in;

    /**
     * Stream for writing out Objects.
     */
    private ObjectOutputStream out;

    /**
     * Session associated with this instance.
     */
    private ShutTheBoxSession session;

    /**
     * Socket connected to client.
     */
    private Socket socket;

    /**
     * Turn for this player.
     */
    private int playerTurn;

    /**
     * Constructor for ViewProxy.
     *
     * @param socket the client's socket
     * @param session the client's session.
     */
    public ViewProxy(Socket socket, ShutTheBoxSession session)
        throws IOException {
        super();
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        this.session = session;
        this.socket = socket;
        start();
    }

    public void run() {
        while (true) {
            try {
                // Read in message
                Message m = (Message) in.readObject();

                // Determine type and act accordingly.
                if (m.getType().equals("quit")) {
                    session.quit();
                } else if (m.getType().equals("roll")) {
                    session.roll(playerTurn);
                } else if (m.getType().equals("tile")) {
                    if (m.getArgs() == null || m.getArgs().length != 2) {
                        System.out.println("Bad message");
                    } else {
                        int index = Integer.parseInt(m.getArgs()[0]);
                        if (index < 1 || index > 9) { 
                            System.out.println("Bad message");
                        }
                        boolean state = m.getArgs()[1].equals("up");
                        session.flipTile(playerTurn, index, state);
                    }
                } else if (m.getType().equals("join")) {
                    if (m.getArgs() == null || m.getArgs().length != 1) {
                        System.out.println("Bad message");
                    } else {
                        playerTurn =
                            session.addPlayer(this, m.getArgs()[0]);
                    }
                } else if (m.getType().equals("done")) {
                    session.done(playerTurn);
                }
            } catch (EOFException e) {
                System.out.println("Player quit");
                break;
            } catch (SocketException e) {
                System.out.println("Player quit");
                break;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }


    /**
     * Sends message to client.
     *
     * @param message the message to be sent
     */
    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
