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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Mechains_Privious_Orders extends AppCompatActivity {

    private RecyclerView recyclerView;
    private currentorderadapter orderAdapter;
    private ArrayList<currentorderlist> orderList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechains_privious_orders);
        ImageButton  imgbtnBack=findViewById(R.id.ImageBtnBack);

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mechains_Privious_Orders.this, mechanic_dashbord.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.RVcurrentorder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        orderAdapter = new currentorderadapter(this, orderList);
        recyclerView.setAdapter(orderAdapter);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CurrentOrders");
        // Read data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear(); // Clear the list before adding orders

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    currentorderlist order = snapshot.getValue(currentorderlist.class);
                    orderList.add(order);
                }

                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });

    }
}