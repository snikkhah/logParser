import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * SessionAggregator class contains list of sessions for a user while tracking session duration and number of sessions
 */
public class SessionAggregator {
    String userId;
    Date lastAccessTime;
    Date sessionStartTime;
    long currentSessionId;
    long minSessionDuration;
    long maxSessionDuration;
    List<Session> sessions;

    public SessionAggregator(){
        this.userId = null;
        this.lastAccessTime = null;
        this.sessionStartTime = null;
        this.currentSessionId = 1;
        this.minSessionDuration = -1;
        this.maxSessionDuration = -1;
        this.sessions = new ArrayList<Session>();
    }

    public SessionAggregator(String userId, Date lastAccessTime, Date sessionStartTime, long currentSessionId,
                             long minSessionDuration, long maxSessionDuration,
                             List<Session> sessions) {
        this.userId = userId;
        this.lastAccessTime = lastAccessTime;
        this.sessionStartTime = sessionStartTime;
        this.currentSessionId = currentSessionId;
        this.minSessionDuration = minSessionDuration;
        this.maxSessionDuration = maxSessionDuration;
        this.sessions = sessions;
    }

    /**
     * add new session while tracking session and updating session related values being tracked
     * @param session session created after input log line is read and parsed
     */
    public void addSession (Session session){
        Date currentSessionTimestamp =  session.getTimestamp();
        // get delta from previous access to determine if a new session is started
        long deltaFromPrevAccess = Math.abs(currentSessionTimestamp.getTime() - lastAccessTime.getTime());
        // if difference is more than 10 min then update previous concluded session related variables
        if (deltaFromPrevAccess > 600000) {
            // last session duration
            long currentSessionDuration = Math.abs(lastAccessTime.getTime() - sessionStartTime.getTime());
            // if min and max session duration are not recorded then current session duration is both min and max
            if (minSessionDuration == -1 || maxSessionDuration == -1){
                minSessionDuration = currentSessionDuration;
                maxSessionDuration = currentSessionDuration;
            }
            // otherwise update min and max
            else {
                if (currentSessionDuration < minSessionDuration)
                    minSessionDuration = currentSessionDuration;
                if (currentSessionDuration > maxSessionDuration)
                    maxSessionDuration = currentSessionDuration;
            }
            // update session counter and session start time
            currentSessionId++;
            sessionStartTime = currentSessionTimestamp;
        }
        // set for next invocation
        lastAccessTime = currentSessionTimestamp;
        // add session to sessions list
        sessions.add(session);
    }

    /**
     * Since session is concluded when new session is encountered then last (or the only) session will not get evaluated
     * This function is called when all logs are processed to check for last (or only) session and update min max values
     * session number is always ahead by 1 and doesn't need to be updated
     */
    public void finalCheck(){
        long finalSessionDuration = Math.abs(lastAccessTime.getTime() - sessionStartTime.getTime());
        if (minSessionDuration == -1 || maxSessionDuration == -1){
            minSessionDuration = finalSessionDuration;
            maxSessionDuration = finalSessionDuration;
        }
        else {
            if (finalSessionDuration < minSessionDuration)
                minSessionDuration = finalSessionDuration;
            if (finalSessionDuration > maxSessionDuration)
                maxSessionDuration = finalSessionDuration;
        }
    }

    public String toString(){
        return "userId: " + userId + " lastAccessTime: " + lastAccessTime + " NumOfSessions: " + currentSessionId +
                " sessionStartTime: " + sessionStartTime +  " minSessionDuration: " + minSessionDuration +
                " maxSessionDuration: " + maxSessionDuration  + " pageSize: " + sessions.size();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Date getSessionStartTime() {

        return sessionStartTime;
    }

    public void setSessionStartTime(Date sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public long getCurrentSessionId() {
        return currentSessionId;
    }

    public void setCurrentSessionId(long currentSessionId) {
        this.currentSessionId = currentSessionId;
    }

    public long getMinSessionDuration() {
        return minSessionDuration;
    }

    public void setMinSessionDuration(long minSessionDuration) {
        this.minSessionDuration = minSessionDuration;
    }

    public long getMaxSessionDuration() {
        return maxSessionDuration;
    }

    public void setMaxSessionDuration(long maxSessionDuration) {
        this.maxSessionDuration = maxSessionDuration;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}
