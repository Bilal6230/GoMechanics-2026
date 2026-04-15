package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Mechanic_new_orders extends AppCompatActivity {

    RecyclerView RVneworder;
    FirebaseDatabase FB;
    DatabaseReference DR;
    ImageButton imgbtnBack;
    neworderadapter orderAdapter;
    ArrayList<neworderslist> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_new_orders);
        Intent intent = getIntent();
        String contact = intent.getStringExtra("contact");
        Toast.makeText(this, contact, Toast.LENGTH_SHORT).show();


        ImageButton NewOrderimgbtnBack = findViewById(R.id.NewOrderImageBtnBack);
        NewOrderimgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mechanic_new_orders.this, mechanic_dashbord.class);
                startActivity(intent);
            }
        });

        RVneworder = findViewById(R.id.RVNewOrder);
        FB = FirebaseDatabase.getInstance();
        DR = FB.getReference("Bookings");
        RVneworder.setHasFixedSize(true);
        RVneworder.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        orderAdapter = new neworderadapter(this, list);
        RVneworder.setAdapter(orderAdapter);

        DR.orderByChild("mcontact").equalTo(contact).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the list before adding orders

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String orderAddress = childSnapshot.child("address").getValue(String.class);
                    String orderContact = childSnapshot.child("contact").getValue(String.class);
                    String orderBudget = childSnapshot.child("max").getValue(String.class);
                    String orderIssues = childSnapshot.child("vehicalIssues").getValue(String.class);

                    // Create a neworderslist object and add it to the list
                    neworderslist order = new neworderslist(orderAddress, orderContact, orderBudget, orderIssues,contact);
                    list.add(order);
                }

                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });
    }
}
