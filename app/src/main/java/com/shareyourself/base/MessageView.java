package com.shareyourself.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shareyourself.R;
import com.shareyourself.eventbus.EventBusSingleton;
import com.shareyourself.eventbus.MessageEvent;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MessageView extends Activity implements View.OnClickListener {

    private String TAG = "MessageView";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private static MessageViewAdapter mAdapter;
    private String topicName = "global";
    private List<MessageEvent> mDataset;
    private EditText mMessageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);

        mDataset = new LinkedList<MessageEvent>();
        mMessageBox = (EditText) findViewById(R.id.message_editText);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MessageViewAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        Button sendButton = (Button) findViewById(R.id.message_send_button);
        sendButton.setOnClickListener(this);

        //GcmHandler handler = new GcmHandler();
        //handler.gcmRegistration(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO get all messages from sqlite database
        EventBusSingleton.instance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBusSingleton.instance().unregister(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.message_send_button:
                    SharedPreferences mSharedPreferences = getSharedPreferences("ShareYourself", Context.MODE_PRIVATE);
                    JSONObject message = new JSONObject();
                    message.put("phno", mSharedPreferences.getString("PHONE", "NONE"));
                    message.put("textMessage", mMessageBox.getText().toString());
                    message.put("topicName", "global");
                    JSONObject payload = new JSONObject();
                    payload.put("message", message);
                    (new GcmSender()).sendToGCM(this, payload);
                    break;
            }
        }
        catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onMessageReceived(MessageEvent event) {
        if(event.topicName.equals(topicName)) {
            mDataset.add(event);
            mAdapter.notifyDataSetChanged();
        }
    }

}
