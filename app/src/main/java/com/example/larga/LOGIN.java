package com.example.larga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LOGIN extends AppCompatActivity {
TextView signin;
    ProgressDialog progressDialog;
    EditText password,email;
    FirebaseAuth firebaseAuth;

Button loginBTn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(0, 0);
        progressDialog = new ProgressDialog(this);
        firebaseAuth= FirebaseAuth.getInstance();


        signin = findViewById(R.id.signin);
        loginBTn = findViewById(R.id.login);
        password =  findViewById(R.id.passwordEditText);
        email =  findViewById(R.id.emailEditText);






        loginBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Login();
            }
        });



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LOGIN.this, SIGNIN.class);
                startActivity(intent);
            }
        });




    }


    private void Login() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        DatabaseReference driversRef = FirebaseDatabase.getInstance().getReference("Drivers");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");


        if (emailText.isEmpty() || passwordText.isEmpty()) {
            if (emailText.isEmpty()) {
                email.setError("Email is empty");
                email.setTextColor(Color.RED);
            }
            if (passwordText.isEmpty()) {
                password.setError("Password is empty");
                password.setTextColor(Color.RED);
            }
        } else if (passwordText.length() < 4) {
            password.setError("Password length should be more than 4 characters");
        } else {
            progressDialog.setMessage("Loging in...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            firebaseAuth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                String userId = firebaseAuth.getCurrentUser().getUid(); // Get the user's unique ID
                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                                rootRef.child("Drivers").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot driversSnapshot) {
                                        if (driversSnapshot.exists()) {
                                            progressDialog.dismiss();
                                            // The user is a Driver
                                            startDriverActivity();
                                        } else {
                                            rootRef.child("User").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot userSnapshot) {
                                                    if (userSnapshot.exists()) {
                                                        progressDialog.dismiss();
                                                        startUserActivity();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        // User data not found in either "Drivers" or "Users"
                                                        showError("User data not found.");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    // Handle any database error for "Users"
                                                    progressDialog.dismiss();
                                                    showError("Database error for Users: " + databaseError.getMessage());
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle any database error for "Drivers"
                                        progressDialog.dismiss();
                                        showError("Database error for Drivers: " + databaseError.getMessage());
                                    }
                                });
                            } else {
                                // Login failed
                                progressDialog.dismiss();
                                showError("Login failed: " + task.getException().getMessage());
                            }
                        }

                        private void startDriverActivity() {
                            Intent intent = new Intent(LOGIN.this, Z_DIVER_PANEL.class);
                            startActivity(intent);
                            finish();
                        }

                        private void startUserActivity() {
                            Intent intent = new Intent(LOGIN.this, USER_PANEL.class);
                            startActivity(intent);
                            finish();
                        }

                        private void showError(String errorMessage) {
                            Toast.makeText(LOGIN.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });



        }
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