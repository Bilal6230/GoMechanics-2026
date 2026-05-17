package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecentMechanicsActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvEmptyRecentMechanics;
    private RecyclerView rvRecentMechanics;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference recentMechanicsRef;

    private recentmechanicadapter recentAdapter;
    private ArrayList<recentmechaniclist> recentMechanicsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_mechanics);

        btnBack = findViewById(R.id.btnBackRecentMechanics);
        tvEmptyRecentMechanics = findViewById(R.id.tvEmptyRecentMechanics);
        rvRecentMechanics = findViewById(R.id.rvRecentMechanics);

        btnBack.setOnClickListener(v -> finish());

        firebaseDatabase = FirebaseDatabase.getInstance();
        recentMechanicsRef = firebaseDatabase.getReference("RecentMechanic");

        recentMechanicsList = new ArrayList<>();
        recentAdapter = new recentmechanicadapter(this, recentMechanicsList);

        rvRecentMechanics.setHasFixedSize(true);
        rvRecentMechanics.setLayoutManager(new LinearLayoutManager(this));
        rvRecentMechanics.setAdapter(recentAdapter);

        loadRecentMechanics();
    }

    private void loadRecentMechanics() {
        recentMechanicsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recentMechanicsList.clear();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String mechanicName = childSnapshot.child("mname").getValue(String.class);

                    if (mechanicName != null && !mechanicName.trim().isEmpty()) {
                        recentmechaniclist item = new recentmechaniclist(mechanicName);
                        recentMechanicsList.add(item);
                    }
                }

                recentAdapter.notifyDataSetChanged();

                if (recentMechanicsList.isEmpty()) {
                    tvEmptyRecentMechanics.setText("No recent mechanics found");
                    tvEmptyRecentMechanics.setVisibility(TextView.VISIBLE);
                } else {
                    tvEmptyRecentMechanics.setVisibility(TextView.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        RecentMechanicsActivity.this,
                        "Failed to load recent mechanics",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}