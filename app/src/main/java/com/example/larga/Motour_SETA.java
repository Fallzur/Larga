package com.example.larga;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.larga.databinding.ActivityMotourSetaBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Motour_SETA extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMotourSetaBinding binding;



    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 50;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    Location currentLocation;
    List<Location> saveLocation;
    LocationCallback locationCallBack;
    //Location request is a config file all setting related to fused
    LocationRequest locationRequest;

    FusedLocationProviderClient fusedLocationProviderClient;


    Button location1,location2,location3;

    Button view1,view2,view3;


    ImageView img1,img2,img3;

    Button Start;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String name,number;

    String StourLocationName,StourLat,Stourlon;

    TextView lat,lon,add;
RelativeLayout relative1,relative2;

TextView spot;

Button cancel;

Intent favoriteDriver;
String IdOfFavoriteDriver ="";

TextView ContactingYourDriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        binding = ActivityMotourSetaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        spot = findViewById(R.id.spot);

lat = findViewById(R.id.trlat);
lon = findViewById(R.id.trlon);
add = findViewById(R.id.tradd);
lat.setVisibility(View.INVISIBLE);
lon.setVisibility(View.INVISIBLE);
add.setVisibility(View.INVISIBLE);



        //------intent get the id of favoritr driver------------
        favoriteDriver = getIntent();
        IdOfFavoriteDriver = favoriteDriver.getStringExtra("favoriteDriver");

        // Check if IdOfFavoriteDriver is empty or null
        if (IdOfFavoriteDriver == null || IdOfFavoriteDriver.isEmpty() || IdOfFavoriteDriver == "") {
           IdOfFavoriteDriver = "";
        } else {
            IdOfFavoriteDriver = favoriteDriver.getStringExtra("favoriteDriver");
        }

        ImageView gifImageView = findViewById(R.id.trloading_setA);
        cancel = findViewById(R.id.tr_cancel_setA);

// Load and display the GIF using Glide
        Glide.with(this)
                .asGif()  // Specify that the resource is a GIF
                .load(R.drawable.loading1)  // Replace with your GIF's resource name
                .into(gifImageView);  // Display it in the ImageView




        relative1 = findViewById(R.id.LocationNav_setA);
        relative2 = findViewById(R.id.findingNav_setA);

        relative1.setVisibility(View.VISIBLE);
        relative2.setVisibility(View.INVISIBLE);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);



        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);



        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //get the location of user
                Location location = locationResult.getLastLocation();
                updateUIValues(location);
            }
        };




        firebaseAuth = FirebaseAuth.getInstance();

        Start = findViewById(R.id.Start_setA);

        location1 = findViewById(R.id.loc_1_setA);
        location2 = findViewById(R.id.loc_2_setA);
        location3 = findViewById(R.id.loc_3_setA);

        view1 = findViewById(R.id.view_loc_1_setA);
        view2 = findViewById(R.id.view_loc_2_setA);
        view3 = findViewById(R.id.view_loc_3_setA);


        img1 = findViewById(R.id.img1_setA);
        img2 = findViewById(R.id.img2_setA);
        img3 = findViewById(R.id.img3_setA);

      ContactingYourDriver = findViewById(R.id.seta_contactYourdriver);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelUpdateStatus("cancel");
            }
        });



        //---------------------------------------------------------------------------Start--------------------
