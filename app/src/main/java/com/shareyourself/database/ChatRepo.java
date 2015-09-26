package com.shareyourself.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shareyourself.chat.ChatMessage;

import java.util.ArrayList;

/**
 * Created by Harinath on 9/26/2015.
 */
public class ChatRepo {
   private DataBaseHandler dbHelper;

    public ChatRepo(Context context) {
        dbHelper = new DataBaseHandler(context);
    }

    public int insert(ChatMessage message) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ChatMessage.KEY_MESSAGE, message.getMessage());
        values.put(ChatMessage.KEY_PHONE_NO, message.getPhoneNo());
        values.put(ChatMessage.KEY_USER_NAME, message.getUserName());
        values.put(ChatMessage.KEY_TOPIC_NAME, message.getTopicName());
        values.put(ChatMessage.KEY_CREATE_TIME, message.getCreateTime());
        
        // Inserting Row
        long message_Id = db.insert(message.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) message_Id;
    }

    public void delete(int message_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(ChatMessage.TABLE, ChatMessage.KEY_ID + "= ?", new String[] { String.valueOf(message_Id) });
        db.close(); // Closing database connection
    }

   /* public void update(ChatMessage message) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ChatMessage.KEY_age, message.age);
        values.put(ChatMessage.KEY_email,message.email);
        values.put(ChatMessage.KEY_name, message.name);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(ChatMessage.TABLE, values, ChatMessage.KEY_ID + "= ?", new String[] { String.valueOf(message.message_ID) });
        db.close(); // Closing database connection
    }*/

    public ArrayList<ChatMessage> getChatMessageList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                ChatMessage.KEY_ID + "," +
                ChatMessage.KEY_TOPIC_NAME + "," +
                ChatMessage.KEY_MESSAGE + "," +
                ChatMessage.KEY_PHONE_NO + "," +
                ChatMessage.KEY_USER_NAME + "," +
                ChatMessage.KEY_CREATE_TIME +
                " FROM " + ChatMessage.TABLE;

        //ChatMessage message = new ChatMessage();
        ArrayList<ChatMessage> messageList = new ArrayList<ChatMessage>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
               ChatMessage message = new ChatMessage();
                message.setMessage_Id(cursor.getInt(cursor.getColumnIndex(ChatMessage.KEY_ID)));
                message.setMessage(cursor.getString(cursor.getColumnIndex(ChatMessage.KEY_MESSAGE)));
                message.setTopicName(cursor.getString(cursor.getColumnIndex(ChatMessage.KEY_TOPIC_NAME)));
                message.setUserName(cursor.getString(cursor.getColumnIndex(ChatMessage.KEY_USER_NAME)));
                message.setCreateTime(cursor.getString(cursor.getColumnIndex(ChatMessage.KEY_CREATE_TIME)));
                message.setPhoneNo(cursor.getString(cursor.getColumnIndex(ChatMessage.KEY_PHONE_NO)));
                messageList.add(message);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return messageList;

    }

    /*
    public ChatMessage getChatMessageById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                ChatMessage.KEY_ID + "," +
                ChatMessage.KEY_name + "," +
                ChatMessage.KEY_email + "," +
                ChatMessage.KEY_age +
                " FROM " + ChatMessage.TABLE
                + " WHERE " +
                ChatMessage.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        ChatMessage message = new ChatMessage();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                message.message_ID =cursor.getInt(cursor.getColumnIndex(ChatMessage.KEY_ID));
                message.name =cursor.getString(cursor.getColumnIndex(ChatMessage.KEY_name));
                message.email  =cursor.getString(cursor.getColumnIndex(ChatMessage.KEY_email));
                message.age =cursor.getInt(cursor.getColumnIndex(ChatMessage.KEY_age));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return message;
    }*/

}