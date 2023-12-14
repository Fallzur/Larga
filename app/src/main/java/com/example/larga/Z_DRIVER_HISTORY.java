package com.example.larga;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Z_DRIVER_HISTORY extends AppCompatActivity {
    ImageView btnHome1,btnMap1,btnHistory1,driverPof1;


    RelativeLayout PASUNDO,MOTOUR,PASUNDOwithMOTORPICK,cart;


    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String type;
    final String[] selectedChoice = {""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zdriver_history);
        overridePendingTransition(0, 0);

        firebaseAuth = FirebaseAuth.getInstance();

        btnHome1 = findViewById(R.id.driver_home1);
        btnMap1 = findViewById(R.id.driver_map1);
        btnHistory1 = findViewById(R.id.driver_history1);






        btnHome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DRIVER_HISTORY.this, Z_DIVER_PANEL.class);
                startActivity(intent);
            }
        });


        btnMap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DRIVER_HISTORY.this, Z_DIVER_LIST.class);
                startActivity(intent);
            }
        });


        btnHistory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DRIVER_HISTORY.this, Z_DRIVER_HISTORY.class);
                startActivity(intent);
            }
        });














        PASUNDO = findViewById(R.id.DRIVER_RelativeSearch_ic);
        MOTOUR = findViewById(R.id.DRIVER_RelativeFlag_ic);
        PASUNDOwithMOTORPICK = findViewById(R.id.DRIVER_RelativeMotor_ic);
        cart = findViewById(R.id.DRIVER_RelativeCart);


        PASUNDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DRIVER_HISTORY.this, USER_DRIVER_HISTORY.class);
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
                Intent intent = new Intent(Z_DRIVER_HISTORY.this, USER_DRIVER_HISTORY.class);
                intent.putExtra("id",firebaseAuth.getUid());
                intent.putExtra("type","withmotor");
                startActivity(intent);
            }
        });


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DRIVER_HISTORY.this, USER_DRIVER_HISTORY.class);
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

                Intent intent = new Intent(Z_DRIVER_HISTORY.this, USER_DRIVER_HISTORY.class);
                intent.putExtra("id",firebaseAuth.getUid());
                intent.putExtra("type",selectedChoice[0]);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Selected: " + selectedChoice[0], Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }
}