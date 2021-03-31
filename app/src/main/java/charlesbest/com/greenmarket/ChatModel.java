package charlesbest.com.greenmarket;

import java.util.Date;

/**
 * Created by BEN on 30/07/2018.
 */

public class ChatModel {
    private  String text;
    private  Long messageTime;

    public ChatModel() {
    }


    public ChatModel(String text) {
        this.text = text;
        this.messageTime = new Date().getTime();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }
}
