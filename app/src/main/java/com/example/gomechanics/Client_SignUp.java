package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Client_SignUp extends AppCompatActivity {
    FirebaseDatabase FB;
    DatabaseReference DR;
    EditText etName,etvehical,etcontact,etpassword,etaddress;
    Button btnSignUp;
    TextView tvsignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_sign_up);
        tvsignin = findViewById(R.id.tvSigin);
        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Client_SignUp.this, LoginScreen.class);
                startActivity(intent);
            }
        });
        etName = findViewById(R.id.etname);
        etvehical = findViewById(R.id.etvehicaletype);
        etcontact = findViewById(R.id.etcontact);
        etpassword = findViewById(R.id.etpassword);
        etaddress = findViewById(R.id.etaddress);
        btnSignUp = findViewById(R.id.btnfinsih);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FB = FirebaseDatabase.getInstance();
                DR = FB.getReference("Users");
                final String contact = etcontact.getText().toString();

                // Check if any EditText field is empty
                if (TextUtils.isEmpty(etName.getText().toString())
                        || TextUtils.isEmpty(etvehical.getText().toString())
                        || TextUtils.isEmpty(contact)
                        || TextUtils.isEmpty(etpassword.getText().toString())
                        || TextUtils.isEmpty(etaddress.getText().toString())) {
                    // Display a toast message to fill all EditText fields
                    Toast.makeText(Client_SignUp.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }
                else
                {
                    if (contact.length() != 11)
                    {
                        Toast.makeText(Client_SignUp.this, "Make Contact 11 Digit", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (etpassword.length()!=6)
                        {
                            Toast.makeText(Client_SignUp.this, "Make Password 6 Digit", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            DR.orderByChild("contact").equalTo(contact).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // Contact number already exists, display a toast message
                                        Toast.makeText(Client_SignUp.this, "Contact number already exists", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (etpassword.getText().length() <= 5) {
                                            Toast.makeText(Client_SignUp.this, "Password should be atleast 6 digit", Toast.LENGTH_SHORT).show();
                                            return; // Stop further execution
                                        }
                                        else
                                        {
                                            // Contact number is not present, proceed with registration
                                            String name = etName.getText().toString();
                                            String vehical = etvehical.getText().toString();
                                            String password = etpassword.getText().toString();
                                            String address = etaddress.getText().toString();
                                            String usertype = "User";
                                            StoringData store = new StoringData(name, vehical, contact, password, address,usertype);
                                            DR.child(contact).setValue(store);
                                            Intent intent = new Intent(Client_SignUp.this, LoginScreen.class);
                                            startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle database error
                                }
                            });

                        }

                    }
                }
                // Check if the contact number already exists in the database
            }
        });

    }


}