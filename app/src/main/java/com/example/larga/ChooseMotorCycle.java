package com.example.larga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ChooseMotorCycle extends AppCompatActivity {

    TextView nmax,aerox,adv,TypeOfMotor;
    ImageView Imgnmax,Imgaerox,Imgadv;

    String MotorCycleType;

    Button select;

    Intent favoriteDriver;
    String IdOfFavoriteDriver = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_motor_cycle);
        overridePendingTransition(0, 0);

        // Create an instance of OverlayHandler
        OverlayHandler overlayHandler = new OverlayHandler(this);

        // Show the overlay using the OverlayHandler
        overlayHandler.showOverlay();

        nmax = findViewById(R.id.TVnmax);
        aerox = findViewById(R.id.TVaerox);
        adv = findViewById(R.id.TVadv);
        TypeOfMotor = findViewById(R.id.MotorTypeChoosed);


        //------intent get the id of favoritr driver------------
        favoriteDriver = getIntent();
        IdOfFavoriteDriver = favoriteDriver.getStringExtra("favoriteDriver");

        // Check if IdOfFavoriteDriver is empty or null
        if (IdOfFavoriteDriver == null || IdOfFavoriteDriver.isEmpty() || IdOfFavoriteDriver == "") {
            IdOfFavoriteDriver ="";
        } else {
            IdOfFavoriteDriver = favoriteDriver.getStringExtra("favoriteDriver");
        }





        Imgnmax = findViewById(R.id.nmax);
        Glide.with(this)
                .load(R.drawable.nmax)  // Replace with your GIF's resource name
                .into(Imgnmax);  // Display it in the ImageView


        Imgaerox = findViewById(R.id.aerox);
        Glide.with(this)
                .load(R.drawable.aerox)  // Replace with your GIF's resource name
                .into(Imgaerox);  // Display it in the ImageView


        Imgadv = findViewById(R.id.adv);
        Glide.with(this)
                .load(R.drawable.adv)  // Replace with your GIF's resource name
                .into(Imgadv);  // Display it in the ImageView



        nmax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nmax.setTextColor(Color.parseColor("#FFA500"));
                MotorCycleType = nmax.getText().toString().trim();
                aerox.setTextColor(Color.WHITE);
                adv.setTextColor(Color.WHITE);

                Imgnmax.setVisibility(View.VISIBLE);
                Imgadv.setVisibility(View.INVISIBLE);
                Imgaerox.setVisibility(View.INVISIBLE);
                TypeOfMotor.setText(MotorCycleType);
            }
        });


        aerox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aerox.setTextColor(Color.parseColor("#FFA500"));
                MotorCycleType = aerox.getText().toString().trim();
                nmax.setTextColor(Color.WHITE);
                adv.setTextColor(Color.WHITE);

                Imgnmax.setVisibility(View.INVISIBLE);
                Imgadv.setVisibility(View.INVISIBLE);
                Imgaerox.setVisibility(View.VISIBLE);
                TypeOfMotor.setText(MotorCycleType);
            }
        });



        adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adv.setTextColor(Color.parseColor("#FFA500"));
                MotorCycleType = adv.getText().toString().trim();
                aerox.setTextColor(Color.WHITE);
                nmax.setTextColor(Color.WHITE);

                Imgnmax.setVisibility(View.INVISIBLE);
                Imgadv.setVisibility(View.VISIBLE);
                Imgaerox.setVisibility(View.INVISIBLE);
                TypeOfMotor.setText(MotorCycleType);
            }
        });



        select = findViewById(R.id.coN_button);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if MotorCycleType is empty before proceeding
                if (TextUtils.isEmpty(MotorCycleType)) {
                    // Handle the case where MotorCycleType is empty (show an error message, etc.)
                    Toast.makeText(ChooseMotorCycle.this, "Please select a motorcycle type", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ChooseMotorCycle.this, MapPahatid.class);
                    intent.putExtra("MotorType", MotorCycleType);
                    intent.putExtra("favoriteDriver",IdOfFavoriteDriver);
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChooseMotorCycle.this, USER_PANEL.class);
        intent.putExtra("favoriteDriver","");
        startActivity(intent);
    }
}