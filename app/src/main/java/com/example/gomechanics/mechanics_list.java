package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class mechanics_list extends AppCompatActivity {
ImageButton imgbtnBack;
Button  btnhireme;
RecyclerView RV;
FirebaseDatabase FB;
DatabaseReference DR;
MyAdappter myAdappter;
ArrayList <Mechanic> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanics_list);
        imgbtnBack = findViewById(R.id.ImageBtnBack);

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mechanics_list.this, User_Dashbord.class);
                startActivity(intent);
            }
        });


        RV = findViewById(R.id.RVmechaniclist);
        FB = FirebaseDatabase.getInstance();
        DR = FB.getReference("Mechanics");
        RV.setHasFixedSize(true);
        RV.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdappter = new MyAdappter(this,list);
        RV.setAdapter(myAdappter);
        DR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the list before adding mechanics

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String name = childSnapshot.child("name").getValue(String.class);
                    String age = childSnapshot.child("age").getValue(String.class);
                    String expert = childSnapshot.child("vehical").getValue(String.class);
                    String contact = childSnapshot.child("contact").getValue(String.class);

                    // Create a Mechanic object and add it to the list
                    Mechanic mechanic = new Mechanic(name, age, expert,contact);
                    list.add(mechanic);

                }

                myAdappter.notifyDataSetChanged(); // Notify the adapter of the data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });

   }
}