package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
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

import java.util.regex.Pattern;

public class MechanicSignIn extends AppCompatActivity {

    private TextView tvSiginAsUser, tvMRegister;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mechanicsRef;
    private EditText etMContactNumber, etMPassword;
    private Button btnMSignIn;

    private boolean isPasswordVisible = false;

    private static final Pattern PAK_PHONE_PATTERN = Pattern.compile("^03\\d{9}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_sign_in);

        initViews();
        initFirebase();
        setListeners();
        setupPasswordToggle();
    }

    private void initViews() {
        etMContactNumber = findViewById(R.id.etMContactNumber);
        etMPassword = findViewById(R.id.etMPass);
        btnMSignIn = findViewById(R.id.btnMSignIN);
        tvSiginAsUser = findViewById(R.id.tvSiginAsUser);
        tvMRegister = findViewById(R.id.tvMRegisternow);
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        mechanicsRef = firebaseDatabase.getReference("Mechanics");
    }

    private void setListeners() {
        tvSiginAsUser.setOnClickListener(view -> {
            Intent intent = new Intent(MechanicSignIn.this, LoginScreen.class);
            startActivity(intent);
        });

        tvMRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MechanicSignIn.this, Mechanics_SignUp.class);
            startActivity(intent);
        });

        btnMSignIn.setOnClickListener(view -> attemptLogin());
    }

    private void setupPasswordToggle() {
        updatePasswordIcon();

        etMPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable drawableEnd = etMPassword.getCompoundDrawablesRelative()[2];

                if (drawableEnd != null &&
                        event.getRawX() >= (etMPassword.getRight()
                                - drawableEnd.getBounds().width()
                                - etMPassword.getPaddingEnd())) {
                    togglePasswordVisibility();
                    etMPassword.performClick();
                    return true;
                }
            }
            return false;
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etMPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            etMPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }

        isPasswordVisible = !isPasswordVisible;
        etMPassword.setSelection(etMPassword.getText().length());
        updatePasswordIcon();
    }

    private void updatePasswordIcon() {
        Drawable eyeIcon = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_view);
        etMPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, eyeIcon, null);
    }

    private void attemptLogin() {
        clearErrors();

        String contactNo = etMContactNumber.getText().toString().trim();
        String password = etMPassword.getText().toString().trim();

        if (!validateContact(contactNo)) return;
        if (!validatePassword(password)) return;

        setLoadingState(true);

        Query checkContact = mechanicsRef.orderByChild("contact").equalTo(contactNo);
        checkContact.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setLoadingState(false);

                if (!snapshot.exists()) {
                    etMContactNumber.setError("No mechanic account found with this mobile number");
                    etMContactNumber.requestFocus();
                    return;
                }

                DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                String userType = userSnapshot.child("usertype").getValue(String.class);
                String name = userSnapshot.child("name").getValue(String.class);
                String contact = userSnapshot.child("contact").getValue(String.class);

                if (passwordFromDB == null) {
                    Toast.makeText(MechanicSignIn.this, "Password record is missing for this account", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(passwordFromDB)) {
                    etMPassword.setError("Incorrect password");
                    etMPassword.requestFocus();
                    return;
                }

                if (userType == null || !userType.equals("Mechanic")) {
                    Toast.makeText(MechanicSignIn.this, "This account is not allowed on mechanic login", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(MechanicSignIn.this, "Login successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MechanicSignIn.this, mechanic_dashbord.class);
                intent.putExtra("contact", contact != null ? contact : contactNo);
                intent.putExtra("name", name);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setLoadingState(false);
                Toast.makeText(MechanicSignIn.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearErrors() {
        etMContactNumber.setError(null);
        etMPassword.setError(null);
    }

    private boolean validateContact(String value) {
        if (TextUtils.isEmpty(value)) {
            etMContactNumber.setError("Mobile number is required");
            etMContactNumber.requestFocus();
            return false;
        }

        if (!PAK_PHONE_PATTERN.matcher(value).matches()) {
            etMContactNumber.setError("Enter a valid number like 03XXXXXXXXX");
            etMContactNumber.requestFocus();
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

    private void setLoadingState(boolean isLoading) {
        btnMSignIn.setEnabled(!isLoading);
        btnMSignIn.setText(isLoading ? "Please wait..." : "Sign In");
    }
}