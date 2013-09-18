import java.io.*;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * This class contains methods for testing if a given set of DNA strands
 * matches a given query.
 *
 * @author Liam Morris
 */
class StrandTester {
    /**
     * Reverse complement of the query to search.
     */
    private final String compQuery;
    /**
     * Given query to search.
     */
    private final String query;
    /**
     * List of strands read in from input file.
     */
    private ArrayList<String> inputStrands;

    /**
     * Constructor for StrandTester. Given input filepath, reads in strands and
     * stores them in inputStrands. Given query, computes reverse complement of
     * the query and stores both as class members.
     *
     * @param dbfile path to input file
     * @param query query to search for in DNA strands
     */
    StrandTester(String dbfile, String query) {
        this.query = query;
        this.compQuery = getReverseComplement(this.query);
        inputStrands = new ArrayList<String>();

        // Read in DNA strands and store them in inputStrands.
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
        // If query was not initialized or if it contained an invalid character
        // (indicated by compQuery being null), do nothing.
        if (query == null || compQuery == null) {
            System.err.println("Invalid query specified.");
        } else {
            // For each strand, start a StrandTesterThread that checks if the
            // strand matches the given query.
            for (int i = 0; i < inputStrands.size(); ++i) {
                new StrandTesterThread(compQuery, query, inputStrands.get(i), i)
                    .start();
            }
        }
    }

    /**
     * Computes the reverse complement of a given query.
     *
     * @param query the query whose reverse complement is being computed
     * @return the reverse complement of the input query
     */
    static String getReverseComplement(String query) {
        StringBuilder compBuilder = new StringBuilder();
        for (int i = 0; i < query.length(); ++i) {
            switch (query.charAt(i)) {
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
                    System.err.println("ERROR: No complement found for letter "
                                       + query.charAt(i));
                    return null;
            }
        }
        return compBuilder.reverse().toString();
    }
}
