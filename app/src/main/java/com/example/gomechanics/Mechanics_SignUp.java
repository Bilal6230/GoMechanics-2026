package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText etMName;
    private EditText etMContact;
    private EditText etMPassword;
    private EditText etMCnic;
    private EditText etMVehicleType;
    private EditText etMWorkshop;
    private EditText etMAddress;
    private Button btnMSignUp;

    private static final Pattern PAK_PHONE_PATTERN = Pattern.compile("^03\\d{9}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanics_sign_up);

        initViews();
        initFirebase();
        setListeners();
    }

    private void initViews() {
        etMName = findViewById(R.id.etname);
        etMContact = findViewById(R.id.etcontact);
        etMPassword = findViewById(R.id.etpassword);
        etMCnic = findViewById(R.id.etcnic);
        etMVehicleType = findViewById(R.id.etvehicaletype);
        etMWorkshop = findViewById(R.id.etworkshop);
        etMAddress = findViewById(R.id.etaddress);
        btnMSignUp = findViewById(R.id.btnfinsih);
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        mechanicsRef = firebaseDatabase.getReference("Mechanics");
    }

    private void setListeners() {
        btnMSignUp.setOnClickListener(v -> attemptSignup());
    }

    private void attemptSignup() {
        clearErrors();

        final String name = etMName.getText().toString().trim();
        final String contact = etMContact.getText().toString().trim();
        final String password = etMPassword.getText().toString().trim();
        final String cnic = etMCnic.getText().toString().trim();
        final String vehicleType = etMVehicleType.getText().toString().trim();
        final String workshop = etMWorkshop.getText().toString().trim();
        final String address = etMAddress.getText().toString().trim();

        if (!validateName(name)) return;
        if (!validateVehicleType(vehicleType)) return;
        if (!validateContact(contact)) return;
        if (!validatePassword(password)) return;
        if (!validateCnic(cnic)) return;
        if (!validateWorkshop(workshop)) return;
        if (!validateAddress(address)) return;

        setLoadingState(true);

        mechanicsRef.orderByChild("contact").equalTo(contact)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            setLoadingState(false);
                            etMContact.setError("This contact number is already registered");
                            etMContact.requestFocus();
                            return;
                        }

                        String userType = "Mechanic";

                        /*
                         * Your current StoringDataMechanic model only supports:
                         * name, vehical, contact, password, age, usertype
                         *
                         * Since your new XML has CNIC / workshop / address instead of age,
                         * we are temporarily storing CNIC in the "age" field to avoid breaking compile/runtime.
                         *
                         * Proper fix later:
                         * update StoringDataMechanic model + Firebase structure
                         * to include cnic, workshop, and address as separate fields.
                         */
                        StoringDataMechanic mechanic = new StoringDataMechanic(
                                name,
                                vehicleType,
                                contact,
                                password,
                                cnic,
                                userType
                        );

                        mechanicsRef.child(contact).setValue(mechanic)
                                .addOnSuccessListener(unused -> {
                                    setLoadingState(false);
                                    Toast.makeText(
                                            Mechanics_SignUp.this,
                                            "Mechanic account created successfully",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    startActivity(new Intent(Mechanics_SignUp.this, LoginScreen.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    setLoadingState(false);
                                    Toast.makeText(
                                            Mechanics_SignUp.this,
                                            "Failed to create account: " + e.getMessage(),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        setLoadingState(false);
                        Toast.makeText(
                                Mechanics_SignUp.this,
                                "Database error: " + error.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void clearErrors() {
        etMName.setError(null);
        etMContact.setError(null);
        etMPassword.setError(null);
        etMCnic.setError(null);
        etMVehicleType.setError(null);
        etMWorkshop.setError(null);
        etMAddress.setError(null);
    }

    private boolean validateName(String value) {
        if (TextUtils.isEmpty(value)) {
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

    private boolean validateVehicleType(String value) {
        if (TextUtils.isEmpty(value)) {
            etMVehicleType.setError("Vehicle type is required");
            etMVehicleType.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateContact(String value) {
        if (TextUtils.isEmpty(value)) {
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

    private boolean validatePassword(String value) {
        if (TextUtils.isEmpty(value)) {
            etMPassword.setError("Password is required");
            etMPassword.requestFocus();
            return false;
        }

        if (value.length() < 6) {
            etMPassword.setError("Password must be at least 6 characters");
            etMPassword.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateCnic(String value) {
        if (TextUtils.isEmpty(value)) {
            etMCnic.setError("CNIC is required");
            etMCnic.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateWorkshop(String value) {
        if (TextUtils.isEmpty(value)) {
            etMWorkshop.setError("Workshop / experience is required");
            etMWorkshop.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateAddress(String value) {
        if (TextUtils.isEmpty(value)) {
            etMAddress.setError("Address is required");
            etMAddress.requestFocus();
            return false;
        }
        return true;
    }

    private void setLoadingState(boolean isLoading) {
        btnMSignUp.setEnabled(!isLoading);
        btnMSignUp.setText(isLoading ? "Please wait..." : "Finish");
    }
}