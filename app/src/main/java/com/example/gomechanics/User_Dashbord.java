package com.example.gomechanics;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class User_Dashbord extends AppCompatActivity {

    CardView cvhiremechanic, cvbooking, cvAIBot, cvRecentMechanics;
    TextView tvWelcomeNote;
    Button btnLogoutUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashbord);

        cvhiremechanic = findViewById(R.id.CardViewHireMechanic);
        cvbooking = findViewById(R.id.CardViewBooking);
        cvAIBot = findViewById(R.id.CardViewAIBot);
        cvRecentMechanics = findViewById(R.id.CardViewRecentMechanics);
        tvWelcomeNote = findViewById(R.id.tvWelcomeNote);
        btnLogoutUser = findViewById(R.id.btnLogoutUser);

        String name = getIntent().getStringExtra("name");
        if (name == null || name.trim().isEmpty()) {
            name = "User";
        }
        tvWelcomeNote.setText("Welcome " + name);

        cvhiremechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        cvRecentMechanics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_Dashbord.this, RecentMechanicsActivity.class);
                startActivity(intent);
            }
        });

        cvAIBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User_Dashbord.this, ChatBotActivity.class);
                startActivity(intent);
            }
        });

        btnLogoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
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