import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ViewProxy extends Thread {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ShutTheBoxSession session;
    private Socket socket;
    private int playerTurn;

    public ViewProxy(Socket socket, ShutTheBoxSession session)
        throws IOException {
        super();
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        this.session = session;
        this.socket = socket;
        start();
    }

    public void run() {
        while (true) {
            try {
                Message m = (Message) in.readObject();
                System.out.println(m);
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
            } catch (SocketException e) {
                System.out.println("Player quit");
                break;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
