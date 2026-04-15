package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Mechaincs_Order_Details extends AppCompatActivity {
    TextView tvcustomeraddres,tvcutomerissues,customerbudget,tvcustomercontact;
    Button btncommin;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference bookingsRef = database.getReference("Bookings");
    DatabaseReference currentOrdersRef = database.getReference("CurrentOrders");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechaincs_order_details);
        tvcustomeraddres= findViewById(R.id.tvcustomeraddres);
        tvcutomerissues = findViewById(R.id.tvcutomerissues);
        customerbudget = findViewById(R.id.customerbudget);
        tvcustomercontact = findViewById(R.id.tvcustomercontact);
        Intent intent = getIntent();
        String orderAddress = intent.getStringExtra("orderAddress");
        String orderContact = intent.getStringExtra("orderContact");
        String orderBudget = intent.getStringExtra("orderBudget");
        String orderIssues = intent.getStringExtra("orderIssues");
        String contact = intent.getStringExtra("mcontact");
        tvcustomeraddres.setText(orderAddress);
        tvcutomerissues.setText(orderIssues);
        customerbudget.setText(orderBudget);
        tvcustomercontact.setText(orderContact);
        btncommin = findViewById(R.id.btncommin);
        btncommin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingsRef.orderByChild("mcontact").equalTo(contact).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            // Get the booking details
                            String address = childSnapshot.child("address").getValue(String.class);
                            String contact = childSnapshot.child("contact").getValue(String.class);
                            String max = childSnapshot.child("max").getValue(String.class);
                            String mcontact = childSnapshot.child("mcontact").getValue(String.class);
                            String min = childSnapshot.child("min").getValue(String.class);
                            String mname = childSnapshot.child("mname").getValue(String.class);
                            String vehicalIssues = childSnapshot.child("vehicalIssues").getValue(String.class);
                            String vehicaleDec = childSnapshot.child("vehicaleDec").getValue(String.class);

                            // Create a new CurrentOrder object
                            currentorderlist currentOrder = new currentorderlist(address, contact, max, mcontact, min, mname, vehicalIssues, vehicaleDec);

                            // Save the current order to the CurrentOrders table
                            currentOrdersRef.child(mcontact).setValue(currentOrder);

                            // Remove the booking from the Bookings table
                            childSnapshot.getRef().removeValue();
                        }
                        Toast.makeText(Mechaincs_Order_Details.this, "Booking moved to CurrentOrders", Toast.LENGTH_SHORT).show();
                        // Add any additional logic after the booking is moved
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error if needed
                    }
                });
            }
        });


    }
}