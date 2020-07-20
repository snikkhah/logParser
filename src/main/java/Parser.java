import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Parser class is defined to parse a given line and return fields of interest
 */
public class Parser {
    private static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
    public Parser(){}

    public String[] extract(String line){
        String timestamp, userId, url;
        String[] splits = line.split("- ",4);
        // splits[2] will contain: <timestamp> "<method> <path> HTTP/x.x" <httpResponseCode>
//        System.out.println(splits[2]);
        LOGGER.finer(splits[2]);
        if (splits.length < 4)
            return null;
        // subSplits will contain [0]<timestamp> [1]<method> <path> HTTP/x.x [2]<httpResponseCode>
        String[] subSplits = splits[2].split("\"",3);
//        printSplit(subSplits);
        if (subSplits.length < 3) {
            return null;
        }
        timestamp = subSplits[0].trim();
        // apiCallSplit will contain [0]<method> [1]<path> [2]HTTP/x.x
        String[] apiCallSplit = subSplits[1].split(" ",3);
        if (apiCallSplit.length < 3)
            return null;

        LOGGER.finer(apiCallSplit[1]);
        url = apiCallSplit[1];
        // split on / to get the userId and ignore urls for ELB calls
        String[] urlSplit = apiCallSplit[1].split("/",5);
//        printSplit(urlSplit);
        if (urlSplit.length < 4)
            return null;
        userId = urlSplit[3].trim();
        LOGGER.finer(timestamp + "\t" + userId + "\t" + url);
        return new String[]{timestamp, userId, url};
    }

    // print out splits for debugging/development only
    private void printSplit(String[] split){
        for (int i = 0; i<split.length; i++)
            LOGGER.finest(i+". "+split[i]+"\t");
    }
}
