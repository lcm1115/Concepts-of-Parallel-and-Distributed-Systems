public class ShutTheBoxModelClone implements ModelClone {
    private boolean[] tiles;
    private int[] dice;
    private String[] scores;
    private int myTurn;
    private String[] players;
    private ModelListener modelListener;
    private String message;

    public ShutTheBoxModelClone() {
        tiles = new boolean[9];
        dice = new int[2];
        scores = new String[]{ "", "" };
        players = new String[2];
    }

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
        modelListener.enableButtons(enabled);
    }

    /**
     * Quit UI.
     */
    public void quit() {
        modelListener.quit();
    }
}
