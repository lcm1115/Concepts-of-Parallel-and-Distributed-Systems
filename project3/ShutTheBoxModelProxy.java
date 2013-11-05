import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ShutTheBoxModelProxy implements ViewListener {
    private BufferedReader in;
    private BufferedWriter out;
    private ModelClone modelClone;
    private Socket socket;

    public ShutTheBoxModelProxy(Socket socket, String name) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
        sendMessage("join " + name);
    }

    public void start() {
        new ReaderThread().start();
    }

    public void setModelClone(ModelClone modelClone) {
        this.modelClone = modelClone;
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

    public void quit() {
        sendMessage("quit");
    }

    public void toggleTile(int i) {
        boolean up = modelClone.getTile(i);
        String state = up ? " down" : " up";
        sendMessage("tile " + (i + 1) + state);
    }

    public String scoreString(String winner) {
        String[] players = modelClone.getPlayers();
        String[] scores = modelClone.getScores();
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
                        quit();
                        break;
                    } else if (type.equals("dice")) {
                        modelClone.setDie(0, Integer.parseInt(tokens[1]));
                        modelClone.setDie(1, Integer.parseInt(tokens[2]));
                    } else if (type.equals("joined")) {
                        modelClone.setPlayer(0, tokens[1]);
                        modelClone.setPlayer(1, tokens[2]);
                        modelClone.setMyTurn(Integer.parseInt(tokens[3]));
                        modelClone.setMessage(
                                tokens[1] + " -- " + tokens[2]);
                    } else if (type.equals("turn")) {
                        int turn = Integer.parseInt(tokens[1]);
                        if (turn == 1) {
                            modelClone.setScore(0, "");
                            modelClone.setScore(1, "");
                        }
                        for (int i = 0; i < 9; ++i) {
                            if (turn == modelClone.getMyTurn()) {
                                sendMessage("tile " + (i + 1) + " up");
                            }
                            modelClone.setTile(i, true);
                        }
                        modelClone.enableButtons(turn == modelClone.getMyTurn());
                    } else if (type.equals("tile")) {
                        int index = Integer.parseInt(tokens[1]) - 1;
                        boolean up = tokens[2].equals("up");
                        modelClone.setTile(index, up);
                    } else if (type.equals("score")) {
                        int index = Integer.parseInt(tokens[1]) - 1;
                        modelClone.setScore(index, " " + tokens[2]);
                        modelClone.setMessage(scoreString(null));
                    } else if (type.equals("win")) {
                        String winner = null;
                        String[] players = modelClone.getPlayers();
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
                        modelClone.setMessage(scoreString(winner));
                    }
                }
            } catch (IOException e) {
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
