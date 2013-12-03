import java.util.Random;

/**
 * This class represents the state of a game of Shut The Box.
 *
 * @author Liam Morris (lcm1115)
 */
public class ShutTheBoxModel implements ViewListener {
    /**
     * Current values of dice.
     */
    private int[] dice;

    /**
     * Current player scores.
     */
    private int[] scores;

    /**
     * Current turn.
     */
    private int turn;

    /**
     * Current state of tiles.
     */
    private boolean[] tiles;

    /**
     * RNG for generating dice rolls.
     */
    private Random rand;

    /**
     * Player names.
     */
    private String player1, player2;

    /**
     * Constructor for ShutTheBoxModel.
     *
     * @param player1 player 1's name
     * @param player2 player 2's name
     */
    public ShutTheBoxModel(String player1, String player2) {
        dice = new int[2];
        dice[0] = 1;
        dice[1] = 1;
        tiles = new boolean[9];
        for (int i = 0; i < 9; ++i) {
            tiles[i] = true;
        }
        scores = new int[2];
        this.player1 = player1;
        this.player2 = player2;
        rand = new Random();
    }

    /**
     * Signals that a roll should be performed.
     */
    public void roll() {
        dice[0] = rand.nextInt(6) + 1;
        dice[1] = rand.nextInt(6) + 1;
    }

    /**
     * Signals that a player has finished a turn.
     */
    public void done() {
        int total = 0;
        for (int i = 0; i < 9; ++i) {
            if (tiles[i]) {
                total += i + 1;
            }
        }
        scores[turn] = total;
        ++turn;
        turn %= 2;
    }

    /**
     * Signals that a player has quit the game.
     */
    public void quit() {
        // No implementation; only needed to satisfy interface.
    }

    /**
     * Signals that a tile has been flipped.
     */
    public void toggleTile(int i) {
        tiles[i - 1] = !tiles[i - 1];
    }
}
