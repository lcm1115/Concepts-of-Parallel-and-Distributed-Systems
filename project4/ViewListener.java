/**
 * Class for listening to view events in a game of Shut The Box.
 *
 * @author Liam Morris (lcm1115)
 */
public interface ViewListener {
    /**
     * Signals that a roll should be performed.
     */
    public void roll();

    /**
     * Signals that a player has finished a turn.
     */
    public void done();

    /**
     * Signals that a player has quit the game.
     */
    public void quit();

    /**
     * Signals that a tile has been flipped.
     */
    public void toggleTile(int i);
}
