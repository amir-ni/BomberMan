package Models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Chat implements Serializable {

    protected long id;
    protected String text;
    protected int senderId;
    protected LocalDateTime timeSent;

    public Chat(int id){
        this.id  = id;
    }

    public long getId() {
        return id;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }
}
