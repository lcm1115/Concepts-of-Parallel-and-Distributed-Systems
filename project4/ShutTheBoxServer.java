import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ShutTheBoxServer {
   private static HashMap<Integer, ShutTheBoxSession> sessions;
    private static int currentID;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java ShutTheBoxServer <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        ServerSocket serversocket = new ServerSocket();
        serversocket.bind(new InetSocketAddress(host, port));
        sessions = new HashMap<Integer, ShutTheBoxSession>();
        currentID = 1;

        while (true) {
            Socket socket = serversocket.accept();
            ViewProxy client = new ViewProxy(socket, getNewShutTheBoxSession());
        }
    }

    public static synchronized ShutTheBoxSession getNewShutTheBoxSession() {
        ShutTheBoxSession session = sessions.get(currentID);
        if (session == null) {
            session = new ShutTheBoxSession(currentID);
            sessions.put(currentID, session);
        } else {
            ++currentID;
        }
        return session;
    }

    public static synchronized void deleteShutTheBoxSession(int sessionID) {
        sessions.remove(sessionID);
    }
}
