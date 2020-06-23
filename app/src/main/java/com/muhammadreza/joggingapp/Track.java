package com.muhammadreza.joggingapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.muhammadreza.joggingapp.MyTrackingService;
import com.muhammadreza.joggingapp.R;
import com.muhammadreza.joggingapp.SessionDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Track extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager lm;
    private Location loc;
    private LocalBroadcastManager lbm;
    private boolean flag = false;
    private TextView locationinfo, trackkmtv, tracktimetv, trackkmcalories;
    private updatebroadcast ubc;
    private Button serviceStartStopBtn;
    myloclistener loclistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        serviceStartStopBtn = (Button) (findViewById(R.id.serviceStartStopBtn));
        trackkmtv = (TextView) (findViewById(R.id.trackkmtv));
        tracktimetv = (TextView) (findViewById(R.id.tracktimetv));
        trackkmcalories = (TextView) (findViewById(R.id.trackkmcalories));
        locationinfo = (TextView) findViewById(R.id.locationinfo);


        if(getSharedPreferences("fitdata",MODE_PRIVATE).getString("servicestatus",null)!=null)
        {
            serviceStartStopBtn.setText("Stop Tracking");

        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        setupLocationServices();
        setupBroadcastReceiver();
    }


    private void setupLocationServices() {
        try {

            lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            loclistener = new myloclistener();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            boolean gps_enabled = false;
            boolean network_enabled = false;

            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (gps_enabled) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, loclistener);
            } else if (network_enabled) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, loclistener);
            }
        } catch (Exception ae) {
            ae.printStackTrace();
        }
    }

    private void setupBroadcastReceiver() {
        try {


            ubc = new updatebroadcast();
            lbm = LocalBroadcastManager.getInstance(this);
            lbm.registerReceiver(ubc, new IntentFilter("com.fitness.action"));

        } catch (Exception ae) {
            ae.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lbm.unregisterReceiver(ubc);
        lm.removeUpdates(loclistener);
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    public void track(View view) {


        if (serviceStartStopBtn.getText().toString().equals("Start Tracking")) {
            Intent in = new Intent(getApplicationContext(), SessionDialog.class);
            startActivityForResult(in, 88);
            serviceStartStopBtn.setText("Stop Tracking");
            getSharedPreferences("fitdata",MODE_PRIVATE).edit().putString("servicestatus","on").commit();

        } else {
            Intent servicein = new Intent(getApplicationContext(), MyTrackingService.class);
            stopService(servicein);
            serviceStartStopBtn.setText("Start Tracking");
            getSharedPreferences("fitdata",MODE_PRIVATE).edit().putString("servicestatus",null).commit();

        }

    }

    class myloclistener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }


            Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng yourLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
            mMap.addMarker(new MarkerOptions().position(yourLocation).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(yourLocation, 17.f));
            lm.removeUpdates(this);

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

    class updatebroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("com.fitness.action")) {


                String km = intent.getStringExtra("km");
                String time = intent.getStringExtra("time");
                String cal = intent.getStringExtra("cal");

                if (!km.equals("no")) {
                    trackkmtv.setText(km + " km");
                }
                if (!time.equals("no")) {
                    String millisec = intent.getStringExtra("time");
                    long millis = Long.parseLong(millisec);
                    long minutes = (millis / 1000) / 60;
                    long seconds = (millis / 1000) % 60;

                    tracktimetv.setText(("0" + minutes).substring(("0" + minutes).length() - 2) + " : " + ("0" + seconds).substring(("0" + seconds).length() - 2));
                }
                if (!cal.equals("no")) {
                    trackkmcalories.setText(cal + " cal");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 88) {
            String sessionname = data.getStringExtra("sessionname");
            locationinfo.setText("Session Name " + sessionname);
            Intent servicein = new Intent(getApplicationContext(), MyTrackingService.class);
            servicein.putExtra("sessionname", sessionname);
            startService(servicein);

        }
    }
}
