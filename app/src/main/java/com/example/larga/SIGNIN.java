package com.example.larga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SIGNIN extends AppCompatActivity {
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 50;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    Location currentLocation;
    List<Location> saveLocation;
    LocationCallback locationCallBack;
    //Location request is a config file all setting related to fused
    LocationRequest locationRequest;

    FusedLocationProviderClient fusedLocationProviderClient;

    ProgressDialog progressDialog;
    TextView lat,lon, add;


    String selectedVehicleType;
    String userType,address ;


    Button signInBtn;

    Spinner vehicleTypeSpinner ;
    Spinner userTypeSpinner;

    private FirebaseAuth firebaseAuth;

    EditText name,password,repassword,email,contact;

    String Sname,Spassword,Semail,Scontact,Slat,Slon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        overridePendingTransition(0, 0);

        progressDialog = new ProgressDialog(this);
        lat = findViewById(R.id.lat);
        lon = findViewById(R.id.lon);

        firebaseAuth = FirebaseAuth.getInstance();


        name = findViewById(R.id.signName);
        password = findViewById(R.id.signPassword);
        repassword = findViewById(R.id.signRePassword);
        email = findViewById(R.id.signEmail);
        contact = findViewById(R.id.signPhone);


       vehicleTypeSpinner = findViewById(R.id.vehicleTypeSpinner);
       userTypeSpinner = findViewById(R.id.userTypeSpinner);



        signInBtn = findViewById(R.id.submitButton);


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

     Slon = lon.getText().toString().trim();
     Slat = lat.getText().toString().trim();
                if (name.getText().toString().isEmpty()) {
                    name.setError("Email is empty");
                    Toast.makeText(SIGNIN.this, "Name field is empty", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Email is empty");
                    Toast.makeText(SIGNIN.this, "Username field is empty", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Email is empty");
                    Toast.makeText(SIGNIN.this, "Password field is empty", Toast.LENGTH_SHORT).show();
                } else if (repassword.getText().toString().isEmpty()) {
                    repassword.setError("Email is empty");
                    Toast.makeText(SIGNIN.this, "Repassword field is empty", Toast.LENGTH_SHORT).show();
                } else if (userType.isEmpty()) {
                    Toast.makeText(SIGNIN.this, "No userType selected", Toast.LENGTH_SHORT).show();
                } else if (selectedVehicleType.isEmpty()){
                    selectedVehicleType.equals("");
                } else if (address.isEmpty()) {
                    address.equals("");
                } else if (Slat.isEmpty()){
                    Toast.makeText(SIGNIN.this, "lat empty", Toast.LENGTH_SHORT).show();
                } else if (Slon.isEmpty()){
                    Toast.makeText(SIGNIN.this, "lon empty", Toast.LENGTH_SHORT).show();
                }
                else if (!password.getText().toString().equals(repassword.getText().toString())) {
                    repassword.setError("Passwords do not match");
                    password.setError("Passwords do not match");
                    Toast.makeText(SIGNIN.this, " Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    Semail = email.getText().toString().trim();
                    Spassword = password.getText().toString();
                    Sname = name.getText().toString().trim();
                    Scontact = contact.getText().toString().trim();

                    progressDialog.setMessage("Signing in Please wait.....");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);

                    firebaseAuth.createUserWithEmailAndPassword(Semail,Spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                sendDataForNewUser();
                            }else {
                                Toast.makeText(SIGNIN.this, "SignUp failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SIGNIN.this, "SignUp error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }



            }
        });















        //----------------------------type of vihicle------------------------------------

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.vehicle_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeSpinner.setAdapter(adapter);
        vehicleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedVehicleType = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        //----------------------------type of user------------------------------------

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.usertype, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       userTypeSpinner.setAdapter(adapter2);
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                userType = parentView.getItemAtPosition(position).toString();
                if (userType.equals("Drivers")){
                    vehicleTypeSpinner.setVisibility(View.VISIBLE);
                }else {
                    vehicleTypeSpinner.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


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

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        updateGPS();
    }


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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SIGNIN.this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
// values of location into ui components
                    updateUIValues(location);
                    currentLocation = location;
                    progressDialog.dismiss();
                    Toast.makeText(SIGNIN.this, "location access", Toast.LENGTH_SHORT).show();

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


            progressDialog.dismiss();
        } else {

            Toast.makeText(this, "Check Your InterNet", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        Geocoder geocoder = new Geocoder(SIGNIN.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            address = addresses.get(0).getAddressLine(0);
        }catch (Exception e){
            Toast.makeText(this, "unable to get street and address", Toast.LENGTH_SHORT).show();

        }


    }


    private void sendDataForNewUser() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(userType).child(firebaseAuth.getUid());

        User_geterSeter users = new User_geterSeter(
                firebaseAuth.getUid(),
                Sname,
                Spassword,
                Semail,
                Scontact,
                userType,
                selectedVehicleType,
                Slat,
                Slon,
                address);
        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SIGNIN.this, "Login Successfully", Toast.LENGTH_SHORT).show();
progressDialog.dismiss();

                if (userType.equals("Drivers")){
                    Intent intent = new Intent(SIGNIN.this, Z_DIVER_PANEL.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SIGNIN.this, USER_PANEL.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }



    public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable()){

            android.app.AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            alertDialog.setTitle("internet");
            alertDialog.setMessage("Check your connectivity and try again");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isOnline();
    }

}