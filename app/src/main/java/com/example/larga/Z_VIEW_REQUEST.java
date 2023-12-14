package com.example.larga;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;

import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.larga.databinding.ActivityZviewRequestBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Z_VIEW_REQUEST extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {
    Polyline polyline;
    LatLng pointB,pointA,pointC;
    Location lastlocation;
    private LatLng waypoint; // Waypoint
    private LatLng destinationLatLng; // Destination

    LatLng lastLocationlatLng;
    LocationRequest locationRequest;

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 50;
    private static final int PERMISSIONS_FINE_LOCATION = 99;

    GoogleApiClient googleApiClient;

    private GoogleMap mMap;
    private ActivityZviewRequestBinding binding;
    Intent i;
    Button cancel ,accept,arrived,startTrip,cancelTrip,finishTripe,cash,Motouraccept;
    String Sid, Sname, Scontact, SlocationA, SlatA, SlonA, SlocationB, SlatB, SlonB,Spayment,Stype,SpapalitInfo,Stotal;
    String SlatC,SlonC,SlocationC;
    TextView tvname, tvnumber, tvlocationA, tvlocationB,papalitInfo;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String driverName,driverContact;
RelativeLayout relativeInfo;
TextView total,type;

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.black};

Intent favoriteDriver;
String IdOfFavoriteDriver;


    String randomString;

    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        binding = ActivityZviewRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        polylines = new ArrayList<>();
        accept = findViewById(R.id.accept_button);
        Motouraccept = findViewById(R.id.motour_button);
        total = findViewById(R.id.totalcost);
        type = findViewById(R.id.driverTittle2);

        arrived = findViewById(R.id.arrive_button);
        startTrip = findViewById(R.id.startTrip_button);
        cancelTrip = findViewById(R.id.cancelTripe_button);
        finishTripe = findViewById(R.id.finish_button);
        cash = findViewById(R.id.cashRecieve_button);

        tvname = findViewById(R.id.D_name);
        tvnumber = findViewById(R.id.D_number);
        tvlocationA = findViewById(R.id.L_locationA);
        tvlocationB = findViewById(R.id.L1_locationB);

        i = getIntent();
        Sid = i.getStringExtra("id");
        Sname = i.getStringExtra("name");
        tvname.setText(Sname);
        Scontact = i.getStringExtra("number");
        tvnumber.setText(Scontact);

        SlocationA = i.getStringExtra("locationA");
        tvlocationA.setText(SlocationA);
        SlatA = i.getStringExtra("latA");
        SlonA = i.getStringExtra("lonA");

        SlocationB = i.getStringExtra("locationB");
        tvlocationB.setText(SlocationB);
        SlatB = i.getStringExtra("latB");
        SlonB = i.getStringExtra("lonB");

        Stype = i.getStringExtra("type");
        type.setText(Stype);
        Spayment = i.getStringExtra("Paymenttype");
        SpapalitInfo = i.getStringExtra("papalitInfo");

        Stotal = i.getStringExtra("tol");
        total.setText(Stotal+" "+"Pesos");

        papalitInfo = findViewById(R.id.InformationOfPapalit);
        papalitInfo.setText(i.getStringExtra("papalitInfo"));

        if (SpapalitInfo.isEmpty()){
            relativeInfo = findViewById(R.id.RL_information);
            relativeInfo.setVisibility(View.INVISIBLE);
        }else {
            relativeInfo = findViewById(R.id.RL_information);
            relativeInfo.setVisibility(View.VISIBLE);
        }
