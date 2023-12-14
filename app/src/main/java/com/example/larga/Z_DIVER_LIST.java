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
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Z_DIVER_LIST extends AppCompatActivity {
    ImageView btnHome2,btnMap2,btnHistory2,driverPof;

    RecyclerView RequestRecyclerView;
    Z_DIVER_LIST_ADAPTER list_adapter;
    ImageView contactBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zdiver_list);
        overridePendingTransition(0, 0);

        btnHome2 = findViewById(R.id.driver_home2);
        btnMap2 = findViewById(R.id.driver_map2);
        btnHistory2 = findViewById(R.id.driver_history2);



        btnHome2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DIVER_LIST.this, Z_DIVER_PANEL.class);
                startActivity(intent);
            }
        });


        btnMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DIVER_LIST.this, Z_DIVER_LIST.class);
                startActivity(intent);
            }
        });


        btnHistory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DIVER_LIST.this, Z_DRIVER_HISTORY.class);
                startActivity(intent);
            }
        });

        //---------------------------------------------------------------fetch all request---------------------------------------//

        RequestRecyclerView= findViewById(R.id.driverlist_recyclerView);
        RequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ALL_REQEUST_GETER_SETER> options =
                new FirebaseRecyclerOptions.Builder<ALL_REQEUST_GETER_SETER>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("ALL PASUNDO"),ALL_REQEUST_GETER_SETER.class)
                        .build();

        list_adapter = new Z_DIVER_LIST_ADAPTER(options);
        RequestRecyclerView.setAdapter(list_adapter);

    }





    public void onBackPressed() {

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
        list_adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isOnline();
        list_adapter.stopListening();
    }

}