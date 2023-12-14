package com.example.larga;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Favorate_ADAPTER extends FirebaseRecyclerAdapter<FAVORITE_GETER_SETER, Favorate_ADAPTER .myViewHolder>  {


    public Favorate_ADAPTER (@NonNull FirebaseRecyclerOptions<FAVORITE_GETER_SETER> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Favorate_ADAPTER .myViewHolder holder, int position, @NonNull FAVORITE_GETER_SETER model) {

        holder.statusOfDriver.setText(model.getStatus().toString().trim());
        holder.nameOfDriver.setText(model.getNameOfDriver().toString().trim());
        holder.availavility.setText(model.getAvailavility().toString().trim());

        if (model.getStatus().equals("active")){
            holder.statusOfDriver.setTextColor(Color.GREEN);
        }

        if (model.getAvailavility().equals("available")) {
            holder.availavility.setTextColor(Color.WHITE);
            holder.availavility.setBackgroundColor(Color.GREEN);
            //-----------------------------------------setClickable
  holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          // Show a dialog with three choices
          AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
          builder.setTitle("Choose an option")
                  .setItems(new CharSequence[]{"PASUNDO", "PAPALIT","Motour"}, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                          // Open activity based on the selected choice
                          switch (i) {
                              case 0:
                                  // Open Activity for Option 1
                                  Intent intentOption1 = new Intent(holder.itemView.getContext(), MapPahatid.class);
                                  intentOption1.putExtra("MotorType","PASUNDO");
                                  intentOption1.putExtra("favoriteDriver",model.getIdOfDriver().toString().trim());
                                  holder.itemView.getContext().startActivity(intentOption1);
                                  break;
                              case 1:
                                  // Open Activity for Option 3
                                  Intent intentOption2 = new Intent(holder.itemView.getContext(), MapPahatid.class);
                                  intentOption2.putExtra("MotorType","PAPALIT");
                                  intentOption2.putExtra("favoriteDriver",model.getIdOfDriver().toString().trim());
                                  holder.itemView.getContext().startActivity(intentOption2);
                                  break;
                              case 2:
                                  // Open Activity for Option 3
                                  Intent intentOption3 = new Intent(holder.itemView.getContext(), MoTour.class);
                                  intentOption3.putExtra("MotorType","Motour");
                                  intentOption3.putExtra("favoriteDriver",model.getIdOfDriver().toString().trim());
                                  holder.itemView.getContext().startActivity(intentOption3);
                                  break;
                          }
                      }
                  })
                  .setCancelable(false) // Set to true if you want the dialog to be cancelable
                  .show();
      }
  });
        } else {
            holder.availavility.setTextColor(Color.WHITE);
            holder.availavility.setBackgroundColor(Color.RED);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(holder.itemView.getContext(), "This Driver not available", Toast.LENGTH_SHORT).show();
                }
            });
        }




    }
    @NonNull
    @Override
    public Favorate_ADAPTER .myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_favorate_viewlayout,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        private TextView nameOfDriver;
        private TextView statusOfDriver,availavility;


        public myViewHolder(View view) {
            super(view);

            nameOfDriver = view.findViewById(R.id.ridername);
            statusOfDriver = view.findViewById(R.id.TypeOfstatus);
            availavility = view.findViewById(R.id.Availability);

        }


    }
}

