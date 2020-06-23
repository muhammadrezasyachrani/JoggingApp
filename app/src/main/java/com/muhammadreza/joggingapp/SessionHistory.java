package com.muhammadreza.joggingapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;

public class SessionHistory extends AppCompatActivity {

    private SQLiteDatabase db;
    ArrayList<sessionClass> alSessionHistoty;
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView rvhistory;
    HistoryAdapter myad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_session_history);

        alSessionHistoty = new ArrayList<>();
        toolbar = (Toolbar) (findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        rvhistory = (RecyclerView) findViewById(R.id.rvhistory);
        rvhistory.setNestedScrollingEnabled(false);

        myad = new HistoryAdapter(this, alSessionHistoty);
        rvhistory.setAdapter(myad);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvhistory.setLayoutManager(linearLayoutManager);

        setUpDataBase();
        fetchHistoryFromDatabase();

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


    public void deleteSession(String sessionId) {

        db.execSQL("delete from session where sessionid="+sessionId);
        fetchHistoryFromDatabase();
        Toast.makeText(this, "Session Deleted", Toast.LENGTH_SHORT).show();

    }


    private void fetchHistoryFromDatabase() {

        alSessionHistoty.clear();
        Cursor c = db.rawQuery("select * from session", null);
        while (c.moveToNext()) {

            int sessionid = c.getInt(c.getColumnIndex("sessionid"));
            String name = c.getString(c.getColumnIndex("sessionname"));
            String totaltime = c.getString(c.getColumnIndex("totaltime"));
            String kilometers = c.getString(c.getColumnIndex("kilometers"));
            String calories = c.getString(c.getColumnIndex("calories"));
            String date = c.getString(c.getColumnIndex("date"));
            sessionClass obj = new sessionClass(sessionid, name, totaltime, kilometers, calories, date);
            alSessionHistoty.add(obj);
        }
        myad.notifyDataSetChanged();
    }


}
