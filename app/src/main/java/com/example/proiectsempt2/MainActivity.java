package com.example.proiectsempt2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {


    private Button btnStart, btnMap;
    private TextView latTxt, longTxt, nameTxt;
    private LocationManager locationManager;
    private LocationListener locationListener;
    boolean ok=true;
    Client clt = new Client("192.168.0.102", 8000,null);


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = findViewById(R.id.btnStart);
        btnMap = findViewById(R.id.btnMap);
        latTxt = findViewById(R.id.textViewLat);
        longTxt = findViewById(R.id.textViewLong);
        nameTxt = findViewById(R.id.editTextTextPersonName);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latTxt.setText(" ");
                latTxt.append("" + location.getLatitude());
                longTxt.setText(" ");
                longTxt.append("" + location.getLongitude());
                clt.sendMsg = location.getLatitude()+" "+location.getLongitude() + " "+ nameTxt.getText();

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET
                },10);
                return;
            } else {
                configureButton();

            }
        }

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    public void configureButton() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ok==true) {
                    ok=false;
                    clt.start();
                    if (locationManager == null)
                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    locationManager.requestLocationUpdates("gps", 1000, 1, locationListener);
                }
            }
        });
    }
}
