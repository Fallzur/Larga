package com.example.larga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MoTour extends AppCompatActivity {

Intent favoriteDriver;
String IdOfFavoriteDriver = "";

    RelativeLayout setA,setB,setC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_tour);
        overridePendingTransition(0, 0);

        setA = findViewById(R.id.Con_2);
        setB = findViewById(R.id.Con_3);

        //------intent get the id of favoritr driver------------
        favoriteDriver = getIntent();
        IdOfFavoriteDriver = favoriteDriver.getStringExtra("favoriteDriver");

        // Check if IdOfFavoriteDriver is empty or null
        if (IdOfFavoriteDriver == null || IdOfFavoriteDriver.isEmpty() || IdOfFavoriteDriver == "") {
          IdOfFavoriteDriver = "";
        } else {
            IdOfFavoriteDriver = favoriteDriver.getStringExtra("favoriteDriver");
        }

        setA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoTour.this, Motour_SETA.class);
                intent.putExtra("favoriteDriver",IdOfFavoriteDriver);
                startActivity(intent);
            }
        });


        setB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoTour.this, Motour_SETB.class);
                intent.putExtra("favoriteDriver",IdOfFavoriteDriver);
                startActivity(intent);
            }
        });







    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MoTour.this, USER_PANEL.class);
        intent.putExtra("favoriteDriver","");
        startActivity(intent);
    }
}