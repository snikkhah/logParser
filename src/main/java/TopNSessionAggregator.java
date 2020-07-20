/**
 * This class tracks top N element based on number of pages visited descending
 * it has an array that is sorted at all times
 */
public class TopNSessionAggregator {
    SessionAggregator[] topN;
    int minSessionSize;
    int size;
    int noOfElements;

    public TopNSessionAggregator(int size){
        topN = new SessionAggregator[size];
        this.size = size;
        noOfElements = 0;
        minSessionSize = -1;
    }

    /**
     * This is sorted insert with accordance to number of pages visited descending
     * @param sa sessionAggregator instance after aggregation
     */
    public void insert(SessionAggregator sa){
        if (size == 0 ) return;
        int saSize =  sa.getSessions().size();
        // first insertion doesn't require checks
        if (noOfElements == 0) {
            topN[0] = sa;
            noOfElements++;
            minSessionSize = saSize;
            return;
        }
        // if we already have topN and we have new highscore that belong to top N list
        if (noOfElements == size && saSize > minSessionSize){
            // loop to find the right position
            for (int i = 0; i < size; i++){
                int currentElementSize = topN[i].getSessions().size();
                // shift elements to the right by 1 from end of list to position
                if (saSize > currentElementSize){
                    for (int j = size - 1; j > i; j--){
                        topN[j] = topN[j-1];
                    }
                    topN[i] = sa;
                    return;
                }
            }
        }
        // else add it regardless of if it is new highscore or not to where it belongs keeping array sorted
        if (noOfElements < size) {
            for (int i = 0; i <= noOfElements; i++){
                // cover cases when its right most smallest record in the array
                if (topN[i] == null){
                    topN[i] = sa;
                    noOfElements++;
                    break;
                }
                int currentElementSize = topN[i].getSessions().size();
                if (saSize > currentElementSize){
                    // shift elements to the right by 1 from end of list to position
                    for (int j = noOfElements; j > i; j--){
                        topN[j] = topN[j-1];
                    }
                    topN[i] = sa;
                    noOfElements++;
                    break;
                }
            }
        }
    }

    /**
     * Print top N elements in required format
     * @return String formatted to requirement
     */
    public String toString(){
        String output = "id\t\t# pages\t\t\t# sess\t\tlongest\t\tshortest\n";
        for (SessionAggregator sa : topN){
            output += sa.getUserId() + "\t\t" + sa.getSessions().size() + "\t\t" + sa.getCurrentSessionId() + "\t\t"
                    + sa.getMaxSessionDuration()/60000 + "\t\t" + sa.getMinSessionDuration()/60000+"\n";
        }
        return output;
    }
}
