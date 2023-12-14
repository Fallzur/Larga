package com.example.larga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.larga.R;
import com.example.larga.User_geterSeter;
import com.example.larga.Z_DIVER_LIST;
import com.example.larga.Z_DRIVER_HISTORY;
import com.example.larga.Z_DRIVER_PROFILE;
import com.example.larga.databinding.ActivityZdiverPanelBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Z_DIVER_PANEL extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityZdiverPanelBinding binding;

    ImageView btnHome, btnMap, btnHistory, driverPof,online_offline;
    TextView name;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    Double lat, lon;
    String location;

    private SupportMapFragment mapFragment;
    private boolean mapVisible = true;

    TextView online,offline;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.DRIVERMap);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.DRIVERMap, mapFragment)
                    .commit();
        }


        mapFragment.getMapAsync(googleMap -> {
            // Customize the map if needed
        });




        binding = ActivityZdiverPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        driverPof = findViewById(R.id.DriverImage);
        btnHome = findViewById(R.id.driver_home);
        btnMap = findViewById(R.id.driver_map);
        btnHistory = findViewById(R.id.driver_history);
        name = findViewById(R.id.nameOfDirver);

        online_offline = findViewById(R.id.online_offline);
        online_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
toggleMapVisibility();
            }
        });

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        driverPof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DIVER_PANEL.this, Z_DRIVER_PROFILE.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DIVER_PANEL.this, Z_DIVER_PANEL.class);
                startActivity(intent);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DIVER_PANEL.this, Z_DIVER_LIST.class);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DIVER_PANEL.this, Z_DRIVER_HISTORY.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Drivers");
        databaseReference.keepSynced(true);
        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_geterSeter user = snapshot.getValue(User_geterSeter.class);
                name.setText(user.getName().trim());
                location = user.getAddress().trim();
                lon = Double.parseDouble(user.getLon().trim());
                lat = Double.parseDouble(user.getLat().trim());

                // Set up the map after fetching driver data
                setUpMap();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    // Method to set up the map
    private void setUpMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.DRIVERMap);
        mapFragment.getMapAsync(this);
    }

    // Rest of your class remains unchanged

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at the driver's location
        LatLng driverLocation = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(driverLocation).title(location));

        // Automatically zoom to the driver's location with a specified zoom level (e.g., 15f)
        float zoomLevel = 15f;

        // Use animateCamera for a smooth zoom animation
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, zoomLevel));
    }



    @Override
    public void onBackPressed() {
        // Handle the back button as needed
    }

    private void toggleMapVisibility() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mapVisible) {
            transaction.hide(mapFragment);
            online = findViewById(R.id.onine);
            offline = findViewById(R.id.offline);
            offline.setVisibility(View.VISIBLE);
            online.setVisibility(View.INVISIBLE);
           offline();
        } else {
            transaction.show(mapFragment);
            online = findViewById(R.id.onine);
            offline = findViewById(R.id.offline);
            offline.setVisibility(View.INVISIBLE);
            online.setVisibility(View.VISIBLE);
           active();
        }
        transaction.commit();
        mapVisible = !mapVisible;
    }



    private  void offline(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ALL FAVORITE");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    // Check if the "MYID" field matches your ID
                    String myIdValue = child.child("idOfDriver").getValue(String.class);
                    if (myIdValue != null && myIdValue.equals(firebaseAuth.getUid())) {
                        // Update "status" and "availability" for the matching data
                        DatabaseReference statusRef = child.child("status").getRef();
                        DatabaseReference availabilityRef = child.child("availavility").getRef();

                        // Update the values as needed
                        statusRef.setValue("offline");
                        availabilityRef.setValue("not available");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database operation
            }
        });

    }



    private  void active(){
        // Your Firebase Database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ALL FAVORITE");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    // Check if the "MYID" field matches your ID
                    String myIdValue = child.child("idOfDriver").getValue(String.class);
                    if (myIdValue != null && myIdValue.equals(firebaseAuth.getUid())) {
                        // Update "status" and "availability" for the matching data
                        DatabaseReference statusRef = child.child("status").getRef();
                        DatabaseReference availabilityRef = child.child("availavility").getRef();

                        // Update the values as needed
                        statusRef.setValue("active");
                        availabilityRef.setValue("available");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database operation
            }
        });

    }
}
