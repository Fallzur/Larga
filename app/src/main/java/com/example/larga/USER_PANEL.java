package com.example.larga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class USER_PANEL extends AppCompatActivity {
    ImageView btn1, btn2, btn3, btn4, btn5, btn6;
    TextView btnHome,btnLocation,btnHistory;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;


    TextView name,number,email,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        overridePendingTransition(0, 0);

        // Initialize your ImageViews
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);


        name = findViewById(R.id.info_name);
        number = findViewById(R.id.info_contact);
        email = findViewById(R.id.info_email);

        logout = findViewById(R.id.LogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(USER_PANEL.this, Logout.class);
                startActivity(intent);
                finish();
            }
        });



        progressDialog = new ProgressDialog(this);

        btnHome = findViewById(R.id.btn_home);
        btnLocation = findViewById(R.id.btn_favorate);
        btnHistory = findViewById(R.id.btn_history);




        //------------------------------------------------------------------main 3 Buttons----------------------------------
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(USER_PANEL.this, USER_PANEL.class);
                startActivity(intent);
            }
        });


        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(USER_PANEL.this, Favorate.class);
                startActivity(intent);
            }
        });


        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(USER_PANEL.this, History.class);
                startActivity(intent);
            }
        });



//------------------------------------------------------------------------------------6 buttons-----------------------------------------
        // Set OnClickListener for btn1
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(USER_PANEL.this, MapPahatid.class);
                intent.putExtra("MotorType","PASUNDO");
                startActivity(intent);
            }
        });

        // Set OnClickListener for btn2
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(USER_PANEL.this, ChooseMotorCycle.class);
                intent.putExtra("favoriteDriver","");
                startActivity(intent);
            }
        });

        // Set OnClickListener for btn3
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(USER_PANEL.this, MoTour.class);
                intent.putExtra("favoriteDriver","");
                startActivity(intent);
            }
        });

        // Set OnClickListener for btn4
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(USER_PANEL.this, MapPahatid.class);
                intent.putExtra("favoriteDriver","");
                intent.putExtra("MotorType","PAPALIT");
                startActivity(intent);
            }
        });

        // Set OnClickListener for btn5
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click for btn5
                Intent intent = new Intent(USER_PANEL.this, Promav.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for btn6
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click for btn6
                Intent intent = new Intent(USER_PANEL.this, Customcare.class);
                startActivity(intent);
            }
        });





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
               name.setText(user.getName().trim());
               number.setText(user.getContact().trim());
               email.setText(user.getEmail().trim());
               progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onBackPressed() {

    }
}
