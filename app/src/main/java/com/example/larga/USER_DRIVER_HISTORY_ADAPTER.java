package com.example.larga;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class USER_DRIVER_HISTORY_ADAPTER extends FirebaseRecyclerAdapter<ALL_REQEUST_GETER_SETER, USER_DRIVER_HISTORY_ADAPTER.myViewHolder> {
    ProgressBar progressBar;
Intent i;
    public USER_DRIVER_HISTORY_ADAPTER(@NonNull FirebaseRecyclerOptions<ALL_REQEUST_GETER_SETER> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ALL_REQEUST_GETER_SETER model) {


holder.name.setText(model.getName());

 holder.itemView.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         Intent intent = new Intent(holder.itemView.getContext(), USER_DRIVER_VIEW_HISTORY.class);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_history_viewlayout,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        private TextView name;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Hridername);



        }
    }

    @Override
    public void onDataChanged() {
        if (progressBar!=null){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
