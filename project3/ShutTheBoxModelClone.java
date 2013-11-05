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

    public boolean[] getTiles() {
        return tiles;
    }

    public boolean getTile(int i) {
        return tiles[i];
    }

    public void setTile(int i, boolean up) {
        tiles[i] = up;
        modelListener.setTile(i + 1, up);
    }

    public int[] getDice() {
        return dice;
    }

    public int getDie(int i) {
        return dice[i];
    }

    public void setDie(int i, int value) {
        dice[i] = value;
        modelListener.setDie(i, value);
    }

    public String[] getScores() {
        return scores;
    }

    public String getScore(int i) {
        return scores[i];
    }

    public void setScore(int i, String score) {
        scores[i] = score;
    }

    public int getMyTurn() {
        return myTurn;
    }

    public void setMyTurn(int i) {
        myTurn = i;
    }

    public String[] getPlayers() {
        return players;
    }

    public String getPlayer(int i) {
        return players[i];
    }

    public void setPlayer(int i, String player) {
        players[i] = player;
    }

    public void setMessage(String message) {
        this.message = message;
        modelListener.setMessage(message);
        System.out.println("Message: " + message);
    }

    public void enableButtons(boolean enabled) {
        modelListener.enableButtons(enabled);
    }
}
