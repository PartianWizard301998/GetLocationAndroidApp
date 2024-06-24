package com.example.locationidentifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    FusedLocationProviderClient fusedLocationProviderClient;
    Button locationButton;
    TextView locationText, city, country, lattitude, lontitude;
    private final static int REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationButton = findViewById(R.id.button);
        locationText = findViewById(R.id.textView);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        lattitude = findViewById(R.id.latitude);
        lontitude = findViewById(R.id.longitude);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });

    }
//18002673456
    private void getLastLocation() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                            locationText.setText("Address: "+addresses.get(0).getAddressLine(0));
                            city.setText("City: "+addresses.get(0).getLocality());
                            lattitude.setText("Lattitude:"+addresses.get(0).getLatitude());
                            lontitude.setText("Lontitude:"+addresses.get(0).getLongitude());
                            country.setText("Country:"+addresses.get(0).getCountryName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }else {
            askPermission();
        }

    }

    private void askPermission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_LONG).show();
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}