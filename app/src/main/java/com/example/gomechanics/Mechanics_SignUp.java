package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class Mechanics_SignUp extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mechanicsRef;

    private EditText etMName, etMContact, etMAge, etMPassword;
    private Spinner spVehicleType;
    private Button btnMSignUp;
    private TextView tvSignIn;

    private static final Pattern PAK_PHONE_PATTERN = Pattern.compile("^03\\d{9}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanics_sign_up);

        initViews();
        setupSpinner();
        setListeners();
    }

    private void initViews() {
        etMName = findViewById(R.id.etMname);
        spVehicleType = findViewById(R.id.etMvehicaletype);
        etMContact = findViewById(R.id.etMcontact);
        etMAge = findViewById(R.id.etMage);
        etMPassword = findViewById(R.id.etMpassword);
        btnMSignUp = findViewById(R.id.btnMfinsih);
        tvSignIn = findViewById(R.id.tvSigin);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mechanicsRef = firebaseDatabase.getReference("Mechanics");
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.vehicle_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVehicleType.setAdapter(adapter);
    }

    private void setListeners() {
        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(Mechanics_SignUp.this, LoginScreen.class));
            finish();
        });

        btnMSignUp.setOnClickListener(v -> attemptSignup());
    }

    private void attemptSignup() {
        clearErrors();

        final String name = etMName.getText().toString().trim();
        final String vehicle = spVehicleType.getSelectedItem() != null
                ? spVehicleType.getSelectedItem().toString().trim()
                : "";
        final String contact = etMContact.getText().toString().trim();
        final String age = etMAge.getText().toString().trim();
        final String password = etMPassword.getText().toString().trim();

        if (!validateName(name)) return;
        if (!validateVehicle(vehicle)) return;
        if (!validateContact(contact)) return;
        if (!validateAge(age)) return;
        if (!validatePassword(password)) return;

        setLoadingState(true);

        mechanicsRef.orderByChild("contact").equalTo(contact).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setLoadingState(false);
                    etMContact.setError("This contact number is already registered");
                    etMContact.requestFocus();
                    return;
                }

                String userType = "Mechanic";
                StoringDataMechanic mechanic = new StoringDataMechanic(name, vehicle, contact, password, age, userType);

                mechanicsRef.child(contact).setValue(mechanic)
                        .addOnSuccessListener(unused -> {
                            setLoadingState(false);
                            Toast.makeText(Mechanics_SignUp.this, "Mechanic account created successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Mechanics_SignUp.this, LoginScreen.class));
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            setLoadingState(false);
                            Toast.makeText(Mechanics_SignUp.this, "Failed to create account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setLoadingState(false);
                Toast.makeText(Mechanics_SignUp.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearErrors() {
        etMName.setError(null);
        etMContact.setError(null);
        etMAge.setError(null);
        etMPassword.setError(null);
    }

    private boolean validateName(String value) {
        if (value.isEmpty()) {
            etMName.setError("Name is required");
            etMName.requestFocus();
            return false;
        }

        if (value.length() < 3) {
            etMName.setError("Name must be at least 3 characters");
            etMName.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateVehicle(String value) {
        if (value.isEmpty() || value.equals("Select vehicle experience")) {
            Toast.makeText(this, "Please select vehicle experience", Toast.LENGTH_SHORT).show();
            spVehicleType.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateContact(String value) {
        if (value.isEmpty()) {
            etMContact.setError("Contact number is required");
            etMContact.requestFocus();
            return false;
        }

        if (!PAK_PHONE_PATTERN.matcher(value).matches()) {
            etMContact.setError("Enter a valid number like 03XXXXXXXXX");
            etMContact.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateAge(String value) {
        if (value.isEmpty()) {
            etMAge.setError("Age is required");
            etMAge.requestFocus();
            return false;
        }

        try {
            int parsedAge = Integer.parseInt(value);
            if (parsedAge < 18 || parsedAge > 65) {
                etMAge.setError("Age must be between 18 and 65");
                etMAge.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etMAge.setError("Enter a valid age");
            etMAge.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validatePassword(String value) {
        if (value.isEmpty()) {
            etMPassword.setError("Password is required");
            etMPassword.requestFocus();
            return false;
        }


        if (value.length() < 6) {
            etMPassword.setError("Password must be at least 6 digits/characters");
            etMPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void setLoadingState(boolean isLoading) {
        btnMSignUp.setEnabled(!isLoading);
        btnMSignUp.setText(isLoading ? "Please wait..." : "Finish");
    }
}