import java.awt.event.ActionEvent;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *  Main client for network game of Shut The Box.
 *
 *  @author Liam Morris (lcm1115)
 */
public class ShutTheBox {
    public static void main(String[] args) throws Exception {
        // Verify correct arguments.
        if (args.length < 3) {
            System.err.println(
                    "usage: java ShutTheBox <host> <port> <playername>");
        } else {
            // Initialize socket.
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));
            String name = args[2];

            // Create model clone, view, and model proxy.
            ShutTheBoxModelClone model = new ShutTheBoxModelClone();
            ShutTheBoxUI view = new ShutTheBoxUI();
            ShutTheBoxModelProxy proxy = new ShutTheBoxModelProxy(socket, name);
            model.setModelListener(view);
            view.setViewListener(proxy);
            proxy.setModelClone(model);

            // Begin receiving data.
            proxy.start();
        }
    }
}
