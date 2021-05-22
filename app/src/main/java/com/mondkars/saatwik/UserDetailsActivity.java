package com.mondkars.saatwik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.mondkars.saatwik.Utils.Users;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, address4, address1, address2, address3, pincode;
    Button verify;
    private Button register;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference databaseReference;
    private CollectionReference collectionReference = db.collection("Registering users");
    DatabaseReference pincodes = FirebaseDatabase.getInstance().getReference().child("Pincodes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        address1 = (EditText) findViewById(R.id.house);
        pincode = (EditText) findViewById(R.id.pincode);
        address4 = (EditText) findViewById(R.id.state);
        address2 = (EditText) findViewById(R.id.road);
        address3 = (EditText) findViewById(R.id.city);
        register = (Button) findViewById(R.id.save);
        verify = (Button) findViewById(R.id.verify);
        name = (EditText) findViewById(R.id.name);
        register.setOnClickListener(this);
        verify.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void verifySignInCode() {
        {
            final String Name = name.getText().toString().trim();
            final String Pincode = pincode.getText().toString().trim();
            final String Address = address1.getText().toString().trim() + ", " + address2.getText().toString().trim() + ", " + address3.getText().toString().trim() + ", " + address4.getText().toString().trim() + ", " + pincode.getText().toString();
            {
                {
                    {
                        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    {
                                        // Sign in success, update UI with the signed-in user's information
                                        currentUser = firebaseAuth.getCurrentUser();
                                        final String currentUserNumber = currentUser.getPhoneNumber();
                                        Map<String, String> userObj = new HashMap<>();
                                        userObj.put("Name", Name);
                                        userObj.put("Pincode", Pincode);
                                        userObj.put("Address", Address);

                                        collectionReference.document(currentUserNumber).set(userObj);

                                        Users users = Users.getInstance();
                                        users.setName(Name);
                                        users.setUserNumber(currentUserNumber);
                                        users.setPincode(Pincode);
                                        users.setAddress(Address);
                                        Toast.makeText(UserDetailsActivity.this, "Your details are saved successfully.", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(UserDetailsActivity.this, Main2Activity.class);
                                        startActivity(intent);
                                        finish();
                                        // ...
                                    }
                                }
                            }
                        });
                    }


                }
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
                Toast.makeText(UserDetailsActivity.this, "Please fill all the details", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(Name)) {
                Toast.makeText(UserDetailsActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(Pincode)) {
                Toast.makeText(UserDetailsActivity.this, "Please enter your pincode", Toast.LENGTH_SHORT).show();
                return;
            } else if (address1.getText().toString().isEmpty() || address2.getText().toString().isEmpty() || address3.getText().toString().isEmpty()) {
                Toast.makeText(UserDetailsActivity.this, "Please complete the address", Toast.LENGTH_LONG).show();
            } else {
                sendVerificationCode();
            }
        }
        if(view == verify){
            progressDialog.setMessage("Checking for availability...");
            progressDialog.show();
            pincodes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        if(pincode.getText().toString().equals(dataSnapshot.getValue().toString())){
                            progressDialog.cancel();
                            new SweetAlertDialog(UserDetailsActivity.this)
                                    .setTitleText("Go ahead!")
                                    .setContentText("We are happy to serve in your area.")
                                    .show();
                            return;
                        }
                    }
                    progressDialog.cancel();
                    new SweetAlertDialog(UserDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("SORRY!")
                            .setContentText("We are not serving in your area as of now.")
                            .show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                };
            });
        }
    }

    private void sendVerificationCode() {
        pincodes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(pincode.getText().toString().equals(dataSnapshot.getValue().toString())){
                        verifySignInCode();
                        return;
                    }
                }
                progressDialog.cancel();
                Toast.makeText(UserDetailsActivity.this, "Sorry, We are not serving in your locality as of now. We will notify you once we start our service in your area.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            };
        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        firebaseAuth.getCurrentUser().delete();
        firebaseAuth.signOut();
        finish();
        return true;
    }
    @Override
    public void onBackPressed() {
        firebaseAuth.getCurrentUser().delete();
        firebaseAuth.signOut();
        finish();
    }
}
