package com.shareyourself.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by akhare on 9/15/15.
 */
public class Utility {

    public void showAlertDialog(final Activity activity){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("PASSWORD");
        alertDialog.setMessage("Enter Password");

        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String myName = input.getText().toString();
                        if(myName.length() != 0){
                            SharedPreferences sharedPreferences = activity.getSharedPreferences("ShareYourself", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("MYNAME", myName);
                            editor.commit();
                        }
                        else{
                            activity.finish();
                        }
                    }
                });

        alertDialog.show();
    }

}
