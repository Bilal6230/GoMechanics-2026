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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Previous_Booking extends AppCompatActivity {
    RecyclerView RV;
    FirebaseDatabase FB;
    DatabaseReference DR,DRMechanic;
    ImageButton imgbtnBack;
    MyAdappter bookingadapter;
    ArrayList <priviousbookinglist> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_booking);
        imgbtnBack = findViewById(R.id.ImageBtnBack);
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Previous_Booking.this, User_Dashbord.class);
                startActivity(intent);
            }
        });


        RV = findViewById(R.id.lyRVpriviousbooking);
        FB = FirebaseDatabase.getInstance();
        DR = FB.getReference("Bookings");
//        DRMechanic = FB.getReference("Mechanics")
        RV.setHasFixedSize(true);
        RV.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        bookingaddapter bookingadapter = new bookingaddapter(this, list);
        RV.setAdapter(bookingadapter);
        DR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                list.clear(); // Clear the list before adding mechanics
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String VehicaleDec = childSnapshot.child("vehicaleDec").getValue(String.class);
                    String VehicalIssues = childSnapshot.child("vehicalIssues").getValue(String.class);
                    String Vehicalecontact = childSnapshot.child("mcontact").getValue(String.class);
                    String Mname = childSnapshot.child("mname").getValue(String.class);

                    // Create a Mechanic object and add it to the list
                    priviousbookinglist booking = new priviousbookinglist(VehicaleDec, VehicalIssues, Vehicalecontact,Mname);
                    list.add(booking);
                }
                bookingadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}