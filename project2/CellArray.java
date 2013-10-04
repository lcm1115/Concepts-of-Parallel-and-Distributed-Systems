/**
 * Class that is used for storing array for the cellular automaton.
 * Contains supporting functions for getting the next step of the array and then
 * updating the array.
 *
 * @author Liam Morris (lcm1115)
 */
public class CellArray {
    /**
     * Array in which the next step is stored.
     */
    private int[] nextCells;

    /**
     * Array in which the current state is tsored.
     */
    private int[] currentCells;

    /**
     * Update rule for the automaton.
     */
    private int[] update;

    /**
     * Constructor for CellArray.
     *
     * @param update update rule for automaton
     * @param currentCells initial cell array for automaton
     */
    public CellArray(int[] update, int[] currentCells) {
        this.update = update;
        this.currentCells = currentCells;
        nextCells = new int[currentCells.length];
    }

    /**
     * Counts the number of live cells in the automaton.
     *
     * @return number of live cells in the automaton
     */
    public int getPopulation() {
        int count = 0;
        for (int i : currentCells) {
            count += i;
        }
        return count;
    }

    /**
     * Swaps the arrays that store the next step and current state.
     */
    public void swapArrays() {
        int[] temp = currentCells;
        currentCells = nextCells;
        nextCells = temp;
    }
   
    /**
     * Gets the value of a given index after one step is applied to the
     * automaton.
     *
     * @param index the index for which the next value is being computed
     *
     * @return the next value (0 or 1) that the given index will have
     */
    public int getNextValue(int index) {
        // Get left, center, and right values.
        int length = currentCells.length;
        int left = currentCells[(index - 1 + length) % length];
        int center = currentCells[index];
        int right = currentCells[(index + 1) % length];

        // Get next value (based on 3-bit value from left, center, right).
        int updateIndex = (left << 2) + (center << 1) + right;
        return update[updateIndex];
    }

    /**
     * Sets the value of an index in the array for the next step to a given
     * value.
     *
     * @param index the index being updated
     * @param value the value to store in the index
     */
    public void setIndex(int index, int value) {
        nextCells[index] = value;
    }
}