Start.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        showYesNoAlertDialog();
    }
});




        //---------------------------------------------------------------------------btn1--------------------

        location1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location2.setVisibility(View.VISIBLE);
                location2.setClickable(true);

                location1.setVisibility(View.INVISIBLE);
                location1.setClickable(false);


                location3.setVisibility(View.VISIBLE);
                location3.setClickable(true);



                view1.setVisibility(View.VISIBLE);
                view1.setClickable(true);

                view2.setVisibility(View.INVISIBLE);
                view2.setClickable(false);

                view3.setVisibility(View.INVISIBLE);
                view3.setClickable(false);

                img2.setVisibility(View.INVISIBLE);
                img3.setVisibility(View.INVISIBLE);
                updateLocation1();
            }
        });

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img1.setVisibility(View.VISIBLE);

                spot.setText("Bud promotory eco park");

                location2.setVisibility(View.VISIBLE);
                location2.setClickable(true);

                location1.setVisibility(View.VISIBLE);
                location1.setClickable(true);


                location3.setVisibility(View.VISIBLE);
                location3.setClickable(true);



                view1.setVisibility(View.INVISIBLE);
                view1.setClickable(false);

                view2.setVisibility(View.INVISIBLE);
                view2.setClickable(false);

                view3.setVisibility(View.INVISIBLE);
                view3.setClickable(false);
            }
        });

     //---------------------------------------------------------------------------btn2--------------------

        location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location2.setVisibility(View.INVISIBLE);
                location2.setClickable(false);

                location1.setVisibility(View.VISIBLE);
                location1.setClickable(true);


                location3.setVisibility(View.VISIBLE);
                location3.setClickable(true);


                view1.setVisibility(View.INVISIBLE);
                view1.setClickable(false);

                view2.setVisibility(View.VISIBLE);
                view2.setClickable(true);

                view3.setVisibility(View.INVISIBLE);
                view3.setClickable(false);

                img1.setVisibility(View.INVISIBLE);
                img3.setVisibility(View.INVISIBLE);

                updateLocation2();
            }
        });

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img2.setVisibility(View.VISIBLE);

                spot.setText("Balangay site museum");

                location2.setVisibility(View.VISIBLE);
                location2.setClickable(true);

                location1.setVisibility(View.VISIBLE);
                location1.setClickable(true);


                location3.setVisibility(View.VISIBLE);
                location3.setClickable(true);



                view1.setVisibility(View.INVISIBLE);
                view1.setClickable(false);

                view2.setVisibility(View.INVISIBLE);
                view2.setClickable(false);

                view3.setVisibility(View.INVISIBLE);
                view3.setClickable(false);
            }
        });

        //---------------------------------------------------------------------------btn3--------------------


        location3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location3.setVisibility(View.INVISIBLE);
                location3.setClickable(false);

                location2.setVisibility(View.VISIBLE);
                location2.setClickable(true);


                location1.setVisibility(View.VISIBLE);
                location1.setClickable(true);


                view1.setVisibility(View.INVISIBLE);
                view1.setClickable(false);

                view2.setVisibility(View.INVISIBLE);
                view2.setClickable(false);

                view3.setVisibility(View.VISIBLE);
                view3.setClickable(true);

                img1.setVisibility(View.INVISIBLE);
                img2.setVisibility(View.INVISIBLE);


                updateLocation3();
            }
        });

        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img3.setVisibility(View.VISIBLE);

                spot.setText("Magellan anchorge");

                location2.setVisibility(View.VISIBLE);
                location2.setClickable(true);

                location1.setVisibility(View.VISIBLE);
                location1.setClickable(true);


                location3.setVisibility(View.VISIBLE);
                location3.setClickable(true);

                view1.setVisibility(View.INVISIBLE);
                view1.setClickable(false);

                view2.setVisibility(View.INVISIBLE);
                view2.setClickable(false);

                view3.setVisibility(View.INVISIBLE);
                view3.setClickable(false);
            }
        });



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth= FirebaseAuth.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.keepSynced(true);
        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_geterSeter user = snapshot.getValue(User_geterSeter.class);
                name = user.getName().toString().trim();
                number = user.getContact().toString().trim();
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //----------------------------------------------------------check my request----------------------------------------
        DatabaseReference alertRef = FirebaseDatabase.getInstance().getReference("/ALL PASUNDO/"+firebaseAuth.getUid()+"/status");
        alertRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String status = snapshot.getValue(String.class);
                    if (status.equals("accepted")){
                        relative1.setVisibility(View.VISIBLE);
                        relative2.setVisibility(View.INVISIBLE);
                        progressDialog.dismiss();
                        fetchAcceptedDetails();
                    }else {
                        progressDialog.dismiss();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });





        updateGPS();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_seta);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // By default, let's show a marker in Sydney, Australia
        LatLng sydney = new LatLng(8.952067365149873, 125.49264693662136);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    // Function to update the map with a specific location
    private void updateMap(double latitude, double longitude, String title) {
        LatLng location = new LatLng(latitude, longitude);
        mMap.clear(); // Clear existing markers
        mMap.addMarker(new MarkerOptions().position(location).title(title));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10)); // Adjust the zoom level
    }

    // Example button click handlers
    public void updateLocation1() {
        updateMap(8.952067365149873, 125.49264693662136, "Libertad butuan city bud promotory eco park");
    }

    public void updateLocation2() {
        updateMap(8.954204171924449, 125.49625218080097, "Libertad butuan city balangay site museum");
    }

    public void updateLocation3() {
        updateMap(8.999651229449013, 125.48436998265288, "Libertad butuan city magellan anchorage");
    }










    private void updateGPS() {
        //get permission from the user to track GPS-------------------------------------
        //get the current location of user from the fuse clieat ---------------------------------------------------
        //Updet the ui--------------------------------------------------------------------------
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Motour_SETA.this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
// values of location into ui components
                    updateUIValues(location);
                    currentLocation = location;
                    Toast.makeText(Motour_SETA.this, "location access", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "Check Your Internet", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }

    }
    private void updateUIValues(Location location) {
        if (location != null) {
            lat.setText(String.valueOf(location.getLatitude()));
            lon.setText(String.valueOf(location.getLongitude()));


        } else {

            Toast.makeText(this, "Check Your InterNet", Toast.LENGTH_SHORT).show();

        }
        Geocoder geocoder = new Geocoder(Motour_SETA.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                add.setText(address.getLocality());
                Toast.makeText(this, "location access", Toast.LENGTH_SHORT).show();




            }
        }catch (Exception e){
            Toast.makeText(this, "unable to get street and address", Toast.LENGTH_SHORT).show();

        }
    }






    private void showYesNoAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set the title and message for the AlertDialog
        alertDialogBuilder.setTitle("Confirm Action");
        alertDialogBuilder.setMessage("Are you sure you want to perform this action?");

        // Set the "Yes" button and its click listener
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               StartSet_A_sendData();
            }
        });

        // Set the "No" button and its click listener
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the "No" button click here
                // You can cancel the action or perform an alternative action
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }








    public void StartSet_A_sendData(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        String status= "pending";
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("ALL PASUNDO").child(firebaseAuth.getUid());

        ALL_REQEUST_GETER_SETER users = new ALL_REQEUST_GETER_SETER(
                firebaseAuth.getUid(),
                name,
                number,
                add.getText().toString().trim(),
                lat.getText().toString().trim(),
                lon.getText().toString().trim(),
                "",
                "",
                "",
                "",
                "Motour Set A",
               IdOfFavoriteDriver,
               "",
                "230",
                status);
        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                showSuccessAlertDialog();
                Toast.makeText(Motour_SETA.this, "Request sent  Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void showSuccessAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set the title and message for the AlertDialog
        alertDialogBuilder.setTitle("Request sent successfully");
        alertDialogBuilder.setMessage("Press OK to start searching a driver");

        // Set the "OK" button and its click listener
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (IdOfFavoriteDriver.equals("")) {
                 IdOfFavoriteDriver = "";
                    relative1.setVisibility(View.INVISIBLE);
                    relative2.setVisibility(View.VISIBLE);
                }else {
                    ContactingYourDriver.setText("Contacting Your Driver");
                    relative1.setVisibility(View.INVISIBLE);
                    relative2.setVisibility(View.VISIBLE);
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }











    private void fetchAcceptedDetails(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ALL ACCEPT").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

showDriverDetailsAlertDialog(dataSnapshot.child("name").getValue(String.class),
                             dataSnapshot.child("contact").getValue(String.class));

progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    // The data doesn't exist.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                // Handle any errors that may occur.
            }
        });
    }


    // Method to show an AlertDialog with driver details
    private void showDriverDetailsAlertDialog(String driverName, String driverNumber) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set the title and message for the AlertDialog
        alertDialogBuilder.setTitle("Driver Details");
        alertDialogBuilder.setMessage("Driver will call you as soon as possible. Please stand by.\n\nName: " + driverName + "\nNumber: " + driverNumber);
        // Set the "OK" button and its click listener
        alertDialogBuilder.setPositiveButton("OK", null );

        // Create and show the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }








    //--------------------------------------------------------------upate all status------------------------------------------
    public void CancelUpdateStatus(String newStatusValue) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Request Abording...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ALL PASUNDO").child(firebaseAuth.getUid()).child("status");

        mDatabaseReference.setValue(newStatusValue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Motour_SETA.this, "Request Aborded", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                 relative1.setVisibility(View.VISIBLE);
                 relative2.setVisibility(View.INVISIBLE);
                } else {
              }
            }
        }) ;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
