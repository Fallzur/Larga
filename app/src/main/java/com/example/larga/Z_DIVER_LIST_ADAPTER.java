package com.example.larga;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Z_DIVER_LIST_ADAPTER extends FirebaseRecyclerAdapter<ALL_REQEUST_GETER_SETER, Z_DIVER_LIST_ADAPTER.myViewHolder> {
    ProgressBar progressBar;
    String motorType ,uid;

    FirebaseAuth firebaseAuth ;

    public Z_DIVER_LIST_ADAPTER(@NonNull FirebaseRecyclerOptions<ALL_REQEUST_GETER_SETER> options) {
        super(options);
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Drivers");
        databaseReference.keepSynced(true);
        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               User_geterSeter user = snapshot.getValue(User_geterSeter.class);
                motorType = user.getTypeMotor();
                uid = user.id;


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ALL_REQEUST_GETER_SETER model) {


        if (model.getStatus().equals("accepted") || model.getStatus().equals("cancel")) {
            holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(0, 0)); // This sets the width and height to 0
        } else {
            // Handle other conditions or default behavior here
            if (model.getMotorType().equals("PAPALIT") && model.getStatus().equals("pending") ||
                    model.getMotorType().equals("PASUNDO") && model.getStatus().equals("pending") ||
                    model.getMotorType().equals("PAPALIT") && model.getStatus().equals("pending") ||
                    model.getMotorType().equals("") && model.getStatus().equals("pending") ||
                    model.getMotorType().equals(motorType) && model.getStatus().equals("pending") ||
                    model.getMotorType().equals("Motour Set A") && model.getStatus().equals("pending") ||
                    model.getMotorType().equals("Motour Set B")&& model.getStatus().equals("pending") ||
                    model.getMotorType().isEmpty() && model.getStatus().equals("pending") ||
                    model.getMotorType().equals("pending") && model.getStatus().equals("pending"))
            {

                holder.name.setText(model.getName().trim());
                holder.status.setText(model.getMotorType().trim());
                holder.number.setText(model.getContact().trim());
                holder.address.setText(model.getLocationA().trim());

                // Check if model.getIdOfDriver() is equal to uid
                if (model.getIdOfFavoriteDriver().equals("")) {
                    holder.name.setText(model.getName().trim());
                    holder.status.setText(model.getMotorType().trim());
                    holder.number.setText(model.getContact().trim());
                    holder.address.setText(model.getLocationA().trim());

                } else if (!model.getIdOfFavoriteDriver().equals(uid)) {
                    holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(0, 0)); // This sets the width and height to 0
                }


            }else{
                holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(0, 0)); // This sets the width and height to 0

            }
        }





        holder.viewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), Z_VIEW_REQUEST.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("name", model.getName());
                intent.putExtra("number", model.getContact());

                intent.putExtra("locationA", model.getLocationA());
                intent.putExtra("latA", model.getLatA());
                intent.putExtra("lonA", model.getLonA());

                intent.putExtra("locationB", model.getLocationB());
                intent.putExtra("latB", model.getLatB());
                intent.putExtra("lonB", model.getLonB());
                intent.putExtra("type", model.getMotorType());
                intent.putExtra("Paymenttype", model.getPaymentType());
                intent.putExtra("papalitInfo", model.getPapalitInfo());
                intent.putExtra("tol", model.getTotal());

                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_driver_request_viewlayout,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        private TextView status,name,number,address;
        ImageView viewRequest;

RelativeLayout relativeLayout;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ridename);
            status = itemView.findViewById(R.id.ridestatus);
            number = itemView.findViewById(R.id.ridecontact);
            viewRequest = itemView.findViewById(R.id.rideView);
            address = itemView.findViewById(R.id.rideaddress);
            relativeLayout = itemView.findViewById(R.id.nameHolder);


        }
    }

    @Override
    public void onDataChanged() {
        if (progressBar!=null){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
