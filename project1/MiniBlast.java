/**
 * @author Liam Morris
 */
class MiniBlast {
    private MiniBlast() {}

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("usage: java MiniBlast <dbfile> <query>");
        } else {
            StrandTester tester = new StrandTester(args[0], args[1]);
            tester.printMatchingStrands();
        }
    }
}
