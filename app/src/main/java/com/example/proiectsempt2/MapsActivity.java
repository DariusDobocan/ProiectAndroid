package com.example.proiectsempt2;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.proiectsempt2.databinding.ActivityMapsBinding;

import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    public static String latLong=null;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    public static double x= 0.0;
    public static double y=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        LatLng position = new LatLng(x, y);
        mMap.addMarker(new MarkerOptions().position(position).title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,15));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    public void onBack(View view){
        if(view.getId() == R.id.btnBck){
            Intent i = new Intent(MapsActivity.this,MainActivity.class);
        startActivity(i);
        }
    }
}