if (Stype.equals("Motour Set A") || Stype.equals("Motour Set B")){
    tvlocationB.setText(Stype);
    total.setText("230.00");
    accept.setVisibility(View.INVISIBLE);
    accept.setClickable(false);
    Motouraccept.setVisibility(View.VISIBLE);
    Motouraccept.setClickable(true);
} else if (Stype.equals("ADV") || Stype.equals("NMAX") || Stype.equals("AEROX")) {
    accept.setVisibility(View.VISIBLE);
    accept.setClickable(true);
    Motouraccept.setVisibility(View.INVISIBLE);
    Motouraccept.setClickable(false);
    Stype = "withmotor";
} else {
    accept.setVisibility(View.VISIBLE);
    accept.setClickable(true);
    Motouraccept.setVisibility(View.INVISIBLE);
    Motouraccept.setClickable(false);
}


        randomString = generateRandomString(12);


        //------intent get the id of favoritr driver------------
        favoriteDriver = getIntent();
        IdOfFavoriteDriver = favoriteDriver.getStringExtra("favoriteDriver");

        // Check if IdOfFavoriteDriver is empty or null
        if (IdOfFavoriteDriver == null || IdOfFavoriteDriver.isEmpty() || IdOfFavoriteDriver == "") {

        } else {
            IdOfFavoriteDriver = favoriteDriver.getStringExtra("favoriteDriver");
        }


        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);


        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);



        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendHistoryOfDriver();


            }
        });


        startTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                availability( "not available");
               startTrip.setClickable(false);
               startTrip.setVisibility(View.INVISIBLE);
               cancelTrip.setClickable(false);
               cancelTrip.setVisibility(View.INVISIBLE);
               cancel.setClickable(false);
               cancel.setVisibility(View.INVISIBLE);

               cash.setClickable(true);
               cash.setVisibility(View.VISIBLE);
               ACCEPT();
            }
        });

        cancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                availability( "available");
                CancelHistoryOfUser();


            }
        });

        arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrived.setClickable(false);
                arrived.setVisibility(View.INVISIBLE);

                startTrip.setClickable(true);
                startTrip.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                cancel.setClickable(false);
                cancelTrip.setVisibility(View.VISIBLE);
                cancelTrip.setClickable(true);
            }
        });



        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRoute(pointB);
              accept.setClickable(false);
              accept.setVisibility(View.INVISIBLE);
              cancel.setVisibility(View.INVISIBLE);
              cancel.setClickable(false);

              startTrip.setClickable(false);
              cancelTrip.setClickable(false);
              arrived.setVisibility(View.VISIBLE);
                updateStatus("accepted");
            }
        });


        Motouraccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Motouraccept.setClickable(false);
                Motouraccept.setVisibility(View.INVISIBLE);
                accept.setClickable(false);
                accept.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                cancel.setClickable(false);

                startTrip.setClickable(false);
                cancelTrip.setClickable(false);
                arrived.setVisibility(View.VISIBLE);
                updateStatus("accepted");
            }
        });



        cancel = findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_VIEW_REQUEST.this, Z_DIVER_LIST.class);
                startActivity(intent);
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        //----------------------------------------------------------------GET THE DATA OF TRIVER--------------------------
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth= FirebaseAuth.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Drivers");
        databaseReference.keepSynced(true);
        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_geterSeter user = snapshot.getValue(User_geterSeter.class);
                driverName = user.getName().toString().trim();
                driverContact = user.getContact().toString().trim();
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.z_requestmap);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check if latitude and longitude values are valid for both points
        if (isValidLatLng(SlatA, SlonA) && isValidLatLng(SlatB, SlonB)) {
            // LatLng for Point A
             pointA = new LatLng(Double.parseDouble(SlatA), Double.parseDouble(SlonA));

            // LatLng for Point B
            pointB = new LatLng(Double.parseDouble(SlatB), Double.parseDouble(SlonB));

            // Marker for Point A
            mMap.addMarker(new MarkerOptions().position(pointA).title(SlocationA)).setTitle("pickup location  " +SlocationA);

            // Marker for Point B
            mMap.addMarker(new MarkerOptions().position(pointB).title(SlocationB)).setTitle("Destination  "+SlocationB);



            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);


            // Add this code to automatically zoom to the user's current location
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (lastKnownLocation != null) {
                LatLng currentLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

            }
        }else {

            if (type.getText().toString().trim().equals("Motour Set A")){

                // Existing code for adding markers
                mMap.addMarker(new MarkerOptions().position(new LatLng(8.952099159587851, 125.492689851965)).title("")).setTitle("location1 ");
                mMap.addMarker(new MarkerOptions().position(new LatLng(8.954267737594224, 125.4962628878145)).title("")).setTitle("location2 ");
                mMap.addMarker(new MarkerOptions().position(new LatLng(8.999672422961002, 125.48435925381695)).title("")).setTitle("location3 ");
// LatLngs for the markers
                LatLng MARKA = new LatLng(8.952099159587851, 125.492689851965);
                LatLng MARKB = new LatLng(8.954267737594224, 125.4962628878145);
                LatLng MARKC = new LatLng(8.999672422961002, 125.48435925381695);
// Create a LatLngBounds that includes all markers
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(MARKA);
                builder.include(MARKB);
                builder.include(MARKC);
                LatLngBounds bounds = builder.build();
// Move the camera to include all markers with padding
                int padding = 100; // Adjust the padding as needed
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
// Routing code
                Routing routing = new Routing.Builder()
                        .key("AIzaSyC1z9hkQLxjjLIspZP_8WwmPC6hfCVSSng")
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener(this)
                        .alternativeRoutes(false)
                        .waypoints(MARKA, MARKB, MARKC)
                        .build();
                routing.execute();
            }else {
                // Existing code for adding markers
                mMap.addMarker(new MarkerOptions().position(new LatLng(8.972173444744817, 125.5383803519654)).title("")).setTitle("location1 ");
                mMap.addMarker(new MarkerOptions().position(new LatLng(8.954235966175625, 125.49629509614462)).title("")).setTitle("location2 ");
                mMap.addMarker(new MarkerOptions().position(new LatLng(8.947834442747997, 125.54060193847276)).title("")).setTitle("location3 ");
// LatLngs for the markers
                LatLng MarkA = new LatLng(8.972173444744817, 125.5383803519654);
                LatLng MarkB = new LatLng(8.954235966175625, 125.49629509614462);
                LatLng MarkC = new LatLng(8.947834442747997, 125.54060193847276);
// Create a LatLngBounds that includes all markers
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(MarkA);
                builder.include(MarkB);
                builder.include(MarkC);
                LatLngBounds bounds = builder.build();
// Move the camera to include all markers with padding
                int padding = 100; // Adjust the padding as needed
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
// Routing code
                Routing routing = new Routing.Builder()
                        .key("AIzaSyC1z9hkQLxjjLIspZP_8WwmPC6hfCVSSng")
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener(this)
                        .alternativeRoutes(false)
                        .waypoints(MarkA, MarkB, MarkC)
                        .build();
                routing.execute();

            }





        }


    }


    protected synchronized void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

    }



    // Helper method to check if latitude and longitude values are valid
    private boolean isValidLatLng(String latitude, String longitude) {
        try {
            double lat = Double.parseDouble(latitude);
            double lon = Double.parseDouble(longitude);
            return (-90 <= lat && lat <= 90) && (-180 <= lon && lon <= 180);
        } catch (NumberFormatException e) {
            return false;
        }
    }



    public void updateStatus(String newStatusValue) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ALL PASUNDO").child(Sid).child("status");

        mDatabaseReference.setValue(newStatusValue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Z_VIEW_REQUEST.this, "Request Accepted", Toast.LENGTH_SHORT).show();
                    sendDataForAccept();
                } else {
                    Toast.makeText(Z_VIEW_REQUEST.this, "Accepting error", Toast.LENGTH_SHORT).show();
                }
            }
        }) ;

    }






    private void sendHistoryOfDriver() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid()).child(randomString);

        ALL_REQEUST_GETER_SETER users = new ALL_REQEUST_GETER_SETER(
                Sid,//user id
                Sname,
                Scontact,
                SlocationA,
                SlatA,
                SlonA,
                SlocationB,
                SlatB,
                SlonB,
                Spayment,
                Stype,
                IdOfFavoriteDriver,
                SpapalitInfo,
                Stotal,
                "");
        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sendHistoryOfUser();
               progressDialog.dismiss();
            }
        });

    }



    private void sendHistoryOfUser() {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(Sid).child(randomString);

        ALL_REQEUST_GETER_SETER users = new ALL_REQEUST_GETER_SETER(
                firebaseAuth.getUid(),//driver id
                driverName,
                driverContact,
                SlocationA,
                SlatA,
                SlonA,
                SlocationB,
                SlatB,
                SlonB,
                Spayment,
                Stype,
                IdOfFavoriteDriver,
                SpapalitInfo,
                Stotal,
                "");
        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              progressDialog.dismiss();
                TotalErnings();
                showTransactionCompleteAlertDialog();
            }
        });

    }



    private void CancelHistoryOfUser() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("ALL CANCEL").child(randomString);

        ALL_REQEUST_GETER_SETER users = new ALL_REQEUST_GETER_SETER(
                firebaseAuth.getUid(),//driver id
                driverName,
                driverContact,
                SlocationA,
                SlatA,
                SlonA,
                SlocationB,
                SlatB,
                SlonB,
                Spayment,
                Stype,
                IdOfFavoriteDriver,
                SpapalitInfo,
                Stotal,
                "");
        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                CancelUpdateStatus("cancel");
              progressDialog.dismiss();
            }
        });

    }




    private void sendDataForAccept() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("ALL ACCEPT").child(Sid);

        ALL_REQEUST_GETER_SETER users = new ALL_REQEUST_GETER_SETER(
                firebaseAuth.getUid(),//driver id
                driverName,
                driverContact,
                SlocationA,
                SlatA,
                SlonA,
                SlocationB,
                SlatB,
                SlonB,
                Spayment,
                Stype,
                IdOfFavoriteDriver,
                SpapalitInfo,
                Stotal,
                "");
        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
            }
        });

    }
    private void TotalErnings() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("ALL EARN").child(randomString);

        ALL_EARN_GETER_SETER users = new ALL_EARN_GETER_SETER(
                firebaseAuth.getUid(),
                Sid,
                Stotal);
        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               progressDialog.dismiss();
            }
        });

    }
    private void ACCEPT() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(driverName).child(randomString);

        ALL_EARN_GETER_SETER users = new ALL_EARN_GETER_SETER(
                firebaseAuth.getUid(),
                Sid,
                Stotal);
        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             progressDialog.dismiss();
            }
        });

    }









    private void showTransactionCompleteAlertDialog() {
        availability("available");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set the title and message for the AlertDialog
        alertDialogBuilder.setTitle("Transaction Complete");
        alertDialogBuilder.setMessage("Your transaction has been completed successfully.");

        // Set the "OK" button and its click listener
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Z_VIEW_REQUEST.this, Z_DIVER_LIST.class);
                startActivity(intent);
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Set the background color to black
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));

        alertDialog.show();
    }







    //--------------------------------------------------------------upate all status------------------------------------------
    public void CancelUpdateStatus(String newStatusValue) {
        availability( "available");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Request Abording...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ALL PASUNDO").child(Sid).child("status");

        mDatabaseReference.setValue(newStatusValue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Z_VIEW_REQUEST.this, "Trip Aborded", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(Z_VIEW_REQUEST.this, Z_DIVER_LIST.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(Z_VIEW_REQUEST.this, "Request Aborded error", Toast.LENGTH_SHORT).show();
                }
            }
        }) ;

    }




    private  void availability( String status){
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

                        availabilityRef.setValue(status);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the database operation
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Handle the back button press
    }





    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1500);
        locationRequest.setFastestInterval(1500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        lastlocation = location;

        lastLocationlatLng = new LatLng(location.getLatitude(), location.getLongitude());


        mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLocationlatLng));

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }










    private void getRoute(LatLng destinationLatLng) {

        Routing routing = new Routing.Builder()
                .key("AIzaSyC1z9hkQLxjjLIspZP_8WwmPC6hfCVSSng")
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(pointA, destinationLatLng)
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }



    public static String generateRandomString(int length) {
        // Define the characters to be used in the random string
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Use SecureRandom for better randomness
        SecureRandom random = new SecureRandom();

        // StringBuilder to build the random string
        StringBuilder sb = new StringBuilder(length);

        // Generate random string by appending random characters
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        // Convert StringBuilder to String and return
        return sb.toString();
    }


}
