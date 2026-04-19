package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User_Dashbord extends AppCompatActivity {

    CardView cvhiremechanic, cvbooking, cvmechainc;
    TextView tvWelcomeNote;
    RecyclerView RV;
    FirebaseDatabase FB;
    DatabaseReference DR, DRMechanic;
    recentmechanicadapter recentadapter;
    ArrayList list;
    Button btnLogoutUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashbord);

        cvhiremechanic = findViewById(R.id.CardViewHireMechanic);
        cvbooking = findViewById(R.id.CardViewBooking);
        tvWelcomeNote = findViewById(R.id.tvWelcomeNote);
        RV = findViewById(R.id.lyRVrecentmechainc);
        btnLogoutUser = findViewById(R.id.btnLogoutUser);

        String name = getIntent().getStringExtra("name");
        tvWelcomeNote.setText("Welcome " + name);

        cvhiremechanic.setOnClickListener(v -> {
            Intent intent = new Intent(User_Dashbord.this, mechanics_list.class);
            startActivity(intent);
        });

        cvbooking.setOnClickListener(view -> {
            Intent intent = new Intent(User_Dashbord.this, Previous_Booking.class);
            startActivity(intent);
        });

        btnLogoutUser.setOnClickListener(v -> showLogoutDialog());

        FB = FirebaseDatabase.getInstance();
        DR = FB.getReference("RecentMechanic");

        RV.setHasFixedSize(true);
        RV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        list = new ArrayList<>();
        recentadapter = new recentmechanicadapter(this, list);
        RV.setAdapter(recentadapter);

        DR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String RMname = childSnapshot.child("mname").getValue(String.class);
                    recentmechaniclist recentItem = new recentmechaniclist(RMname);
                    list.add(recentItem);
                }

                recentadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(User_Dashbord.this, LoginScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}