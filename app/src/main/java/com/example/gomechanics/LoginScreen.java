package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginScreen extends AppCompatActivity {
    TextView tvRegister,tvSiginAsMecahnic;
    FirebaseDatabase FB;
    DatabaseReference DR;
    EditText etContactNumber,etPassword;
    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        etContactNumber = findViewById(R.id.etContactNumber);
        etPassword = findViewById(R.id.etPass);
        btnSignIn = findViewById(R.id.btnSignIN);
        tvSiginAsMecahnic = findViewById(R.id.tvSiginAsMecahnic);
        tvSiginAsMecahnic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreen.this, MechanicSignIn.class);
                startActivity(intent);
            }
        });
        tvRegister = findViewById(R.id.tvRegisternow);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(LoginScreen.this, view);
                popupMenu.inflate(R.menu.registration_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.menu_register_user) {
                            Intent intent = new Intent(LoginScreen.this, Client_SignUp.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.menu_register_mechanic) {
                            Intent intent = new Intent(LoginScreen.this, Mechanics_SignUp.class);
                            startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contactno = etContactNumber.getText().toString();
                String password = etPassword.getText().toString();

                FB = FirebaseDatabase.getInstance();
                DR = FB.getReference("Users");
                Query check_contact = DR.orderByChild("contact").equalTo(contactno);
                check_contact.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                            String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                            if (passwordFromDB != null && password.equals(passwordFromDB)) {
                                String userType = userSnapshot.child("usertype").getValue(String.class);
                                if (userType != null && userType.equals("User")) {
                                    String name = userSnapshot.child("name").getValue(String.class);
                                    Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginScreen.this, User_Dashbord.class);
                                    intent.putExtra("name", name);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(LoginScreen.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginScreen.this, "Invalid contact number", Toast.LENGTH_SHORT).show();
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