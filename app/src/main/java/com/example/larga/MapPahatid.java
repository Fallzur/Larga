package com.example.larga;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.larga.databinding.ActivityMapPahatidBinding;
import com.example.larga.directionhelpers.FetchURL;
import com.example.larga.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapPahatid extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,RoutingListener{



    private Handler handler;
    private LatLng lastLocationLatLng; // Set this to the last known location
    private LatLng destinationLatLng; // Set this to the destination location
    private long startTime;
    private Polyline routePolyline;
    GoogleApiClient googleApiClient;
    Location lastlocation;
    LatLng lastLocationlatLng;
    LocationRequest locationRequest;
    private static GoogleMap mMap;

    private ActivityMapPahatidBinding binding;

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 50;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    Location currentLocation;
    List<Location> saveLocation;
    LocationCallback locationCallBack;
    //Location request is a config file all setting related to fused

    FusedLocationProviderClient fusedLocationProviderClient;

    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    RelativeLayout relative1, relative2, relative3, relative4, relative5;
    RelativeLayout RL_paymentType;
    EditText PickUpLlocation, WhereToGo;

    String pointA, pointB;
    String PaymentType;
    String WheretoGo_lat, WhereToGo_lon;
    String name, number;

    TextView Type1, Type2, Type3, TVmotorType, Type4, Type5;

    ImageView imgAddFavor, imgAddedFavor;


    private String latitudeStr, longitudeStr, locationName;
    TextView lat, lon, add;
    TextView from, to, from1, to1;
    TextView back, papalitBack;
    TextView DriverName, DriverNumber;

    Button done, confirm, btnPay, findingCancel, arrive, papalitdone;
    TextView methodPay;
    Intent i,favoriteDriver;
    String IdOfFavoriteDriver = "";

    String IdOfDriver, Motortype, SpapalitInfo;
    EditText papalitAddInfo, papalitPickUplocation, papalitWhereToBuy;
    String type, TotalCost;
    TextView tvTotal;

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.orange};


    private RequestQueue requestQueue; // Declare this variable at the class level

 TextView ContactingMyDriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);

        firebaseAuth = FirebaseAuth.getInstance();

        binding = ActivityMapPahatidBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        requestQueue = Volley.newRequestQueue(this); // Initialize the request queue


        polylines = new ArrayList<>();

        relative1 = findViewById(R.id.usernavigation1);
        relative2 = findViewById(R.id.usernavigation2);
        relative3 = findViewById(R.id.usernavigation3);
        relative4 = findViewById(R.id.usernavigation4);

        relative5 = findViewById(R.id.papalit_navigation);//papalit

        tvTotal = findViewById(R.id.usertvtotal);
        ContactingMyDriver = findViewById(R.id.ContactingYourDriver1);

        ImageView gifImageView = findViewById(R.id.userloading);

