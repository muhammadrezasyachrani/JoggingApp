package com.muhammadreza.joggingapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyTrackingService extends Service {

    private LocationManager lm;
    LocalBroadcastManager lbm;
    long milisec = 0;
    private double totalmeters = 0;
    private double glat = 0;
    private double glng = 0;
    Timer t;
    mylocationlistener loclis;
    int calories = 0;
    int seccal = 6;
    NotificationManager nm;
    NotificationCompat.Builder builder;
    Notification n;
    private String sessionName;
    String date;
    private ArrayList<String> arrayLatitude;
    private ArrayList<String> arrayLongitude;

    private SQLiteDatabase db;

    public MyTrackingService() {

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        lbm = LocalBroadcastManager.getInstance(getApplication());

        sessionName = intent.getStringExtra("sessionname");
        arrayLatitude = new ArrayList<>();
        arrayLongitude = new ArrayList<>();
        initLocation();
        initNotification();
        setUpDataBase();
        initTimer();

        return START_STICKY;
    }

    private void setUpDataBase() {
        db = openOrCreateDatabase("FitDb", MODE_PRIVATE, null);
        db.execSQL("create table if not exists session (sessionid INTEGER PRIMARY KEY AUTOINCREMENT,sessionname VARCHAR(45),totaltime VARCHAR(45),kilometers VARCHAR(45),calories VARCHAR(45),date VARCHAR(45))");
        db.execSQL("create table if not exists sessiondata (sessiondataid INTEGER PRIMARY KEY AUTOINCREMENT,sessionid INTEGER,latitude VARCHAR(45),longitude VARCHAR(45))");

    }


    @Override
    public void onDestroy() {

        nm.cancel(1);
        t.cancel();
        lm.removeUpdates(loclis);


        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
        date = dateFormat.format(d);


        long millis = milisec;
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        String totaltime = minutes + " min " + seconds + " sec";
        db.execSQL("insert into session (sessionname,totaltime,kilometers,calories,date) values('" + sessionName + "','" + totaltime + "','" + df.format(totalmeters) + "','" + calories + "','" + date + "')");


        String values[] = {"1", "abc"};
        Cursor c = db.rawQuery("select MAX(sessionid) As maxid from session", null);
        c.moveToNext();

        String maxid = c.getString(c.getColumnIndex("maxid"));


        for (int i = 0; i < arrayLatitude.size(); i++) {
            db.execSQL("insert into sessiondata (sessionid,latitude,longitude) values(" + maxid + ",'" + arrayLatitude.get(i) + "','" + arrayLongitude.get(i) + "')");
        }

        Toast.makeText(getApplicationContext(), "Session Created", Toast.LENGTH_LONG).show();
        super.onDestroy();

    }


    public void initLocation() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_LONG).show();
        loclis = new mylocationlistener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, loclis);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, loclis);

    }

    public void initNotification() {
        Intent in = new Intent(this, Track.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, in, 0);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder = new NotificationCompat.Builder(this);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.common_full_open_on_phone));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

    }

    public void initTimer() {
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                lbm = LocalBroadcastManager.getInstance(getApplication());
                Intent in = new Intent("com.fitness.action");
                in.putExtra("time", milisec + "");
                in.putExtra("km", "no");
                milisec = milisec + 1000;
                if (milisec / 1000 == seccal) {
                    calories++;
                    seccal = seccal + 6;
                    in.putExtra("cal", calories + "");
                } else {
                    in.putExtra("cal", "no");
                }
                lbm.sendBroadcast(in);
                long millis = milisec;
                long minutes = (millis / 1000) / 60;
                long seconds = (millis / 1000) % 60;
                builder.setContentTitle("Stay Fit");
                builder.setContentText(("0" + minutes).substring(("0" + minutes).length() - 2) + " : " + ("0" + seconds).substring(("0" + seconds).length() - 2));
                builder.setContentInfo("3");
                n = builder.build();
                nm.notify(1, n);

            }
        }, 0, 1000);
    }

    class mylocationlistener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (glat == 0 && glng == 0) {

                glat = loc.getLatitude();
                glng = loc.getLongitude();
                arrayLatitude.add(glat + "");
                arrayLongitude.add(glng + "");

            } else {
                if (!(glat == loc.getLatitude() || glat == loc.getLongitude())) {

                    float dis[] = new float[1];
                    double distance = distance(glat, glng, loc.getLatitude(), loc.getLongitude());
                    totalmeters = totalmeters + distance;
                    glat = loc.getLatitude();
                    glng = loc.getLongitude();

                    arrayLatitude.add(glat + "");
                    arrayLongitude.add(glng + "");

                    Log.d("MYMSG", loc.getLatitude() + " " + loc.getLongitude());
                    lbm = LocalBroadcastManager.getInstance(getApplication());
                    Intent in = new Intent("com.fitness.action");
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    in.putExtra("time", "no");
                    in.putExtra("km", df.format(totalmeters) + "");
                    in.putExtra("cal", "no");
                    lbm.sendBroadcast(in);


                }
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
