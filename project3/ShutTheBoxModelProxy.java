import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *  Class for communicating with ShutTheBoxServer. Receives data from server and
 *  updates corresponding model and view when necessary.
 *
 *  @author Liam Morris (lcm1115)
 */
public class ShutTheBoxModelProxy implements ViewListener {
    /**
     * Input reader for network socket.
     */
    private BufferedReader in;

    /**
     * Output writer for network socket.
     */
    private BufferedWriter out;

    /**
     * Client side model representation.
     */
    private ModelClone modelClone;

    /**
     * Network socket for communicating with server.
     */
    private Socket socket;

    /**
     * Constructor for ShutTheBoxModelProxy.
     *
     * @param socket socket connected to server
     * @param name player name for this client
     *
     * @exception IOException thrown if I/O error occurs
     */
    public ShutTheBoxModelProxy(Socket socket, String name) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));

        // Send join command.
        sendMessage("join " + name);
    }

    /**
     * Starts reader thread and begins receiving data from server.
     */
    public void start() {
        new ReaderThread().start();
    }

    /**
     * Sets ModelClone for this client.
     *
     * @param modelClone instance of ModelClone associated with this client
     */
    public void setModelClone(ModelClone modelClone) {
        this.modelClone = modelClone;
    }

    /**
     * Sends message to server.
     *
     * @param message the message to be sent
     */
    public void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends 'roll' command to the server.
     */
    public void roll() {
        sendMessage("roll");
    }

    /**
     * Sends 'done' command to the server.
     */
    public void done() {
        sendMessage("done");
    }

    /**
     * Sends 'quit' command to the server.
     */
    public void quit() {
        sendMessage("quit");
    }

    /**
     * Sends message to server indicating that a tile's state is changing.
     *
     * @param i index of tile that was clicked
     */
    public void toggleTile(int i) {
        boolean up = modelClone.getTile(i);
        String state = up ? " down" : " up";
        sendMessage("tile " + (i + 1) + state);
    }

    /**
     * Build score string to display to both clients once game is over.
     *
     * @param winner winner of the game (null if tie)
     *
     * @return string in format:
     *         [player1] [score] -- [player2] [score] [(player(1|2) wins!)|tie!]
     */
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

    /**
     * Receives messages from server and processes them.
     *
     * @author Liam Morris (lcm1115)
     */
    private class ReaderThread extends Thread {
        public void run() {
            try {
                while (true) {
                    // Read line and split it into tokens.
                    String response = in.readLine();
                    String[] tokens = response.split(" ");

                    // Determine type of message.
                    String type = tokens[0];
                    if (type.equals("quit")) {
                        socket.close();
                        modelClone.quit();
                        break;
                    } else if (type.equals("dice")) {
                        // Update dice values.
                        modelClone.setDie(0, Integer.parseInt(tokens[1]));
                        modelClone.setDie(1, Integer.parseInt(tokens[2]));
                    } else if (type.equals("joined")) {
                        // Set player names and turn number.
                        modelClone.setPlayer(0, tokens[1]);
                        modelClone.setPlayer(1, tokens[2]);
                        modelClone.setMyTurn(Integer.parseInt(tokens[3]));
                        modelClone.setMessage(
                                tokens[1] + " -- " + tokens[2]);
                    } else if (type.equals("turn")) {
                        // Reset scores (if needed) and reset tiles.
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

                        // Enable/disable buttons as needed.
                        modelClone.enableButtons(
                                turn == modelClone.getMyTurn());
                    } else if (type.equals("tile")) {
                        // Update state of tile.
                        int index = Integer.parseInt(tokens[1]) - 1;
                        boolean up = tokens[2].equals("up");
                        modelClone.setTile(index, up);
                    } else if (type.equals("score")) {
                        // Update score.
                        int index = Integer.parseInt(tokens[1]) - 1;
                        modelClone.setScore(index, " " + tokens[2]);
                        modelClone.setMessage(scoreString(null));
                    } else if (type.equals("win")) {
                        // Determine winner and set message accordingly.
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
