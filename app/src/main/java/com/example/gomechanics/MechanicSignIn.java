package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MechanicSignIn extends AppCompatActivity {

    TextView tvSiginAsUser,tvMRegister;
    FirebaseDatabase FB;
    DatabaseReference DR;
    EditText etMContactNumber,etMPassword;
    Button btnMSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_sign_in);
        etMContactNumber = findViewById(R.id.etMContactNumber);
        etMPassword = findViewById(R.id.etMPass);
        btnMSignIn = findViewById(R.id.btnMSignIN);
        tvSiginAsUser = findViewById(R.id.tvSiginAsUser);
        tvSiginAsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MechanicSignIn.this, LoginScreen.class);
                startActivity(intent);
            }
        });
        tvMRegister = findViewById(R.id.tvMRegisternow);
        tvMRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MechanicSignIn.this, Mechanics_SignUp.class);
                startActivity(intent);
            }
        });
        btnMSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Mcontactno = etMContactNumber.getText().toString();
                String Mpassword = etMPassword.getText().toString();

                FB = FirebaseDatabase.getInstance();
                DR = FB.getReference("Mechanics");
                Query check_contact = DR.orderByChild("contact").equalTo(Mcontactno);
                check_contact.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DataSnapshot userSnapshot = snapshot.child(Mcontactno);
                            if (userSnapshot.hasChild("password")) {
                                String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                                if (passwordFromDB != null && Mpassword.equals(passwordFromDB)) {
                                    String userType = userSnapshot.child("usertype").getValue(String.class);
                                    if (userType != null && userType.equals("Mechanic")) {
                                        String name = userSnapshot.child("name").getValue(String.class);
                                        Toast.makeText(MechanicSignIn.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MechanicSignIn.this, mechanic_dashbord.class);
                                        intent.putExtra("contact", Mcontactno);
                                        intent.putExtra("name", name);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(MechanicSignIn.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MechanicSignIn.this, "No password found for this user", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MechanicSignIn.this, "Invalid contact number", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                    }
                });
            }
        });

    }
}