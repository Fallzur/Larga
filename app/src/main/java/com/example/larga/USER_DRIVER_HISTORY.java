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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class USER_DRIVER_HISTORY extends AppCompatActivity {


    RecyclerView RequestRecyclerView;
    USER_DRIVER_HISTORY_ADAPTER USERDRIVER_history_adapter;
FirebaseAuth firebaseAuth;

Intent i;
String idOfUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zdriver_history_view_historry);
        overridePendingTransition(0, 0);
        i = getIntent();
        idOfUser = i.getStringExtra("id");


        //---------------------------------------------------------------fetch all request---------------------------------------//

        RequestRecyclerView= findViewById(R.id.HISTORY_recyclerView);
        RequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();


// Create a query using the DatabaseReference and orderByChild
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(idOfUser);
        Query query = databaseReference.orderByChild("motorType").equalTo(i.getStringExtra("type"));

// Create FirebaseRecyclerOptions using the query
        FirebaseRecyclerOptions<ALL_REQEUST_GETER_SETER> options =
                new FirebaseRecyclerOptions.Builder<ALL_REQEUST_GETER_SETER>()
                        .setQuery(query, ALL_REQEUST_GETER_SETER.class)
                        .build();

        USERDRIVER_history_adapter = new USER_DRIVER_HISTORY_ADAPTER(options);
        RequestRecyclerView.setAdapter(USERDRIVER_history_adapter);


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
        USERDRIVER_history_adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isOnline();
        USERDRIVER_history_adapter.stopListening();
    }


}