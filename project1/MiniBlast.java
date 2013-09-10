/**
 * Contains main method for running MiniBlast test on DNA strands.
 *
 * @author Liam Morris
 */
class MiniBlast {
    /**
     * Prevents instantiation of class.
     */
    private MiniBlast() {}

    /**
     * Main method for running MiniBlast on DNA strands.
     *
     * @param args command line args for program. First argument should be path
     *             to input file, second argment should be query to search for.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("usage: java MiniBlast <dbfile> <query>");
        } else {
            StrandTester tester = new StrandTester(args[0], args[1]);
            tester.printMatchingStrands();
        }
    }
}
