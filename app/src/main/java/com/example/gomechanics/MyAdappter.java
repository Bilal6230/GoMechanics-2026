package com.example.gomechanics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdappter extends RecyclerView.Adapter<MyAdappter.MyViewHolder> {
    public MyAdappter(Context context, ArrayList<Mechanic> list) {
        this.context = context;
        this.list = list;
    }

    Context context;
    ArrayList<Mechanic> list;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.mechanic_list_view , parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {

        Mechanic mechanic  = list.get(position);
        holder.name.setText(mechanic.getName());
        holder.age.setText(mechanic.getAge());
        holder.expert.setText(mechanic.getExpert());
        holder.btnHireMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click here

                String contactNo = mechanic.getContact(); // Get the contact number
                String name = mechanic.getName();
                // Create an intent and pass the contact number as an extra
                Intent intent = new Intent(context, address_Information.class);
                intent.putExtra("contactNo", contactNo);
                intent.putExtra("name", name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name , age, expert;
        Button btnHireMe;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvMname);
            age = itemView.findViewById(R.id.tvMage);
            expert = itemView.findViewById(R.id.tvMexpert);
            btnHireMe = itemView.findViewById(R.id.btnhireme);
        }
    }
}
