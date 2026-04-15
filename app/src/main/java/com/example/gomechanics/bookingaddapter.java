package com.example.gomechanics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class bookingaddapter extends RecyclerView.Adapter<bookingaddapter.bookingViewHolder> {
    public bookingaddapter(Context context, ArrayList<priviousbookinglist> list) {
        this.context = context;
        this.list = list;
    }

    Context context;
    ArrayList<priviousbookinglist> list;

    @NonNull
    @Override
    public bookingaddapter.bookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.previousbookinglist , parent,false);
        return new bookingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull bookingaddapter.bookingViewHolder holder, int position) {
        priviousbookinglist booking  = list.get(position);

        holder.VehicaleDec.setText(booking.getVehicaleDec());
        holder.VehicalIssues.setText(booking.getVehicalIssues());
        holder.Vehicalecontact.setText(booking.getContact());
        holder.MechanicName.setText(booking.getMname());
        holder.response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "jobcompleted", Toast.LENGTH_SHORT).show();
//                // Handle the button click here
//
//                String contactNo = mechanic.getContact(); // Get the contact number
//
//                // Create an intent and pass the contact number as an extra
//                Intent intent = new Intent(context, address_Information.class);
//                intent.putExtra("contactNo", contactNo);
//                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class bookingViewHolder extends RecyclerView.ViewHolder
    {
        TextView VehicaleDec,VehicalIssues,Vehicalecontact,MechanicName;
        Button response;

        public bookingViewHolder(@NonNull View itemView) {
            super(itemView);
            VehicaleDec = itemView.findViewById(R.id.etVehicalDecription);
            VehicalIssues = itemView.findViewById(R.id.etVehicaleIssues);
            Vehicalecontact = itemView.findViewById(R.id.etVehicaleContact);
            response = itemView.findViewById(R.id.btnwaitingforresponse);
            MechanicName = itemView.findViewById(R.id.MechanicName);
        }
    }
}
