import java.lang.Thread;
import java.util.ArrayList;

/**
 * Thread on which queries are tested against individual DNA strands.
 *
 * @author Liam Morris
 */
class StrandTesterThread extends Thread {
    /**
     * Reverse complement of the query to search.
     */
    private final String compQuery;
    /**
     * Given query to search.
     */
    private final String query;
    /**
     * Given strand to test against.
     */
    private final String strand;
    /**
     * If strand matches query, it will be stored in result; otherwise result
     * will be null.
     */
    private String result;

    /**
     * Constructor for StrandTesterThread.
     *
     * @param compQuery reverse complement of input query
     * @param query input query
     * @param strand input strand
     */
    StrandTesterThread(String compQuery, String query, String strand) {
        this.compQuery = compQuery;
        this.query = query;
        this.strand = strand;
    }

    public void run() {
        // Test if either query or reverse complement of query matches input
        // strand.
        if (strand.contains(query) || strand.contains(compQuery)) {
            // If the given strand matches the query, find the indices in which
            // the query is found.
            String[] toks = strand.split(" ");
            String curStrand = toks[0] + " " + toks[1].toLowerCase();
            ArrayList<Integer> queryIndices = new ArrayList<Integer>();
            int curIndex = 0;
            while (curIndex != -1) {
                curIndex = strand.indexOf(query, curIndex);
                if (curIndex != -1) {
                    queryIndices.add(curIndex);
                    ++curIndex;
                }
            }
            
            curIndex = 0;
            while (curIndex != -1) {
                curIndex = strand.indexOf(compQuery, curIndex);
                if (curIndex != -1) {
                    queryIndices.add(curIndex);
                    ++curIndex;
                }
            }

            // Capitalize the substrings that match the query.
            for (int i : queryIndices) {
                curStrand = 
                    curStrand.substring(0, i) +
                    curStrand.substring(i, i + compQuery.length()).toUpperCase() +
                    curStrand.substring(i + compQuery.length(), curStrand.length());
            }

            result = curStrand;
        }
    }

    /**
     * Returns result if it exists.
     * @return the DNA strand if it matches the query, otherwise null
     */
    public String getResult() {
        return result;
    }
}
