package com.example.larga;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Logout extends AppCompatActivity {
    private static int SPLASH_TIMER=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        overridePendingTransition(0, 0);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(Logout.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        },SPLASH_TIMER);

    }

    public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable()){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
        FirebaseAuth firebaseAuth;
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        isOnline();
    }

    @Override
    protected void onStop() {
        super.onStop();

        isOnline();
    }
}