import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class is driver runnable class, that takes input and produces output required by using internal and implemented
 * classes and libraries.
 *
 * args has to be input path to file or directory for log(s)
 */
public class Driver {
    private static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
    public static void main(String[] args){
        Handler consoleHandler = new ConsoleHandler();
        LOGGER.addHandler(consoleHandler);
        // Checks for number of argument parameters passed.
        if (args.length < 1){
            LOGGER.log(Level.SEVERE,"arguments are missing.");
            System.exit(1);
        }
        String inputPath = args[0];
        if (args.length == 2){
            String s = args[1].toUpperCase();
            if (s.equals("FINE")) {
                LOGGER.setLevel(Level.FINE);
                consoleHandler.setLevel(Level.FINE);

            }
            else if (s.equals("ALL")) {
                LOGGER.setLevel(Level.ALL);
                consoleHandler.setLevel(Level.ALL);
            }
            else {
                LOGGER.setLevel(Level.INFO);
                consoleHandler.setLevel(Level.INFO);
            }
        }
        // create instance of FileHandler to get list of files to read
        FileHandler myFileHandler = new FileHandler(inputPath);
        LOGGER.finer( myFileHandler.getPath());
        List<File> fileList = myFileHandler.getFileList();

        String line = null;

        // create instance of Parser class to parse input lines
        Parser parser = new Parser();
        // create map of sessionAggregator for each userId
        Map<String,SessionAggregator> userSessionsMap = new HashMap<String, SessionAggregator>();
        // create instance of TopNSessionAggregator to keep track of top users with more page visits
        TopNSessionAggregator top5 = new TopNSessionAggregator(5);
        // loop files in file list and read it
        for (File f : fileList) {
            LOGGER.finer(f.getAbsolutePath());
            try {
                // create Scanner and read line by line
                Scanner scanner = new Scanner(f);
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    LOGGER.finest(line);
                    // pass each line to Parser to get fields of interest in this order [0]timestamp, [1]userId, [2]url
                    String[] fields = parser.extract(line);
                    if (fields != null){
                        // create a session from parsed fields
                        Session session = new Session(fields[1], convert(fields[0]), fields[2],-1);
                        LOGGER.finer(session.toString());
                        // if user has previous session update his sessionAggregate
                        if (userSessionsMap.containsKey(fields[1])){
                            SessionAggregator currentSessionAgg = userSessionsMap.get(fields[1]);
                            currentSessionAgg.addSession(session);
                        }
                        // else create new entry in the map
                        else{
                            List<Session> sessions = new ArrayList<Session>();
                            sessions.add(session);
                            userSessionsMap.put(fields[1],new SessionAggregator(fields[1], session.getTimestamp(),
                                    session.getTimestamp(), 1, -1, -1,
                                    sessions));
                        }
                    }
//                    break;
                }
                scanner.close();
//                break;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LOGGER.log(Level.SEVERE, "Bad File Exception was thrown for: " + f.getPath(), e);
                System.exit(1);
            }
        }

        // Loop over aggregated result, correct sessionAggregator with final min max values while tracking top N element
        for (String key : userSessionsMap.keySet()){
            SessionAggregator value = userSessionsMap.get(key);
            value.finalCheck();
            // keep track of top users
            top5.insert(value);
            // insert it back with updated values
            userSessionsMap.put(key, value);
            LOGGER.fine(key + " " + value);
        }

        System.out.println("Total number of users is: "+userSessionsMap.size());
        System.out.println("Top 5 users with most page visits are");
        System.out.println(top5);

    }

    /**
     * convert string to Date format with pattern given in problem statement
     * @param timeString time is string format "dd/MMM/yyyy:HH:mm:ss Z"
     * @return Date corresponding object to timeString passed
     */
    public static Date convert(String timeString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss ZZZZ");
        // set default timezone to be runtime independent
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        try {
            return dateFormat.parse(timeString);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Date cannot be parsed: "+ timeString, e);
            return null;
        }
    }
}
