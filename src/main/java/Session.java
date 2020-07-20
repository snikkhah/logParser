import java.util.Date;

/**
 * Session class contains session related parameters
 */
public class Session {
    String userId;
    Date timestamp;
    String url;
    int sessionId;

    public Session(){
        this.userId = null;
        this.timestamp = null;
        this.sessionId = -1;
        this.url = null;
    }

    public Session(String userId, Date timestamp, String url, int sessionId){
        this.userId = userId;
        this.timestamp = timestamp;
        this.url = url;
        this.sessionId = sessionId;
    }

    public String toString(){
        return "userId: "+userId+ " timestamp: " + timestamp.toString() + " url: " + url + " sessionId: " + sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
