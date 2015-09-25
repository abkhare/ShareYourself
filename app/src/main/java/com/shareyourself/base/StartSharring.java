package com.shareyourself.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.shareyourself.R;
import com.shareyourself.gcm.GcmHandler;
import com.shareyourself.map.LocationMap;


public class StartSharring extends Activity implements View.OnClickListener{


    private static final String TAG = "StartSharring";
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_sharring);

        GcmHandler handler = new GcmHandler();
        handler.gcmRegistration(this);

        findViewById(R.id.save_phno_button).setOnClickListener(this);
        mSharedPreferences = getSharedPreferences("ShareYourself", Context.MODE_PRIVATE);
        if(!mSharedPreferences.getString("PHONE", "NONE").equals("NONE")){
            Intent intent = new Intent(this, LocationMap.class);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_phno_button:
                EditText phoneText = (EditText) findViewById(R.id.phone_editText);
                String phoneNumber = phoneText.getText().toString();
                if(phoneNumber.length() != 0) {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("PHONE", phoneNumber);
                    editor.commit();
                    Intent intent = new Intent(this, LocationMap.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_sharring, menu);
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
}
