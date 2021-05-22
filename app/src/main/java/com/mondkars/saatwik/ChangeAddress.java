package com.mondkars.saatwik;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChangeAddress extends AppCompatActivity implements View.OnClickListener {

    private EditText name, address4, address1, address2, address3, pincode;
    Button verify;
    private Button register;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    String currentUserPhone;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference databaseReference;
    private CollectionReference collectionReference = db.collection("Registering users");
    DatabaseReference pincodes = FirebaseDatabase.getInstance().getReference().child("Pincodes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        address1 = (EditText) findViewById(R.id.house_change);
        pincode = (EditText) findViewById(R.id.pincode_change);
        address4 = (EditText) findViewById(R.id.state_change);
        address2 = (EditText) findViewById(R.id.road_change);
        address3 = (EditText) findViewById(R.id.city_change);
        register = (Button) findViewById(R.id.save_change);
        verify = (Button) findViewById(R.id.verify_change);
        name = (EditText) findViewById(R.id.name_change);
        register.setOnClickListener(this);
        verify.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        currentUserPhone = currentUser.getPhoneNumber();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void verifySignInCode() {
        {
            final String Name = name.getText().toString().trim();
            final String Pincode = pincode.getText().toString().trim();
            final String Address = address1.getText().toString().trim() + ", " + address2.getText().toString().trim() + ", " + address3.getText().toString().trim() + ", " + address4.getText().toString().trim() + ", " + pincode.getText().toString();
            {
                progressDialog.setMessage("We are updating your profile...");
                progressDialog.show();

                collectionReference.document(currentUserPhone).update("Address", Address);
                collectionReference.document(currentUserPhone).update("Name", Name);
                collectionReference.document(currentUserPhone).update("Pincode", Pincode);
                progressDialog.dismiss();
                ;
                Toast.makeText(ChangeAddress.this, "updated successfully", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == register) {
            progressDialog.setMessage("Saving your details");
            progressDialog.show();
            final String Name = name.getText().toString().trim();
            final String Pincode = pincode.getText().toString().trim();
            final String Address = address1.getText().toString().trim() + ", " + address2.getText().toString().trim() + ", " + address3.getText().toString().trim() + ", " + address4.getText().toString().trim();
            if (TextUtils.isEmpty(Name) && TextUtils.isEmpty(Pincode) && TextUtils.isEmpty(Address)) {
                Toast.makeText(ChangeAddress.this, "Please fill all the details", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(Name)) {
                Toast.makeText(ChangeAddress.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(Pincode)) {
                Toast.makeText(ChangeAddress.this, "Please enter your pincode", Toast.LENGTH_SHORT).show();
                return;
            } else if (address1.getText().toString().isEmpty() || address2.getText().toString().isEmpty() || address3.getText().toString().isEmpty()) {
                Toast.makeText(ChangeAddress.this, "Please complete the address", Toast.LENGTH_LONG).show();
            } else {
                sendVerificationCode();
            }
        }
        if (view == verify) {
            progressDialog.setMessage("Checking for availability...");
            progressDialog.show();
            pincodes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (pincode.getText().toString().equals(dataSnapshot.getValue().toString())) {
                            progressDialog.cancel();
                            new SweetAlertDialog(ChangeAddress.this)
                                    .setTitleText("Go ahead!")
                                    .setContentText("We are happy to serve in your area.")
                                    .show();
                            return;
                        }
                    }
                    progressDialog.cancel();
                    new SweetAlertDialog(ChangeAddress.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("SORRY!")
                            .setContentText("We are not serving in your area as of now.")
                            .show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

                ;
            });
        }
    }

    private void sendVerificationCode() {
        pincodes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (pincode.getText().toString().equals(dataSnapshot.getValue().toString())) {
                        verifySignInCode();
                        return;
                    }
                }
                progressDialog.cancel();
                Toast.makeText(ChangeAddress.this, "Sorry, We are not serving in your locality as of now. We will notify you once we start our service in your area.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            ;
        });
    }
}
