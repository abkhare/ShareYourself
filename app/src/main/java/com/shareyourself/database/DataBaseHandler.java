package com.shareyourself.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shareyourself.chat.ChatMessage;

public class DataBaseHandler  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "chat.db";

    public DataBaseHandler(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_MESSAGE = "CREATE TABLE " + ChatMessage.TABLE  + "("
                + ChatMessage.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + ChatMessage.KEY_PHONE_NO + " TEXT, "
                + ChatMessage.KEY_TOPIC_NAME + " TEXT, "
                + ChatMessage.KEY_CREATE_TIME + " TEXT, "
                + ChatMessage.KEY_MESSAGE + " TEXT )";

        db.execSQL(CREATE_TABLE_MESSAGE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + ChatMessage.TABLE);

        // Create tables again
        onCreate(db);

    }

}