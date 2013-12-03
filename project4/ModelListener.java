/**
 *  Defines the interface for listening to changes in model for a game of Shut
 *  The Box.
 *
 *  @author Liam Morris (lcm1115)
 */
public interface ModelListener {

    /**
     * Set the state of the given tile.
     *
     * @param  i   Tile index (1..9).
     */
    public void setTile(int i, boolean up);

    /**
     * Set the value of the given die.
     *
     * @param  i      Die index (0..1).
     * @param  value  Die value (1..6).
     */
    public void setDie(int i, int value);

    /**
     * Set the message displayed in the message area.
     *
     * @param  msg  Message.
     */
    public void setMessage(String message);

    /**
     * Enable or disable all the buttons.
     *
     * @param  enabled  True to enable all the buttons, false to disable them.
     */
    public void enableButtons(boolean enabled);

    /**
     * Quit UI.
     */
    public void quit();
}
