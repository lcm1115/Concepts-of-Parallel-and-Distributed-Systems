public interface ModelClone {
    public boolean[] getTiles();
    public boolean getTile(int i);
    public void setTile(int i, boolean up);
    public int[] getDice();
    public int getDie(int i);
    public void setDie(int i, int value);
    public String[] getScores();
    public String getScore(int i);
    public void setScore(int i, String score);
    public int getMyTurn();
    public void setMyTurn(int i);
    public String[] getPlayers();
    public String getPlayer(int i);
    public void setPlayer(int i, String player);
    public void setMessage(String message);
    public void enableButtons(boolean enabled);
}
