public interface ModelListener {
    public void enableButtons(boolean enabled);

    public void setTile(int i, boolean up);

    public void setDie(int i, int value);

    public void setMessage(String message);

    public void quit();
}