// Load and display the GIF using Glide
        Glide.with(this)
                .asGif()  // Specify that the resource is a GIF
                .load(R.drawable.loading1)  // Replace with your GIF's resource name
                .into(gifImageView);  // Display it in the ImageView


        imgAddedFavor = findViewById(R.id.useradded);
        imgAddFavor = findViewById(R.id.useradd);


        Type1 = findViewById(R.id.userTittle1);
        Type2 = findViewById(R.id.userTittle2);
        Type3 = findViewById(R.id.userTittle3);
        Type4 = findViewById(R.id.papalit_Tittle1);
        Type5 = findViewById(R.id.userTittle4);

        TVmotorType = findViewById(R.id.usermotorTV);
        i = getIntent();
        if (i.getStringExtra("MotorType") == "PASUNDO") {
            type = i.getStringExtra("MotorType");
        } else if (i.getStringExtra("MotorType") == "PAPALIT") {
            type = i.getStringExtra("MotorType");
        }
        {
            type = i.getStringExtra("MotorType");
            Type1.setText(type);
            Type2.setText(type);
            Type3.setText(type);
            Type4.setText(type);
            Type5.setText(type);

        }

        //------intent get the id of favoritr driver------------
        favoriteDriver = getIntent();
        IdOfFavoriteDriver = favoriteDriver.getStringExtra("favoriteDriver");

        // Check if IdOfFavoriteDriver is empty or null
        if (IdOfFavoriteDriver == null || IdOfFavoriteDriver.isEmpty() || IdOfFavoriteDriver == "") {
            IdOfFavoriteDriver = "";
        } else {
            IdOfFavoriteDriver = favoriteDriver.getStringExtra("favoriteDriver");
        }







        DriverName = findViewById(R.id.userDriver_name);
        DriverNumber = findViewById(R.id.userDriver_number);


        PickUpLlocation = findViewById(R.id.userpickuplocation);
        PickUpLlocation.setFocusable(false);
        PickUpLlocation.setClickable(false);
        PickUpLlocation.setEnabled(false);

        WhereToGo = findViewById(R.id.userwhereTogo);


        papalitPickUplocation = findViewById(R.id.papalit_editText1);
        papalitPickUplocation.setFocusable(false);
        papalitPickUplocation.setClickable(false);
        papalitPickUplocation.setEnabled(false);

        papalitWhereToBuy = findViewById(R.id.papalit_editText2);
        papalitAddInfo = findViewById(R.id.papalit_editText3);


        methodPay = findViewById(R.id.usertvmethod);


        from = findViewById(R.id.userlocationa);
        to = findViewById(R.id.userlocationb);
        from1 = findViewById(R.id.userlocationa1);
        to1 = findViewById(R.id.userlocationb1);

        lat = findViewById(R.id.userlat);
        lon = findViewById(R.id.userlon);
        add = findViewById(R.id.useradd1);


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


        back = findViewById(R.id.ordinaryCancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackDialog();
            }
        });
        papalitBack = findViewById(R.id.papalit_canceTransuction);
        papalitBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackDialog();
            }
        });


        arrive = findViewById(R.id.userarrive_button);
        arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showThankYouDialog();
            }
        });

        imgAddFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgAddFavor.setVisibility(View.INVISIBLE);
                imgAddedFavor.setVisibility(View.VISIBLE);
                favoriteRdier();
            }
        });


        btnPay = findViewById(R.id.userpaymentbtn);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //  DrawRoute(lastLocationlatLng,destinationLatLng);
                getRoute(destinationLatLng);
                showPaymentOptionsDialog();
            }


        });


        findingCancel = findViewById(R.id.userfindingcancel);
        findingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelUpdateStatus("cancel");
            }
        });


        confirm = findViewById(R.id.userconfirmbtn);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IdOfFavoriteDriver.equals("")){
                    IdOfFavoriteDriver = "";
                }else {
                    ContactingMyDriver.setText("Contacting Your Driver");
                }
                relative1.setVisibility(View.INVISIBLE);
                relative2.setVisibility(View.INVISIBLE);
                relative4.setVisibility(View.VISIBLE);
                relative3.setVisibility(View.INVISIBLE);
                sendDataForPasundo();
            }
        });


        //-------------------------------------------------PAPALIT  DONE BUTTON----------------------------------------------------//
        papalitdone = findViewById(R.id.papalit_done);
        papalitdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (papalitPickUplocation.getText().toString().isEmpty()) {
                    papalitPickUplocation.setError("Pick up location is empty");
                    Toast.makeText(MapPahatid.this, "Pick up Location field is empty", Toast.LENGTH_SHORT).show();
                } else if (papalitWhereToBuy.getText().toString().isEmpty()) {
                    papalitWhereToBuy.setError("Where to go is empty");
                    Toast.makeText(MapPahatid.this, "Where field is empty", Toast.LENGTH_SHORT).show();
                } else if (papalitAddInfo.getText().toString().isEmpty()) {
                    papalitAddInfo.setError("Pick up location is empty");
                } else {

                    progressDialog = new ProgressDialog(MapPahatid.this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);

                    pointA = papalitPickUplocation.getText().toString().trim();
                    pointB = papalitWhereToBuy.getText().toString().trim();
                    SpapalitInfo = papalitAddInfo.getText().toString().trim();

                    from.setText(papalitPickUplocation.getText().toString().trim());
                    to.setText(papalitWhereToBuy.getText().toString().trim());

                    relative1.setVisibility(View.INVISIBLE);
                    relative2.setVisibility(View.VISIBLE);
                    relative3.setVisibility(View.INVISIBLE);
                    relative4.setVisibility(View.INVISIBLE);
                    relative5.setVisibility(View.INVISIBLE);

                    // Calculate the distance and fare
                    calculateDistanceAndFare(pointA, pointB);
                }
            }
        });


        //-------------------------------------------------PAHATID  DONE BUTTON----------------------------------------------------//
        done = findViewById(R.id.userdone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PickUpLlocation.getText().toString().isEmpty()) {
                    PickUpLlocation.setError("Pick up location is empty");
                    Toast.makeText(MapPahatid.this, "Pick up Location field is empty", Toast.LENGTH_SHORT).show();
                } else if (WhereToGo.getText().toString().isEmpty()) {
                    WhereToGo.setError("Where to go is empty");
                    Toast.makeText(MapPahatid.this, "Where field is empty", Toast.LENGTH_SHORT).show();
                } else if (PickUpLlocation.getText().toString().isEmpty() && WhereToGo.getText().toString().trim().isEmpty()) {
                    PickUpLlocation.setError("Pick up location is empty");
                    WhereToGo.setError("Where to go is empty");
                    Toast.makeText(MapPahatid.this, "Pick up location and Where to go field is empty", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog = new ProgressDialog(MapPahatid.this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);

                    pointA = PickUpLlocation.getText().toString().trim();
                    pointB = WhereToGo.getText().toString().trim();
                    SpapalitInfo = papalitAddInfo.getText().toString().trim();

                    from.setText(PickUpLlocation.getText().toString().trim());
                    to.setText(WhereToGo.getText().toString().trim());

                    relative1.setVisibility(View.INVISIBLE);
                    relative2.setVisibility(View.VISIBLE);
                    relative3.setVisibility(View.INVISIBLE);
                    relative4.setVisibility(View.INVISIBLE);
                    relative5.setVisibility(View.INVISIBLE);

                    // Calculate the distance and fare
                    calculateDistanceAndFare(pointA, pointB);
                }
            }
        });


        if (i.getStringExtra("MotorType").equals("PAPALIT")) {
            relative1.setVisibility(View.INVISIBLE);
            relative2.setVisibility(View.INVISIBLE);
            relative3.setVisibility(View.INVISIBLE);
            relative4.setVisibility(View.INVISIBLE);
            relative5.setVisibility(View.VISIBLE);
            done.setClickable(false);
        } else {
            relative1.setVisibility(View.VISIBLE);
            relative2.setVisibility(View.INVISIBLE);
            relative3.setVisibility(View.INVISIBLE);
            relative4.setVisibility(View.INVISIBLE);
            relative5.setVisibility(View.INVISIBLE);
            papalitdone.setClickable(false);

        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
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
        DatabaseReference alertRef = FirebaseDatabase.getInstance().getReference("/ALL PASUNDO/" + firebaseAuth.getUid() + "/status");
        alertRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if (status.equals("accepted")) {
                        relative1.setVisibility(View.INVISIBLE);
                        relative2.setVisibility(View.INVISIBLE);
                        relative3.setVisibility(View.VISIBLE);
                        relative4.setVisibility(View.INVISIBLE);
                        relative5.setVisibility(View.INVISIBLE);
                        progressDialog.dismiss();
                        fetchAcceptedDetails();
                    } else {
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
                .findFragmentById(R.id.usermap);
        mapFragment.getMapAsync(this);


    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

    }


    private void calculateDistanceAndFare(String pointA, String pointB) {
        Geocoder geocoder = new Geocoder(MapPahatid.this);

        try {
            List<Address> addressesA = geocoder.getFromLocationName(pointA, 1);
            List<Address> addressesB = geocoder.getFromLocationName(pointB, 1);

            if (addressesA != null && addressesA.size() > 0 && addressesB != null && addressesB.size() > 0) {
                Address addressA = addressesA.get(0);
                Address addressB = addressesB.get(0);

                double latitudeA = addressA.getLatitude();
                double longitudeA = addressA.getLongitude();

                double latitudeB = addressB.getLatitude();
                double longitudeB = addressB.getLongitude();

                WheretoGo_lat = Double.toString(latitudeB);
                WhereToGo_lon = Double.toString(longitudeB);
                // Calculate the distance in kilometers
                double distance = calculateDistanceInKm(latitudeA, longitudeA, latitudeB, longitudeB);

                // Calculate the fare based on 20 pesos per kilometer
                double fare = distance * 20;

                // Round the fare to two decimal places
                DecimalFormat df = new DecimalFormat("#.00");
                String roundedFare = df.format(fare);

                // Now, you have the calculated fare rounded to two decimal places
                TotalCost = roundedFare;
                tvTotal.setText(TotalCost + " " + "Pesos");
                Toast.makeText(MapPahatid.this, "Distance: " + distance + " km\nFare: " + roundedFare + " pesos", Toast.LENGTH_SHORT).show();

                // Add a marker to the map at the destination location (latitudeB, longitudeB)
                destinationLatLng = new LatLng(latitudeB, longitudeB);
                mMap.addMarker(new MarkerOptions().position(destinationLatLng).title("Destination"));


                progressDialog.dismiss();
            } else {
                // Handle the case where no location was found for the given names
                Toast.makeText(this, "INVALID LOCATION", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MapPahatid.this, USER_PANEL.class);
                startActivity(intent);
                Log.d("Location", "Location not found for: " + pointA + " or " + pointB);
                progressDialog.dismiss();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any exceptions that may occur during geocoding
        }
    }

    private double calculateDistanceInKm(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // Radius of the Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c; // Distance in kilometers
    }


    //---------------------------------------------GET THE CURRENT LOCATION OF LAT AND LON------------------------//
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                } else {
                    Toast.makeText(this, "This app require permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }

    }

    private void updateGPS() {
        //get permission from the user to track GPS-------------------------------------
        //get the current location of user from the fuse clieat ---------------------------------------------------
        //Updet the ui--------------------------------------------------------------------------
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapPahatid.this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
// values of location into ui components
                    updateUIValues(location);
                    currentLocation = location;
                    Toast.makeText(MapPahatid.this, "location access", Toast.LENGTH_SHORT).show();

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
        Geocoder geocoder = new Geocoder(MapPahatid.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                locationName = address.getLocality(); // Extract the locality or name of the location
                PickUpLlocation.setText(locationName);
                papalitPickUplocation.setText(locationName);
                add.setText(locationName);
                Toast.makeText(this, "location access", Toast.LENGTH_SHORT).show();


            }
        } catch (Exception e) {
            Toast.makeText(this, "unable to get street and address", Toast.LENGTH_SHORT).show();

        }
    }


    //-----------------------------------------------------RELATIVE 2-----------------------------------------------------------------------//

    private void showPaymentOptionsDialog() {
        final String[] paymentOptions = {"Gcash", "Cash"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Payment Method")
                .setItems(paymentOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String selectedOption = paymentOptions[which];
                        handlePaymentOption(selectedOption);


                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void handlePaymentOption(String selectedOption) {
        // You can now use the 'selectedOption' String as per your requirements.
        // For example, you can display a Toast message with the chosen option:
        PaymentType = selectedOption;
        methodPay.setText(PaymentType);

        confirm.setVisibility(View.VISIBLE);
        confirm.setClickable(true);

        btnPay.setVisibility(View.INVISIBLE);
        btnPay.setClickable(false);

        Toast.makeText(this, "You chose: " + selectedOption, Toast.LENGTH_SHORT).show();

    }


//-----------------------------------------------------Send request pasundo----------------------------//


    private void sendDataForPasundo() {
        String status = "pending";
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("ALL PASUNDO").child(firebaseAuth.getUid());

        ALL_REQEUST_GETER_SETER users = new ALL_REQEUST_GETER_SETER(
                firebaseAuth.getUid(),
                name,
                number,
                pointA,
                lat.getText().toString().trim(),
                lon.getText().toString().trim(),
                pointB,
                WheretoGo_lat,
                WhereToGo_lon,
                PaymentType,
                type,
                IdOfFavoriteDriver,
                SpapalitInfo,
                TotalCost,
                status);
        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MapPahatid.this, "Request sent  Successfully", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void fetchAcceptedDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ALL ACCEPT").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The data exists; you can retrieve it here.
                    DriverName.setText(dataSnapshot.child("name").getValue(String.class));
                    DriverNumber.setText(dataSnapshot.child("contact").getValue(String.class));
                    from1.setText(dataSnapshot.child("locationA").getValue(String.class));
                    to1.setText(dataSnapshot.child("locationB").getValue(String.class));
                    IdOfDriver = dataSnapshot.child("id").getValue(String.class);
                    Motortype = dataSnapshot.child("motorType").getValue(String.class);
                    TVmotorType.setText(Motortype);
                    Type3.setText(Motortype);
                    Type4.setText(Motortype);
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
                    Toast.makeText(MapPahatid.this, "Request Aborded", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(MapPahatid.this, USER_PANEL.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(MapPahatid.this, "Request Aborded error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void ArriveUpdateStatus(String newStatusValue) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ALL PASUNDO").child(firebaseAuth.getUid()).child("status");
        mDatabaseReference.setValue(newStatusValue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MapPahatid.this, "Transuction complete", Toast.LENGTH_SHORT).show();
                    ComleteUpdateStatus("complete");
                } else {
                    Toast.makeText(MapPahatid.this, "Request Aborded error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void ComleteUpdateStatus(String newStatusValue) {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ALL ACCEPT").child(firebaseAuth.getUid()).child("status");
        mDatabaseReference.setValue(newStatusValue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MapPahatid.this, "Transuction complete", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(MapPahatid.this, USER_PANEL.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MapPahatid.this, "Request Aborded error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    // ----------------------------------------------------uSER ADD THE FAVORITE RIDER-----------------------------------------------//
    private void favoriteRdier() {
        String status = "active", availavility = "notAvailable";
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Rider adding to favorite...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("ALL FAVORITE").child(firebaseAuth.getUid());

        FAVORITE_GETER_SETER users = new FAVORITE_GETER_SETER(
                firebaseAuth.getUid(),
                IdOfDriver,
                DriverName.getText().toString().trim(),
                DriverNumber.getText().toString().trim(),
                status,
                availavility);
        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MapPahatid.this, "Driver added on favorite Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    //------------------------------------------------MessageDialog-----------------------------------------------------
    public void showThankYouDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thank You");
        builder.setMessage("Thank you for using our app!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // You can perform some action on OK button click, or simply close the dialog
                ArriveUpdateStatus("arrived");
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void BackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setMessage("You want to cancel?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MapPahatid.this, USER_PANEL.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MapPahatid.this, USER_PANEL.class);
        intent.putExtra("favoriteDriver","");
        startActivity(intent);

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

    // Declare a boolean variable to control recentering
    private boolean allowRecentering = true;

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lastlocation = location;
        lastLocationlatLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Check if automatic recentering is allowed
        if (allowRecentering) {
            // You can adjust the zoom level as needed (e.g., 15f for a reasonable zoom)
            float zoomLevel = 15f;

            // Create a CameraPosition object with the desired zoom level
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(lastLocationlatLng)
                    .zoom(zoomLevel)
                    .build();

            // Use CameraUpdateFactory.newCameraPosition to update the camera with the new position and zoom level
            // Use animateCamera for a smooth zoom animation
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    // Call this method when you want to disable automatic recentering
    private void disableRecentering() {
        allowRecentering = false;
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
                .waypoints(lastLocationlatLng, destinationLatLng)
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

    private void erase(){
        for (Polyline line : polylines ){
            line.remove();
        }
        polylines.clear();
    }



}