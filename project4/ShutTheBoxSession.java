import java.util.Random;

public class ShutTheBoxSession {
    private boolean[] tiles;
    private int sessionID;
    private int[] scores;
    private int turn;
    private Random diceRoller;
    private String[] players;
    private ViewProxy[] clients;

    public ShutTheBoxSession(int sessionID) {
        this.sessionID = sessionID;
        scores = new int[2];
        clients = new ViewProxy[2];
        tiles = new boolean[9];
        players = new String[2];
        for (int i = 0; i < 9; ++i) {
            tiles[i] = true;
        }
        diceRoller = new Random();
    }

    public synchronized int addPlayer(ViewProxy proxy, String playerName) {
        if (clients[0] == null) {
            clients[0] = proxy;
            players[0] = playerName;
            return 1;
        } else if (clients[1] == null) {
            clients[1] = proxy;
            players[1] = playerName;
            turn = 1;
            clients[0].sendMessage(new Message(
                        "joined",
                        new String[]{ players[0], players[1], "1" }));
            clients[1].sendMessage(new Message(
                        "joined",
                        new String[]{ players[0], players[1], "2" }));
            clients[0].sendMessage(
                    new Message("turn", new String[]{ "1" }));
            clients[1].sendMessage(
                    new Message("turn", new String[]{ "1" }));
            return 2;
        }

        return 0;
    }

    public synchronized void flipTile(
            int playerTurn, int tile, boolean up) {
        if (turn != playerTurn) {
            System.out.println("Unexpected message");
            return;
        }

        tiles[tile - 1] = up;
        String status = up ? "up" : "down";
        clients[0].sendMessage(new Message(
                    "tile",
                    new String[]{ Integer.toString(tile), status }));
        clients[1].sendMessage(new Message(
                    "tile",
                    new String[]{ Integer.toString(tile), status }));
    }

    public synchronized void roll(int playerTurn) {
        if (turn != playerTurn) {
            System.out.println("Unexpected message");
            return;
        }

        String die1 = Integer.toString(diceRoller.nextInt(6) + 1);
        String die2 = Integer.toString(diceRoller.nextInt(6) + 1);
        clients[0].sendMessage(new Message(
                    "dice", new String[]{ die1, die2 }));
        clients[1].sendMessage(new Message(
                    "dice", new String[]{ die1, die2 }));
    }

    public synchronized void done(int playerTurn) {
        if (turn != playerTurn) {
            System.out.println("Unexpected message");
            return;
        }

        // Compute score and send to clients.
        int score = 0;
        for (int i = 0; i < 9; ++i) {
            if (tiles[i]) {
                score += i + 1;
            }

            // Reset tile
            tiles[i] = true;
        }

        clients[0].sendMessage(new Message(
                    "score",
                    new String[]{ Integer.toString(turn),
                                  Integer.toString(score) }));
        clients[1].sendMessage(new Message(
                    "score",
                    new String[]{ Integer.toString(turn),
                                  Integer.toString(score) }));

        // Determine winner (if necessary)
        if (turn == 1) {
            scores[0] = score;
            turn = 2;
        } else {
            scores[1] = score;
            turn = 1;
            String winner = "0";
            if (scores[0] < scores[1]) {
                winner = "1";
            } else if (scores[0] > scores[1]) {
                winner = "2";
            }
            clients[0].sendMessage(new Message(
                        "win",
                        new String[]{ winner }));
            clients[1].sendMessage(new Message(
                        "win",
                        new String[]{ winner }));
        }

        // Update turn
        clients[0].sendMessage(new Message(
                    "turn",
                    new String[]{ Integer.toString(turn) }));
        clients[1].sendMessage(new Message(
                    "turn",
                    new String[]{ Integer.toString(turn) }));
    }

    public void quit() {
        for (int i = 0; i < 2; ++i) {
            if (clients[i] != null) {
                clients[i].sendMessage(new Message("quit", null));
            }
        }

        ShutTheBoxServer.deleteShutTheBoxSession(sessionID);
    }
}
