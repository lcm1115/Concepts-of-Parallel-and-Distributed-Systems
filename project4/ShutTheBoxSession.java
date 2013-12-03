import java.util.Random;

/**
 * Represents a session of a game of Shut The Box.
 * Keeps all necessary state for each game.
 *
 * @author Liam Morris (lcm1115)
 */
public class ShutTheBoxSession {
    /**
     * State of tiles.
     */
    private boolean[] tiles;

    /**
     * ID for this session.
     */
    private int sessionID;

    /**
     * Scores of each player.
     */
    private int[] scores;

    /**
     * Current turn.
     */
    private int turn;

    /**
     * RNG for dice.
     */
    private Random diceRoller;

    /**
     * Player names.
     */
    private String[] players;

    /**
     * Clients connected to this session.
     */
    private ViewProxy[] clients;

    /**
     * Constructor for ShutTheBoxSession.
     *
     * @param sessionID the ID of this session.
     */
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

    /**
     * Add player to this session.
     *
     * @param ViewProxy the client connecting to this session
     * @param playerName the name of the player connectong to this session
     *
     * @return the number of players connected to this session
     */
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

    /**
     * Sets the state of a tile.
     *
     * @param playerTurn the turn of the player issuing the command
     * @param tile the index of the tile being set
     * @param up the state of the tile
     */
    public synchronized void flipTile(
            int playerTurn, int tile, boolean up) {
        // Check player turn.
        if (turn != playerTurn) {
            System.out.println("Invalid player turn");
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

    /**
     * Rolls the dice.
     *
     * @param playerTurn the turn of the player issuing the command
     */
    public synchronized void roll(int playerTurn) {
        // Check player turn.
        if (turn != playerTurn) {
            System.out.println("Invalid player turn");
            return;
        }

        String die1 = Integer.toString(diceRoller.nextInt(6) + 1);
        String die2 = Integer.toString(diceRoller.nextInt(6) + 1);
        clients[0].sendMessage(new Message(
                    "dice", new String[]{ die1, die2 }));
        clients[1].sendMessage(new Message(
                    "dice", new String[]{ die1, die2 }));
    }

    /**
     * Lets server know that a player has finished a turn.
     *
     * @param playerTurn the turn of the player issuing the command
     */
    public synchronized void done(int playerTurn) {
        // Check player turn.
        if (turn != playerTurn) {
            System.out.println("Invalid player turn");
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

    /**
     * Close session and alert players.
     */
    public void quit() {
        for (int i = 0; i < 2; ++i) {
            if (clients[i] != null) {
                clients[i].sendMessage(new Message("quit", null));
            }
        }

        ShutTheBoxServer.deleteShutTheBoxSession(sessionID);
    }
}
