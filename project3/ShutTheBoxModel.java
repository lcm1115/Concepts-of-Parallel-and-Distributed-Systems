import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ShutTheBoxModel implements ViewListener {
    private boolean[] tiles;
    private int[] dice;
    private String[] scores;
    private int myTurn;
    private String[] players;
    private BufferedReader in;
    private BufferedWriter out;
    private ModelListener modelListener;
    private Socket socket;

    public ShutTheBoxModel(Socket socket, String name) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
        sendMessage("join " + name);
        dice = new int[2];
        tiles = new boolean[9];
        scores = new String[]{ "", "" };
        players = new String[2];
    }

    public void start() {
        new ReaderThread().start();
    }

    public void setModelListener(ModelListener modelListener) {
        this.modelListener = modelListener;
    }

    public void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void roll() {
        sendMessage("roll");
    }

    public void done() {
        sendMessage("done");
    }

    public void toggleTile(int i) {
        tiles[i] = !tiles[i];
        if (tiles[i]) {
            sendMessage("tile " + (i + 1) + " up");
        } else {
            sendMessage("tile " + (i + 1) + " down");
        }
        modelListener.setTile(i + 1, tiles[i]);
    }

    public void quit() {
        sendMessage("quit");
    }

    public String scoreString(String winner) {
        String scoreString = players[0] + scores[0] + " -- " +
                             players[1] + scores[1];
        if (winner != null) {
            scoreString += " -- " + winner;
        }
        return scoreString;
    }

    private class ReaderThread extends Thread {
        public void run() {
            try {
                while (true) {
                    String response = in.readLine();
                    String[] tokens = response.split(" ");
                    String type = tokens[0];
                    if (type.equals("quit")) {
                        out.write("quit");
                        modelListener.quit();
                        break;
                    } else if (type.equals("dice")) {
                        dice[0] = Integer.parseInt(tokens[1]);
                        dice[1] = Integer.parseInt(tokens[2]);
                        modelListener.setDie(0, dice[0]);
                        modelListener.setDie(1, dice[1]);
                    } else if (type.equals("joined")) {
                        players[0] = tokens[1];
                        players[1] = tokens[2];
                        myTurn = Integer.parseInt(tokens[3]);
                        modelListener.setMessage(
                                players[0] + " -- " + players[1]);
                    } else if (type.equals("turn")) {
                        int turn = Integer.parseInt(tokens[1]);
                        if (turn == 1) {
                            scores[0] = "";
                            scores[1] = "";
                        }
                        for (int i = 0; i < 9; ++i) {
                            if (turn == myTurn) {
                                sendMessage("tile " + (i + 1) + " up");
                            }
                            modelListener.setTile(i + 1, true);
                            tiles[i] = true;
                        }
                        modelListener.enableButtons(turn == myTurn);
                    } else if (type.equals("tile")) {
                        int index = Integer.parseInt(tokens[1]) - 1;
                        boolean up = tokens[2].equals("up");
                        tiles[index] = up;
                        modelListener.setTile(index + 1, up);
                    } else if (type.equals("score")) {
                        int index = Integer.parseInt(tokens[1]) - 1;
                        scores[index] = " " + tokens[2];
                        modelListener.setMessage(scoreString(null));
                    } else if (type.equals("win")) {
                        String winner = null;
                        switch (Integer.parseInt(tokens[1])) {
                            case 0:
                                winner = "Tie!";
                                break;
                            case 1:
                                winner = players[0] + " wins!";
                                break;
                            case 2:
                                winner = players[1] + " wins!";
                                break;
                        }
                        modelListener.setMessage(scoreString(winner));
                    }
                }
            } catch (IOException e) {
            }
        }
    }
}
