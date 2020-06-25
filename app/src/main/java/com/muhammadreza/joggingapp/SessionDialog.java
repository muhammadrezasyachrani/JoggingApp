package com.muhammadreza.joggingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SessionDialog extends AppCompatActivity {


    private EditText sessionnameet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set width of dialog 90% of the screen
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.90);
        setContentView(R.layout.activity_session_dialog);
        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        //refer edittext
        sessionnameet=(EditText)(findViewById(R.id.sessionnameet));

    }

    public void startsessiondialogbt(View view) {

        if(sessionnameet.equals(""))
        {

        }
        else
        {
            Intent in=new Intent(getApplicationContext(),Track.class);
            in.putExtra("sessionname",sessionnameet.getText().toString());
            setResult(88,in);
            finish();
        }

    }


}
