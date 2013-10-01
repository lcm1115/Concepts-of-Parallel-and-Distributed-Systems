import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;

public class CA extends Task {
    private CellArray ca;
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
            int largestPop, smallestPop, largestPopGen, smallestPopGen, finalPop;
            largestPop = smallestPop = largestPopGen = smallestPopGen = finalPop = 0;
            // Get update rule.
            int[] update = new int[8];
            for (int i = 0; i < args[0].length(); ++i) {
                update[i] = Character.getNumericValue(args[0].charAt(i));
            }

            // Construct initial cell arrays.
            int[] currentCells = new int[Integer.parseInt(args[1])];
            for (int i = 3; i < args.length; ++i) {
                currentCells[Integer.parseInt(args[i])] = 1;
            }

            ca = new CellArray(update, currentCells);
            largestPop = smallestPop = ca.getPopulation();
            
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

            System.out.println("Smallest popcount: " + smallestPop + " at step " + smallestPopGen);
            System.out.println("Largest popcount: " + largestPop + " at step " + largestPopGen);
            System.out.println("Final popcount: " + finalPop + " at step " + steps);
        }
    }
}
