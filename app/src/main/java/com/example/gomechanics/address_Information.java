package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class address_Information extends AppCompatActivity {
    ImageButton imgbtnBack;
    Button btnfinsih;
    FirebaseDatabase FB;
    DatabaseReference DR , DRrecent;
    TextInputEditText etVehicaleDec, etVehicalIssues;
    EditText etcontact, etminbudget, etmaxbudget, etaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_information);
        imgbtnBack = findViewById(R.id.ImageBtnBack);
        btnfinsih = findViewById(R.id.btnfinsih);
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(address_Information.this, User_Dashbord.class);
                startActivity(intent);
            }
        });
//        btnfinsih.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(address_Information.this, mechanics_list.class);
//                startActivity(intent);
//            }
//        });
        etVehicaleDec = findViewById(R.id.etVehicalDecription);
        etVehicalIssues = findViewById(R.id.etVehicaleIssues);
        etminbudget = findViewById(R.id.etMinBudget);
        etmaxbudget = findViewById(R.id.etMaxBudget);
//        String minBudgetString = etminbudget.getText().toString();
//        String maxBudgetString = etmaxbudget.getText().toString();
//
//        int minBudget = Integer.parseInt(minBudgetString);
//        int maxBudget = Integer.parseInt(maxBudgetString);
        etcontact = findViewById(R.id.etVehicaleContact);
        etaddress = findViewById(R.id.etVehicalAddress);
        btnfinsih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FB = FirebaseDatabase.getInstance();
                DR = FB.getReference("Bookings");
                DRrecent = FB.getReference("RecentMechanic");
                String contactNo = getIntent().getStringExtra("contactNo");
                // Check if any EditText field is empty
                if (TextUtils.isEmpty(etVehicaleDec.getText().toString())
                        || TextUtils.isEmpty(etVehicalIssues.getText().toString())
                        || TextUtils.isEmpty(etmaxbudget.getText().toString())
                        || TextUtils.isEmpty(etminbudget.getText().toString())
                        || TextUtils.isEmpty(etmaxbudget.getText().toString())
                        || TextUtils.isEmpty(etcontact.getText().toString())
                        || TextUtils.isEmpty(etaddress.getText().toString())) {

                    // Display a toast message to fill all EditText fields
                    Toast.makeText(address_Information.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }
            else
                {
                    int minBudget = Integer.parseInt(etminbudget.getText().toString());
                    int maxBudget = Integer.parseInt(etmaxbudget.getText().toString());
                    String contact = etcontact.getText().toString();
                    if (minBudget >= maxBudget)
                    {
                        Toast.makeText(address_Information.this, "Minimum budget should be less than the maximum budget", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (contact.length() != 11)
                        {
                            Toast.makeText(address_Information.this, "Make Contact 11 Digit", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            DR.orderByChild("contact").equalTo(contactNo).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Toast.makeText(address_Information.this, "Currently Busy Choes Someone else", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String VehicaleDec = etVehicaleDec.getText().toString();
                                        String VehicalIssues = etVehicalIssues.getText().toString();
                                        String contact = etcontact.getText().toString();
                                        String address = etaddress.getText().toString();
                                        String min = etminbudget.getText().toString();
                                        String max = etmaxbudget.getText().toString();
                                        Intent intent = getIntent();
                                        String McontactNo = intent.getStringExtra("contactNo");
                                        String Mname = intent.getStringExtra("name");
                                        storingaddressinformation store = new storingaddressinformation(VehicaleDec, VehicalIssues, contact, address, min, max,McontactNo,Mname);
                                        DR.child(contact).setValue(store);
                                        DRrecent.child(contact).setValue(store);
                                        Toast.makeText(address_Information.this, "BookingPlaced", Toast.LENGTH_SHORT).show();
                                        intent = new Intent(address_Information.this, Previous_Booking.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

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