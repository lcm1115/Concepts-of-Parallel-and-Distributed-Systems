/**
 *  Defines the interface for the clone of a model for a game of Shut The Box.
 *
 *  @author Liam Morris (lcm1115)
 */
public interface ModelClone {
    /**
     * Retrieves state of specific tile.
     *
     * @param i index of tile being retrieved
     *
     * @return boolean representing state of tile
     */
    public boolean getTile(int i);

    /**
     * Set state of specific tile.
     *
     * @param i index of tile being set
     * @param up state to set tile to
     */
    public void setTile(int i, boolean up);

    /**
     * Retrieve values of dice.
     *
     * @return array representing values of dice
     */
    public int[] getDice();

    /**
     * Set value of specific die.
     *
     * @param i index of die being set
     * @param value value to assign to die
     */
    public void setDie(int i, int value);

    /**
     * Retrieve player scores.
     *
     * @return array of player scores
     */
    public String[] getScores();

    /**
     * Set score for player
     *
     * @param i index of player whose score is being set
     * @param score score for player
     */
    public void setScore(int i, String score);

    /**
     * Return turn number for this client.
     *
     * @return client's turn number
     */
    public int getMyTurn();

    /**
     * Set turn number for this client.
     *
     * @param i client's turn number
     */
    public void setMyTurn(int i);

    /**
     * Retrieve player names.
     *
     * @return array of player names
     */
    public String[] getPlayers();

    /**
     * Set name for specific player.
     *
     * @param i index of player
     * @param player name of player
     */
    public void setPlayer(int i, String player);

    /**
     * Set message for client.
     *
     * @param message message to display
     */
    public void setMessage(String message);

    /**
     * Set 'enabled' status of all buttons.
     *
     * @param enabled whether buttons should be enabled or disabled
     */
    public void enableButtons(boolean enabled);

    /**
     * Quit UI.
     */
    public void quit();
}
