package com.muhammadreza.joggingapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class SessionHistoryDetail extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String sessionid = "";
    private SQLiteDatabase db;

    TextView showsessionnamev, showtotaltimetv, showdatetv, showcaloriestv, showdistancetv, headsessionnametv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_history_detail);

        showsessionnamev = (TextView) (findViewById(R.id.showsessionnametv));
        showtotaltimetv = (TextView) (findViewById(R.id.showtotaltimetv));
        showdatetv = (TextView) (findViewById(R.id.showdatetv));
        showdistancetv = (TextView) (findViewById(R.id.showdistancetv));
        headsessionnametv = (TextView) (findViewById(R.id.headsessionnametv));


        sessionid = getIntent().getStringExtra("sessionid");
        String sessionname = getIntent().getStringExtra("sessionname");
        String totaltime = getIntent().getStringExtra("totaltime");
        String date = getIntent().getStringExtra("date");
        String distance = getIntent().getStringExtra("distance");

        showsessionnamev.setText(sessionname);
        showdatetv.setText(date);
        showtotaltimetv.setText(totaltime);
        showdistancetv.setText(distance+" km");

        headsessionnametv.setText(sessionname);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpDataBase();
    }

    private void setUpDataBase() {

        try {
            db = openOrCreateDatabase("FitDb", MODE_PRIVATE, null);
            db.execSQL("create table if not exists session (sessionid INTEGER PRIMARY KEY AUTOINCREMENT,sessionname VARCHAR(45),totaltime VARCHAR(45),kilometers VARCHAR(45),calories VARCHAR(45),date VARCHAR(45))");
            db.execSQL("create table if not exists sessiondata (sessiondataid INTEGER PRIMARY KEY AUTOINCREMENT,sessionid INTEGER,latitude VARCHAR(45),longitude VARCHAR(45))");

        } catch (Exception ae) {
            ae.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        Cursor c = db.rawQuery("select * from sessiondata where sessionid=" + Integer.parseInt(sessionid), null);
        Log.d("MYMSG", c.getCount() + " count");
        PolylineOptions rectOptions = new PolylineOptions();

        double latitude = 0, longitude = 0;
        while (c.moveToNext()) {

            int sessiondetailid = c.getInt(c.getColumnIndex("sessiondataid"));
            latitude = Double.parseDouble(c.getString(c.getColumnIndex("latitude")));
            longitude = Double.parseDouble(c.getString(c.getColumnIndex("longitude")));
            rectOptions.add(new LatLng(latitude, longitude)).color(Color.RED).width(5);

        }

        if (latitude != 0 && longitude != 0)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17.0f));

        mMap.addPolyline(rectOptions);
    }
}
