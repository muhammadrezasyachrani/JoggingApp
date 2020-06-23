package com.muhammadreza.joggingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

public class AboutAppDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.90);
        setContentView(R.layout.activity_about_app_dialog);
        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void closeAboutAppDialog(View view) {
        finish();
    }
}
