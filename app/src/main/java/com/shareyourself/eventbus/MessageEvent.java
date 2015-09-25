package com.shareyourself.eventbus;

/**
 * Created by akhare on 9/22/15.
 */
public class MessageEvent {
    public String phno;
    public String message;
    public String topicName;

    public MessageEvent(String phno, String message, String topicName) {
        this.phno = phno;
        this.message = message;
        this.topicName = topicName;
    }
}
