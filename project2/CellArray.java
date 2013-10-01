public class CellArray {
    private int[] nextCells;
    private int[] currentCells;
    private int[] update;
    private int generation;

    public CellArray(int[] update, int[] currentCells) {
        this.update = update;
        this.currentCells = currentCells;
        nextCells = new int[currentCells.length];
    }

    int getPopulation() {
        int count = 0;
        for (int i : currentCells) {
            count += i;
        }
        return count;
    }

    void swapArrays() {
        int[] temp = currentCells;
        currentCells = nextCells;
        nextCells = temp;
    }
    
    int getNextValue(int index) {
        int left = currentCells[(index - 1 + currentCells.length) % currentCells.length];
        int center = currentCells[index];
        int right = currentCells[(index + 1) % currentCells.length];
        int updateIndex = (left << 2) + (center << 1) + right;
        return update[updateIndex];
    }

    void setIndex(int index, int value) {
        nextCells[index] = value;
    }
}
