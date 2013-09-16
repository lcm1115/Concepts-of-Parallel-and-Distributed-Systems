import java.lang.Thread;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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
     * Shared integer to synchronized printing.
     */
    private static AtomicInteger lastPrint = new AtomicInteger(0);

    /**
     * Indicates print order of this thread, printing will not happen until
     * printOrder == lastPrint.
     */
    private final int printOrder;

    /**
     * Constructor for StrandTesterThread.
     *
     * @param compQuery reverse complement of input query
     * @param query input query
     * @param strand input strand
     */
    StrandTesterThread(String compQuery,
                       String query,
                       String strand,
                       int printOrder) {
        this.compQuery = compQuery;
        this.query = query;
        this.strand = strand;
        this.printOrder = printOrder;
    }

    public void run() {
        // Test if either query or reverse complement of query matches input
        // strand.
        String[] toks = strand.split(" ");
        String sequence = toks[0] + " ";
        String curStrand = toks[1];
        String result = null;
        if (curStrand.contains(query) || curStrand.contains(compQuery)) {
            // If the given strand matches the query, find the indices in which
            // the query is found.
            ArrayList<Integer> queryIndices = new ArrayList<Integer>();
            int curIndex = 0;
            while (curIndex != -1) {
                curIndex = curStrand.indexOf(query, curIndex);
                if (curIndex != -1) {
                    queryIndices.add(curIndex);
                    ++curIndex;
                }
            }
            
            curIndex = 0;
            while (curIndex != -1) {
                curIndex = curStrand.indexOf(compQuery, curIndex);
                if (curIndex != -1) {
                    queryIndices.add(curIndex);
                    ++curIndex;
                }
            }

            curStrand = curStrand.toLowerCase();

            // Capitalize the substrings that match the query.
            int queryLength = query.length();
            int curStrandLength = curStrand.length();
            for (int i : queryIndices) {
                curStrand = 
                    curStrand.substring(0, i) +
                    curStrand.substring(i, i + queryLength).toUpperCase() +
                    curStrand.substring(i + queryLength, curStrandLength);
            }

            // Wait for previous thread to print, then print results.
            result = sequence + curStrand;
        }
        while (printOrder > lastPrint.get());
        if (result != null) {
            synchronized (System.out) {
                System.out.println(result);
            }
        }
        lastPrint.incrementAndGet();
    }
}
