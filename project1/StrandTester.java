import java.io.*;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;

class StrandTester {
    private final String compQuery;
    private final String query;
    private ArrayList<String> inputStrands;

    StrandTester(String dbfile, String query) {
        this.query = query;
        this.compQuery = getReverseComplement(this.query);
        inputStrands = new ArrayList<String>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(dbfile));
            
            String line;
            while ((line = in.readLine()) != null) {
                inputStrands.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints all input strands that match the given query.
     * Each strand is run on its own thread.
     */
    public void printMatchingStrands() {
        LinkedList<StrandTesterThread> threads = new LinkedList<StrandTesterThread>();
        // For each strand, start a StrandTesterThread that checks if the strand
        // matches the given query.
        for (String strand : inputStrands) {
            StrandTesterThread strandTesterThread =
                new StrandTesterThread(compQuery, query, strand);
            strandTesterThread.start();
            threads.add(strandTesterThread);
        }

        for (StrandTesterThread thread : threads) {
            try {
                thread.join();
                if (thread.getResult() != null) {
                    System.out.println(thread.getResult());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static boolean strandMatchesQuery(String strand, String query) {
        String compQuery = getReverseComplement(query);
        return strand.contains(query) || strand.contains(compQuery);
    }

    static String getReverseComplement(String initial) {
        StringBuilder compBuilder = new StringBuilder();
        for (int i = 0; i < initial.length(); ++i) {
            switch (initial.charAt(i)) {
                case 'A':
                    compBuilder.append('T');
                    break;
                case 'C':
                    compBuilder.append('G');
                    break;
                case 'G':
                    compBuilder.append('C');
                    break;
                case 'T':
                    compBuilder.append('A');
                    break;
                default:
                    System.err.println("ERROR: No complement found for letter " + initial.charAt(i));
            }
        }
        return compBuilder.reverse().toString();
    }
}
