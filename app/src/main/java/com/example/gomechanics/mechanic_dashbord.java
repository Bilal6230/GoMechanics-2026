package com.example.gomechanics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class mechanic_dashbord extends AppCompatActivity {
    CardView cvneworders,cvcurrentorders, cvcompeletedorders, cvlogout;
    TextView tvMWelcomeNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_dashbord);
        cvneworders = findViewById(R.id.CardViewNewOrders);
        cvcurrentorders = findViewById(R.id.CardViewNewCurrentOrders);
        cvcompeletedorders = findViewById(R.id.CardViewCompletedOrders);
        Intent intent = getIntent();
        String contact = intent.getStringExtra("contact");


        cvlogout = findViewById(R.id.CardViewLogOut);
        cvneworders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mechanic_dashbord.this, Mechanic_new_orders.class);
                intent.putExtra("contact", contact);
                startActivity(intent);
            }
        });
        cvcurrentorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mechanic_dashbord.this, Mechains_Privious_Orders.class);
                startActivity(intent);
//                Toast.makeText(mechanic_dashbord.this, contact, Toast.LENGTH_SHORT).show();
            }
        });
        cvcompeletedorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mechanic_dashbord.this, Completed_Orders.class);
                startActivity(intent);
            }
        });
        cvlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mechanic_dashbord.this, MechanicSignIn.class);
                startActivity(intent);
            }
        });
        tvMWelcomeNote = findViewById(R.id.tvMWelcomeNote);
        String name = getIntent().getStringExtra("name");
        tvMWelcomeNote.setText("Welcome "  + name);
    }
}