package com.example.larga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class History extends AppCompatActivity {
    TextView btnHome1,btnLocation1,btnHistory1;


    RelativeLayout PASUNDO,MOTOUR,PASUNDOwithMOTORPICK,cart;


    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String type;
    final String[] selectedChoice = {""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        overridePendingTransition(0, 0);

        firebaseAuth = FirebaseAuth.getInstance();

        btnHome1 = findViewById(R.id.btn_home1);
        btnLocation1 = findViewById(R.id.btn_favorate1);
        btnHistory1 = findViewById(R.id.btn_history1);

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
                type = user.getTypeMotor().toString().trim();

                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //------------------------------------------------------------------main 3 Buttons----------------------------------
        btnHome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(History.this, USER_PANEL.class);
                startActivity(intent);
            }
        });


        btnLocation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(History.this, Favorate.class);
                startActivity(intent);
            }
        });


        btnHistory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(History.this, History.class);
                startActivity(intent);
            }
        });



        PASUNDO = findViewById(R.id.RelativeSearch_ic);
        MOTOUR = findViewById(R.id.RelativeFlag_ic);
        PASUNDOwithMOTORPICK = findViewById(R.id.RelativeMotor_ic);
        cart = findViewById(R.id.RelativeCart);



        PASUNDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(History.this, USER_DRIVER_HISTORY.class);
                intent.putExtra("id",firebaseAuth.getUid());
                intent.putExtra("type","PASUNDO");
                startActivity(intent);
            }
        });

        MOTOUR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Motour();
            }
        });

        PASUNDOwithMOTORPICK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(History.this, USER_DRIVER_HISTORY.class);
                intent.putExtra("id",firebaseAuth.getUid());
                intent.putExtra("type","withmotor");
                startActivity(intent);
            }
        });


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(History.this, USER_DRIVER_HISTORY.class);
                intent.putExtra("id",firebaseAuth.getUid());
                intent.putExtra("type","PAPALIT");
                startActivity(intent);
            }
        });






    }


    private  void  Motour(){
        // Declare a variable to store the selected choice


// Create an AlertDialog with two choices
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Set");
        builder.setItems(new CharSequence[]{"Tour A", "Tour B"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the choice selected
                if (which == 0) {
                    selectedChoice[0] = "Motour Set A";
                } else if (which == 1) {
                    selectedChoice[0] = "Motour Set B";
                }

                Intent intent = new Intent(History.this, USER_DRIVER_HISTORY.class);
                intent.putExtra("id",firebaseAuth.getUid());
                intent.putExtra("type",selectedChoice[0]);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Selected: " + selectedChoice[0], Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }
}