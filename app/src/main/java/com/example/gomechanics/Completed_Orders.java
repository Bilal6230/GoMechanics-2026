package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Completed_Orders extends AppCompatActivity {
    private RecyclerView recyclerView;
    private completedorderadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_orders);
        ImageButton imgbtnBack =findViewById(R.id.ImageBtnBack);
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Completed_Orders.this, mechanic_dashbord.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.RVcompletedorders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query completedRecordQuery = FirebaseDatabase.getInstance().getReference("Completed_Record");
        completedRecordQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<completedorderlist> completedOrdersList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    completedorderlist order = snapshot.getValue(completedorderlist.class);
                    completedOrdersList.add(order);
                }

                adapter = new completedorderadapter(Completed_Orders.this, completedOrdersList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });


    }
}