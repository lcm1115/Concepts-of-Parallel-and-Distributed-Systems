import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Server for running games of Shut The Box.
 *
 * @author Liam Morris (lcm1115)
 */
public class ShutTheBoxServer {
    /**
     * Map for storing sessions of Shut The Box.
     */
    private static HashMap<Integer, ShutTheBoxSession> sessions;

    /**
     * currentID of game to be created.
     */
    private static int currentID;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java ShutTheBoxServer <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        // Create server socket, initialize map.
        ServerSocket serversocket = new ServerSocket();
        serversocket.bind(new InetSocketAddress(host, port));
        sessions = new HashMap<Integer, ShutTheBoxSession>();
        currentID = 1;

        // Begin accepting connections.
        while (true) {
            Socket socket = serversocket.accept();
            ViewProxy client = new ViewProxy(socket, getNewShutTheBoxSession());
        }
    }

    /**
     * Create new ShutTheBoxSession (if necessary) or return existing session
     * with one player in it.
     *
     * @return a Shut The Box Session with 0 or 1 players currently in it
     */
    public static synchronized ShutTheBoxSession getNewShutTheBoxSession() {
        // Check if session is already in use.
        ShutTheBoxSession session = sessions.get(currentID);
        if (session == null) {
            // If session does not exist, create it.
            session = new ShutTheBoxSession(currentID);
            sessions.put(currentID, session);
        } else {
            // If session does exist, increment ID for next session.
            ++currentID;
        }
        return session;
    }

    /**
     * Close session of Shut The Box.
     *
     * @param sessionID the ID of the session to be closed.
     */
    public static synchronized void deleteShutTheBoxSession(int sessionID) {
        sessions.remove(sessionID);
    }
}
