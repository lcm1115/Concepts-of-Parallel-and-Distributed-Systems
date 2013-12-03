/**
 * Represents a clone of the server side model for a game of ShutTheBox.
 *
 * @author Liam Morris (lcm1115)
 */
public class ShutTheBoxModelClone implements ModelClone {
    /**
     * The state of the tiles.
     */
    private boolean[] tiles;

    /**
     * Current values on the dice.
     */
    private int[] dice;

    /**
     * Current scores for players.
     */
    private String[] scores;

    /**
     * Turn for this player.
     */
    private int myTurn;

    /**
     * Player names.
     */
    private String[] players;

    /**
     * Client side model listener to get updates on events.
     */
    private ModelListener modelListener;

    /**
     * Message in game.
     */
    private String message;

    /**
     * Constructor for ShutTheBoxModelClone.
     */
    public ShutTheBoxModelClone() {
        tiles = new boolean[9];
        dice = new int[2];
        scores = new String[]{ "", "" };
        players = new String[2];
    }

    /**
     * Mutator method for modelListener.
     *
     * @param modelListener the ModelListener for this clone.
     */
    public void setModelListener(ModelListener modelListener) {
        this.modelListener = modelListener;
    }

    /**
     * Retrieves state of specific tile.
     *
     * @param i index of tile being retrieved
     *
     * @return boolean representing state of tile
     */
    public boolean getTile(int i) {
        return tiles[i];
    }

    /**
     * Set state of specific tile.
     *
     * @param i index of tile being set
     * @param up state to set tile to
     */
    public void setTile(int i, boolean up) {
        tiles[i] = up;
        modelListener.setTile(i + 1, up);
    }

    /**
     * Retrieve values of dice.
     *
     * @return array representing values of dice
     */
    public int[] getDice() {
        return dice;
    }

    /**
     * Set value of specific die.
     *
     * @param i index of die being set
     * @param value value to assign to die
     */
    public void setDie(int i, int value) {
        dice[i] = value;
        modelListener.setDie(i, value);
    }

    /**
     * Retrieve player scores.
     *
     * @return array of player scores
     */
    public String[] getScores() {
        return scores;
    }

    /**
     * Set score for player
     *
     * @param i index of player whose score is being set
     * @param score score for player
     */
    public void setScore(int i, String score) {
        scores[i] = score;
    }

    /**
     * Return turn number for this client.
     *
     * @return client's turn number
     */
    public int getMyTurn() {
        return myTurn;
    }

    /**
     * Set turn number for this client.
     *
     * @param i client's turn number
     */
    public void setMyTurn(int i) {
        myTurn = i;
    }

    /**
     * Retrieve player names.
     *
     * @return array of player names
     */
    public String[] getPlayers() {
        return players;
    }

    /**
     * Set name for specific player.
     *
     * @param i index of player
     * @param player name of player
     */
    public void setPlayer(int i, String player) {
        players[i] = player;
    }

    /**
     * Set message for client.
     *
     * @param message message to display
     */
    public void setMessage(String message) {
        this.message = message;
        modelListener.setMessage(message);
    }

    /**
     * Set 'enabled' status of all buttons.
     *
     * @param enabled whether buttons should be enabled or disabled
     */
    public void enableButtons(boolean enabled) {
        for (int i = 0; i < 9; ++i) {
            tiles[i] = true;
            modelListener.setTile(i + 1, true);
        }
        modelListener.enableButtons(enabled);
    }

    /**
     * Quit UI.
     */
    public void quit() {
        modelListener.quit();
    }
}
