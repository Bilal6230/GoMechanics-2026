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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginScreen extends AppCompatActivity {

    private TextView tvRegister, tvSiginAsMecahnic;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersRef;
    private EditText etContactNumber, etPassword;
    private Button btnSignIn;

    private boolean isPasswordVisible = false;

    private static final Pattern PAK_PHONE_PATTERN = Pattern.compile("^03\\d{9}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        initViews();
        initFirebase();
        setListeners();
        setupPasswordToggle();
    }

    private void initViews() {
        etContactNumber = findViewById(R.id.etContactNumber);
        etPassword = findViewById(R.id.etPass);
        btnSignIn = findViewById(R.id.btnSignIN);
        tvSiginAsMecahnic = findViewById(R.id.tvSiginAsMecahnic);
        tvRegister = findViewById(R.id.tvRegisternow);
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference("Users");
    }

    private void setListeners() {
        tvSiginAsMecahnic.setOnClickListener(view -> {
            Intent intent = new Intent(LoginScreen.this, MechanicSignIn.class);
            startActivity(intent);
        });

        tvRegister.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(LoginScreen.this, view);
            popupMenu.inflate(R.menu.registration_menu);

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.menu_register_user) {
                    startActivity(new Intent(LoginScreen.this, Client_SignUp.class));
                    return true;
                } else if (itemId == R.id.menu_register_mechanic) {
                    startActivity(new Intent(LoginScreen.this, Mechanics_SignUp.class));
                    return true;
                }

                return false;
            });

            popupMenu.show();
        });

        btnSignIn.setOnClickListener(view -> attemptLogin());
    }

    private void setupPasswordToggle() {
        updatePasswordIcon();

        etPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable drawableEnd = etPassword.getCompoundDrawablesRelative()[2];

                if (drawableEnd != null &&
                        event.getRawX() >= (etPassword.getRight() - drawableEnd.getBounds().width() - etPassword.getPaddingEnd())) {
                    togglePasswordVisibility();
                    etPassword.performClick();
                    return true;
                }
            }
            return false;
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }

        isPasswordVisible = !isPasswordVisible;
        etPassword.setSelection(etPassword.getText().length());
        updatePasswordIcon();
    }

    private void updatePasswordIcon() {
        Drawable eyeIcon;

        if (isPasswordVisible) {
            eyeIcon = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_close_clear_cancel);
        } else {
            eyeIcon = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_view);
        }

        etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, eyeIcon, null);
    }

    private void attemptLogin() {
        clearErrors();

        String contactNo = etContactNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!validateContact(contactNo)) return;
        if (!validatePassword(password)) return;

        setLoadingState(true);

        Query checkContact = usersRef.orderByChild("contact").equalTo(contactNo);
        checkContact.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setLoadingState(false);

                if (!snapshot.exists()) {
                    etContactNumber.setError("No account found with this mobile number");
                    etContactNumber.requestFocus();
                    return;
                }

                DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                String userType = userSnapshot.child("usertype").getValue(String.class);
                String name = userSnapshot.child("name").getValue(String.class);

                if (passwordFromDB == null) {
                    Toast.makeText(LoginScreen.this, "Password record is missing for this account", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(passwordFromDB)) {
                    etPassword.setError("Incorrect password");
                    etPassword.requestFocus();
                    return;
                }

                if (userType == null || !userType.equals("User")) {
                    Toast.makeText(LoginScreen.this, "This account is not allowed on user login", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(LoginScreen.this, "Login successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginScreen.this, User_Dashbord.class);
                intent.putExtra("name", name);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setLoadingState(false);
                Toast.makeText(LoginScreen.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearErrors() {
        etContactNumber.setError(null);
        etPassword.setError(null);
    }

    private boolean validateContact(String value) {
        if (TextUtils.isEmpty(value)) {
            etContactNumber.setError("Mobile number is required");
            etContactNumber.requestFocus();
            return false;
        }

        if (!PAK_PHONE_PATTERN.matcher(value).matches()) {
            etContactNumber.setError("Enter a valid number like 03XXXXXXXXX");
            etContactNumber.requestFocus();
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
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void setLoadingState(boolean isLoading) {
        btnSignIn.setEnabled(!isLoading);
        btnSignIn.setText(isLoading ? "Please wait..." : "Sign In");
    }
}