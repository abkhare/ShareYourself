package com.shareyourself.chat;

/**
 * Created by Harinath on 9/26/2015.
 */
public class ChatMessage {
        public static final String TABLE = "Message";

        // Labels Table Columns names
        public static final String KEY_ID = "id";
        public static final String KEY_PHONE_NO= "phoneNo";
        public static final String KEY_MESSAGE = "message";
    public static final String KEY_TOPIC_NAME = "topicName";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_CREATE_TIME = "createTime";

        // property help us to keep data
        private int message_Id;
        private String phoneNo;
        private String topicName;
        private String createTime;
        private boolean isMe;
        private String message;
        private String userName;


    public int getMessage_Id() {
        return message_Id;
    }

    public void setMessage_Id(int message_Id) {
        this.message_Id = message_Id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

  }
