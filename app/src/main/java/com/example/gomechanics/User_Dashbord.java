package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User_Dashbord extends AppCompatActivity {
    CardView cvhiremechanic , cvbooking , cvmechainc;
    TextView tvWelcomeNote;
    RecyclerView RV;
    FirebaseDatabase FB;
    DatabaseReference DR,DRMechanic;
    ImageButton imgbtnBack;
    recentmechanicadapter recentadapter;
    ArrayList <recentmechaniclist> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashbord);
        cvhiremechanic = findViewById(R.id.CardViewHireMechanic);
        cvbooking = findViewById(R.id.CardViewBooking);
//        cvmechainc = findViewById(R.id.CardViewMechainc);
        cvhiremechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_Dashbord.this, mechanics_list.class);
                startActivity(intent);
            }
        });
        cvbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_Dashbord.this, Previous_Booking.class);
                startActivity(intent);
            }
        });
//        cvmechainc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(User_Dashbord.this, address_Information.class);
//                startActivity(intent);
//            }
//        });
        tvWelcomeNote = findViewById(R.id.tvWelcomeNote);
        String name = getIntent().getStringExtra("name");
        tvWelcomeNote.setText("Welcome "+name);
        RV = findViewById(R.id.lyRVrecentmechainc);
        FB = FirebaseDatabase.getInstance();
        DR = FB.getReference("RecentMechanic");
//        DRMechanic = FB.getReference("Mechanics")
        RV.setHasFixedSize(true);
        RV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        list = new ArrayList<>();
        recentadapter = new recentmechanicadapter(this, list);
        RV.setAdapter(recentadapter);
        DR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                list.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String RMname = childSnapshot.child("mname").getValue(String.class);
                    recentmechaniclist recentadapter = new recentmechaniclist(RMname);
                    list.add(recentadapter);
                }
                recentadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}