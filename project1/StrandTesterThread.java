import java.lang.Thread;
import java.util.ArrayList;

class StrandTesterThread extends Thread {
    private final String compQuery;
    private final String query;
    private final String strand;
    private String result;

    StrandTesterThread(
            String compQuery,
            String query,
            String strand) {
        this.compQuery = compQuery;
        this.query = query;
        this.strand = strand;
    }

    public void run() {
        if (StrandTester.strandMatchesQuery(strand, query)) {
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

            for (int i : queryIndices) {
                curStrand = curStrand.substring(0, i) +
                            curStrand.substring(i, i + compQuery.length()).toUpperCase() +
                            curStrand.substring(i + compQuery.length(), curStrand.length());
            }

            result = curStrand;
        }
    }

    public String getResult() {
        return result;
    }
}
