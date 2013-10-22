import java.awt.event.ActionEvent;
import java.net.Socket;

public class ShutTheBox {
    private ShutTheBoxModel model;
    private ShutTheBoxUI ui;

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println(
                    "usage: java ShutTheBox <host> <port> <playername>");
        } else {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            String name = args[2];
            ShutTheBox stb = new ShutTheBox(host, port, name);
        }
    }

    public ShutTheBox(String host, int port, String name) {
        try {
            model = new ShutTheBoxModel(new Socket(host, port), name);
            ui = new ShutTheBoxUI();
            model.setModelListener(ui);
            ui.setViewListener(model);
            model.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
