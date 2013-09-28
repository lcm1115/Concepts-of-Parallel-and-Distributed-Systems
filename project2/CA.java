import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import java.lang.StringBuilder;
import java.util.Arrays;

public class CA extends Task {
    private int[] nextCells;
    private int[] currentCells;
    private int[] update;
    private int finalPop;
    private int largestPop;
    private int largestPopGen;
    private int smallestPopGen;
    private int smallestPop;
    
    public void main(String[] args) {
        if (args.length < 4) {
            System.err.println("usage: java CA <rule> <N> <S> <index> ...");
        } else if (args[0].length() != 8) {
            System.err.println("error - rule must be eight characters long");
        } else if (args[0].replaceAll("0", "").replaceAll("1", "").length() > 0) {
            System.err.println("error - rule must contain only 0 and 1 characters");
        } else if (Integer.parseInt(args[1]) <= 0) {
            System.err.println("error - N must be greater than zero");
        } else if (Integer.parseInt(args[2]) <= 0) {
            System.err.println("error - S must be greater than zero");
        } else {
            // Get update rule.
            update = new int[8];
            for (int i = 0; i < args[0].length(); ++i) {
                update[i] = Character.getNumericValue(args[0].charAt(i));
            }

            // Construct initial cell arrays.
            currentCells = new int[Integer.parseInt(args[1])];
            nextCells = new int[Integer.parseInt(args[1])];
            for (int i = 3; i < args.length; ++i) {
                currentCells[Integer.parseInt(args[i])] = 1;
            }
            
            int steps = Integer.parseInt(args[2]);
            largestPop = countLive();
            smallestPop = countLive();

            runGenerations(steps);
            System.out.println("Smallest popcount: " + smallestPop + " at step " + smallestPopGen);
            System.out.println("Largest popcount: " + largestPop + " at step " + largestPopGen);
            System.out.println("Final popcount: " + finalPop + " at step " + steps);
        }
    }

    int countLive() {
        int count = 0;
        for (int i : currentCells) {
            count += i;
        }
        return count;
    }

    void runGenerations(int steps) {
        for (int i = 0; i < steps; ++i) {
            int numLive = countLive();
            if (numLive > largestPop) {
                largestPop = numLive;
                largestPopGen = i;
            }

            if (numLive < smallestPop) {
                smallestPop = numLive;
                smallestPopGen = i;
            }

            parallelFor(0, currentCells.length - 1).exec(new Loop() {
                public void run(int i) {
                    synchronized(nextCells) {
                        nextCells[i] = getNextValue(i);
                    }
                }
            });
            int[] temp = currentCells;
            currentCells = nextCells;
            nextCells = temp;
        }
        finalPop = countLive();
    }

    void printGeneration(int gen, int numLive) {
        System.out.print(gen + "\t");
        for (int i = 0; i < currentCells.length; ++i) {
            System.out.print(currentCells[i]);
        }
        System.out.print("\t" + numLive + "\n");
    }

    int getNextValue(int index) {
        int left = currentCells[(index - 1 + currentCells.length) % currentCells.length];
        int center = currentCells[index];
        int right = currentCells[(index + 1) % currentCells.length];
        int updateIndex = (left << 2) + (center << 1) + right;
        return update[updateIndex];
    }
}
