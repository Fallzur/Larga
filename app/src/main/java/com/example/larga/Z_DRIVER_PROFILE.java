package com.example.larga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Z_DRIVER_PROFILE extends AppCompatActivity {
Button logout;
ProgressDialog progressDialog;
FirebaseAuth firebaseAuth;
TextView name;
String Sname,Scontact;

    private TextView countTextViewAccept, getCountTextViewCancel ,countTrip, countEarn;
    private DatabaseReference allCancelReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zdriver_profile);
        overridePendingTransition(0, 0);
 countTextViewAccept = findViewById(R.id.countAccept);
 getCountTextViewCancel = findViewById(R.id.countCancelation);
 countTrip = findViewById(R.id.countTrip);
 countEarn = findViewById(R.id.countEarnings);

        firebaseAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logoutBtn);
        name = findViewById(R.id.driverName);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Z_DRIVER_PROFILE.this,Logout.class);
                startActivity(intent);
                finish();
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth= FirebaseAuth.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Drivers");
        databaseReference.keepSynced(true);
        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_geterSeter user = snapshot.getValue(User_geterSeter.class);
                name.setText(user.getName().toString().trim());
                Sname = user.getName();
                Scontact = user.getContact();
                progressDialog.dismiss();
                count1();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });











        allCancelReference = FirebaseDatabase.getInstance().getReference("ALL EARN");

        // Create a ValueEventListener to fetch and calculate the total value
        allCancelReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalValue = 0.0; // Use double to handle decimal numbers

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String myid = snapshot.child("myid").getValue(String.class);
                    if (myid != null && myid.equals(firebaseAuth.getUid())) {
                        String valueString = snapshot.child("total").getValue(String.class);

                        // Convert the valueString to a double
                        double value = Double.parseDouble(valueString);

                        totalValue += value;
                    }
                }

                // Round off the totalValue to the nearest whole number
                long roundedTotalValue = Math.round(totalValue);

                String totalValueString = String.valueOf(roundedTotalValue);
                countEarn.setText(totalValueString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if needed
            }
        });



    }



    private void  count1(){
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference(Sname);

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                String countString = String.valueOf(count);
                countTextViewAccept.setText(countString);
                countTrip.setText(countString);
                count2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
    }
    private void count2() {
        // Replace "ALL CANCEL" with the appropriate path to your data
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("ALL CANCEL");

        // Replace "your_uid" with the actual UID you want to count
        Query uidQuery = usersReference.orderByChild("id").equalTo(firebaseAuth.getUid());

        uidQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                String countString = String.valueOf(count);
                getCountTextViewCancel.setText(countString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
    }




}