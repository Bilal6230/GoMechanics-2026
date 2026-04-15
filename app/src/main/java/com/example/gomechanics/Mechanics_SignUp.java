package com.example.gomechanics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Mechanics_SignUp extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView img;
    CardView imageupload;
    TextView tvuplaod,tvsignin;
    FirebaseDatabase FB;
    DatabaseReference DR;
    EditText etMName;
    Spinner etMvehical;
    EditText etMcontact;
    EditText etMage;
    EditText etMpassword;
    Button btnMSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanics_sign_up);
        etMName = findViewById(R.id.etMname);
        etMvehical = findViewById(R.id.etMvehicaletype);
        etMcontact = findViewById(R.id.etMcontact);
        etMpassword = findViewById(R.id.etMpassword);
        etMage = findViewById(R.id.etMage);
        btnMSignUp = findViewById(R.id.btnMfinsih);
        tvsignin = findViewById(R.id.tvSigin);

        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mechanics_SignUp.this, LoginScreen.class);
                startActivity(intent);
            }
        });

        Spinner spinner = etMvehical;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.vehicle_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

//        img = findViewById(R.id.imgupload);
//        tvuplaod = findViewById(R.id.tvclickheretouplaod);
//        imageupload = findViewById(R.id.cardviewimageuplaod);
//
//        imageupload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, PICK_IMAGE_REQUEST);
//            }
//        });
        btnMSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FB = FirebaseDatabase.getInstance();
                DR = FB.getReference("Mechanics");
                final String contact = etMcontact.getText().toString();

                // Check if any EditText field is empty
                if (etMName.getText().toString().isEmpty()
                        || etMvehical.getSelectedItem()==null
                        || contact.isEmpty()
                        || etMpassword.getText().toString().isEmpty()
                        || etMage.getText().toString().isEmpty()) {
                    // Display a toast message to fill all fields
                    Toast.makeText(Mechanics_SignUp.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }
                else
                {
                    if (contact.length() != 11)
                    {
                        Toast.makeText(Mechanics_SignUp.this, "Make Contact 11 Digit", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (etMpassword.length()!=6)
                        {
                            Toast.makeText(Mechanics_SignUp.this, "Make Password 6 Digit", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            DR.orderByChild("contact").equalTo(contact).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // Contact number already exists, display a toast message
                                        Toast.makeText(Mechanics_SignUp.this, "Contact number already exists", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (etMpassword.getText().length() <= 5) {
                                            Toast.makeText(Mechanics_SignUp.this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                                            return; // Stop further execution
                                        } else {
                                            // Contact number is not present, proceed with registration
                                            String name = etMName.getText().toString();
                                            String vehical = etMvehical.getSelectedItem().toString();
                                            String password = etMpassword.getText().toString();
                                            String age = etMage.getText().toString();
                                            String usertype = "Mechanic";
                                            StoringDataMechanic store = new StoringDataMechanic(name, vehical, contact, password, age, usertype);
                                            DR.child(contact).setValue(store);
                                            Intent intent = new Intent(Mechanics_SignUp.this, LoginScreen.class);
                                            startActivity(intent);
                                        }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            img.setImageURI(imageUri);
            ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            img.setLayoutParams(layoutParams);
            tvuplaod.setVisibility(View.GONE);
        }
    }
}