package com.example.larga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Favorate extends AppCompatActivity {
    TextView btnHome2,btnLocation2,btnHistory2;

    RecyclerView favorite_recyclerView;
    Favorate_ADAPTER favorate_adapter;

FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorate);
        overridePendingTransition(0, 0);



        btnHome2 = findViewById(R.id.btn_home2);
        btnLocation2 = findViewById(R.id.btn_favorate2);
        btnHistory2 = findViewById(R.id.btn_history2);


        //---------------------------------------------------------------fetch all request---------------------------------------//

        favorite_recyclerView= findViewById(R.id.favorate_recyclerView);
        favorite_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();
        String myId = firebaseAuth.getUid(); // Get the user's UID

        Query query = FirebaseDatabase.getInstance().getReference().child("ALL FAVORITE")
                .orderByChild("myid")
                .equalTo(myId); // Filter data where "MYID" equals the user's UID

        FirebaseRecyclerOptions<FAVORITE_GETER_SETER> options =
                new FirebaseRecyclerOptions.Builder<FAVORITE_GETER_SETER>()
                        .setQuery(query, FAVORITE_GETER_SETER.class)
                        .build();

        favorate_adapter = new Favorate_ADAPTER(options);
        favorite_recyclerView.setAdapter(favorate_adapter);








        //------------------------------------------------------------------main 3 Buttons----------------------------------
        btnHome2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Favorate.this, USER_PANEL.class);
                startActivity(intent);
            }
        });


        btnLocation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Favorate.this, Favorate.class);
                startActivity(intent);
            }
        });


        btnHistory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Favorate.this, History.class);
                startActivity(intent);
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
        favorate_adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isOnline();
       favorate_adapter.stopListening();
    }
}