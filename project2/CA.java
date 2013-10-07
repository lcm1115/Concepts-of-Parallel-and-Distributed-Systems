import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;

/**
 * This class contains the main function for a representation of a cellular
 * automaton. Given an initial array of cells, an update rule, and a number of
 * steps to run, the program will print out the smallest, largest, and final
 * populations and the steps at which they occurred.
 *
 * @author Liam Morris (lcm1115)
 */
public class CA extends Task {
    // Instance of class that contains the actual data.
    private CellArray ca;

    /**
     * Main method for running cellular automaton.
     *
     * @param args command line args for program.
     *             First arg should be an string of 0's and 1's (length 8) which
     *               represents the update rule.
     *             Second arg should be the size of the cell array.
     *             Third arg should be the number of steps to run.
     *             All following args should be indices that start with a live
     *               cell.
     */
    public void main(String[] args) {
        // Check that usage is correct.
        if (args.length < 4) {
            System.err.println(
                    "usage: java CA <rule> <N> <S> <index> ...");
        } else if (args[0].length() != 8) {
            System.err.println(
                    "error - rule must be eight characters long");
        } else if (
                args[0].replaceAll("0", "").replaceAll("1", "").length() > 0) {
            System.err.println(
                    "error - rule must contain only 0 and 1 characters");
        } else if (Integer.parseInt(args[1]) <= 0) {
            System.err.println("error - N must be greater than zero");
        } else if (Integer.parseInt(args[2]) <= 0) {
            System.err.println("error - S must be greater than zero");
        } else {
            // Initialize all values.
            int largestPop, smallestPop, finalPop;
            int largestPopGen, smallestPopGen;
            largestPop = smallestPop = finalPop = 0;
            largestPopGen = smallestPopGen = 0;

            // Get update rule.
            int[] update = new int[8];
            for (int i = 0; i < args[0].length(); ++i) {
                update[i] = Character.getNumericValue(args[0].charAt(i));
            }

            // Construct initial cell array.
            int[] currentCells = new int[Integer.parseInt(args[1])];
            for (int i = 3; i < args.length; ++i) {
                int cellIndex = Integer.parseInt(args[i]);
                if (cellIndex > currentCells.length) {
                    System.err.println("error - initial live cell index larger than array size");
                    System.exit(1);
                }
                currentCells[cellIndex] = 1;
            }

            ca = new CellArray(update, currentCells);
            largestPop = smallestPop = ca.getPopulation();
            
            // Run steps on the CellArray.
            int steps = Integer.parseInt(args[2]);
            for (int i = 0; i < steps; ++i) {
                parallelFor(0, currentCells.length - 1).exec(new Loop() {
                    public void run(int i) {
                        int nextValue = ca.getNextValue(i);
                        synchronized (ca) {
                            ca.setIndex(i, nextValue);
                        }
                    }
                });

                // After each step, store the result in the CellArray and check
                // if either largestPop or smallestPop should be changed.
                ca.swapArrays();
                int pop = ca.getPopulation();
                if (pop > largestPop) {
                    largestPop = pop;
                    largestPopGen = i + 1;
                } else if (pop < smallestPop) {
                    smallestPop = pop;
                    smallestPopGen = i + 1;
                }
            }

            finalPop = ca.getPopulation();

            System.out.println("Smallest popcount: " + smallestPop +
                               " at step " + smallestPopGen);
            System.out.println("Largest popcount: " + largestPop +
                               " at step " + largestPopGen);
            System.out.println("Final popcount: " + finalPop + 
                               " at step " + steps);
        }
    }
}
