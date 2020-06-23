package com.muhammadreza.joggingapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.muhammadreza.joggingapp.R;
import com.muhammadreza.joggingapp.Track;

public class Frag1 extends Fragment {

    int MY_PERMISSIONS_REQUEST_READ_CONTACTS=60;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frag1, container, false);

        Button starttrack = (Button) (view.findViewById(R.id.starttracting));

        //check for permission
        starttrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
                else
                {
                    checkIfLocationIsOn();
                }
            }
        });


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        {

            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)) {


                checkIfLocationIsOn();

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.

            }

        }
        else
        {

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkIfLocationIsOn()
    {


        LocationManager lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage(R.string.gps_network_not_enabled);
            dialog.setPositiveButton(R.string.open_location_setting, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent in = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(in);

                }

            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();

        }
        else
        {
            Intent in=new Intent(getActivity(),Track.class);
            startActivity(in);
        }

    }

}
