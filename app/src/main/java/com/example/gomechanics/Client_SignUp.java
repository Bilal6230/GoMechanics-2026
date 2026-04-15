package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class Client_SignUp extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersRef;

    private EditText etName, etVehicle, etContact, etPassword, etAddress;
    private Button btnSignUp;
    private TextView tvSignIn;

    private static final Pattern PAK_PHONE_PATTERN = Pattern.compile("^03\\d{9}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_sign_up);

        initViews();
        setListeners();
    }

    private void initViews() {
        tvSignIn = findViewById(R.id.tvSigin);
        etName = findViewById(R.id.etname);
        etVehicle = findViewById(R.id.etvehicaletype);
        etContact = findViewById(R.id.etcontact);
        etPassword = findViewById(R.id.etpassword);
        etAddress = findViewById(R.id.etaddress);
        btnSignUp = findViewById(R.id.btnfinsih);

        firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference("Users");
    }

    private void setListeners() {
        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(Client_SignUp.this, LoginScreen.class));
            finish();
        });

        btnSignUp.setOnClickListener(v -> attemptSignup());
    }

    private void attemptSignup() {
        clearErrors();

        final String name = etName.getText().toString().trim();
        final String vehicle = etVehicle.getText().toString().trim();
        final String contact = etContact.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String address = etAddress.getText().toString().trim();

        if (!validateName(name)) return;
        if (!validateVehicle(vehicle)) return;
        if (!validateContact(contact)) return;
        if (!validatePassword(password)) return;
        if (!validateAddress(address)) return;

        setLoadingState(true);

        usersRef.orderByChild("contact").equalTo(contact).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setLoadingState(false);
                    etContact.setError("This contact number is already registered");
                    etContact.requestFocus();
                    return;
                }

                String userType = "User";
                StoringData user = new StoringData(name, vehicle, contact, password, address, userType);

                usersRef.child(contact).setValue(user)
                        .addOnSuccessListener(unused -> {
                            setLoadingState(false);
                            Toast.makeText(Client_SignUp.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Client_SignUp.this, LoginScreen.class));
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            setLoadingState(false);
                            Toast.makeText(Client_SignUp.this, "Failed to create account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setLoadingState(false);
                Toast.makeText(Client_SignUp.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearErrors() {
        etName.setError(null);
        etVehicle.setError(null);
        etContact.setError(null);
        etPassword.setError(null);
        etAddress.setError(null);
    }

    private boolean validateName(String value) {
        if (TextUtils.isEmpty(value)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return false;
        }

        if (value.length() < 3) {
            etName.setError("Name must be at least 3 characters");
            etName.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateVehicle(String value) {
        if (TextUtils.isEmpty(value)) {
            etVehicle.setError("Vehicle type is required");
            etVehicle.requestFocus();
            return false;
        }

        if (value.length() < 2) {
            etVehicle.setError("Enter a valid vehicle type");
            etVehicle.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateContact(String value) {
        if (TextUtils.isEmpty(value)) {
            etContact.setError("Contact number is required");
            etContact.requestFocus();
            return false;
        }

        if (!PAK_PHONE_PATTERN.matcher(value).matches()) {
            etContact.setError("Enter a valid number like 03XXXXXXXXX");
            etContact.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validatePassword(String value) {
        if (TextUtils.isEmpty(value)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        if (value.length() < 6) {
            etPassword.setError("Password must be at least 6 digits/characters");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateAddress(String value) {
        if (TextUtils.isEmpty(value)) {
            etAddress.setError("Address is required");
            etAddress.requestFocus();
            return false;
        }

        if (value.length() < 10) {
            etAddress.setError("Please enter a complete address");
            etAddress.requestFocus();
            return false;
        }

        return true;
    }

    private void setLoadingState(boolean isLoading) {
        btnSignUp.setEnabled(!isLoading);
        btnSignUp.setText(isLoading ? "Please wait..." : "Finish");
    }
}