package com.example.gomechanics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdappter extends RecyclerView.Adapter<MyAdappter.MyViewHolder> {

    Context context;
    ArrayList<Mechanic> list;

    public MyAdappter(Context context, ArrayList<Mechanic> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mechanic_list_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mechanic mechanic = list.get(position);

        String name = mechanic.getName();
        String age = mechanic.getAge();
        String expert = mechanic.getExpert();

        holder.name.setText(name != null && !name.trim().isEmpty() ? name : "Mechanic");
        holder.age.setText("Age: " + (age != null && !age.trim().isEmpty() ? age : "N/A"));
        holder.expert.setText("Experience: " + (expert != null && !expert.trim().isEmpty() ? expert : "Not provided"));

        holder.btnHireMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contactNo = mechanic.getContact();
                String mechanicName = mechanic.getName();

                Intent intent = new Intent(context, address_Information.class);
                intent.putExtra("contactNo", contactNo);
                intent.putExtra("name", mechanicName);
                intent.putExtra("experience", mechanic.getExpert());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, expert;
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