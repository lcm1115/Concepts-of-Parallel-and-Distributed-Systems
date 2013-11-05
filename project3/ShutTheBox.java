import java.awt.event.ActionEvent;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ShutTheBox {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println(
                    "usage: java ShutTheBox <host> <port> <playername>");
        } else {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));
            String name = args[2];
            ShutTheBoxModelClone model = new ShutTheBoxModelClone();
            ShutTheBoxUI view = new ShutTheBoxUI();
            ShutTheBoxModelProxy proxy = new ShutTheBoxModelProxy(socket, name);
            model.setModelListener(view);
            view.setViewListener(proxy);
            proxy.setModelClone(model);
            proxy.start();
        }
    }
}
