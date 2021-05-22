package com.mondkars.saatwik;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mondkars.saatwik.Utils.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;


public class Activity2 extends AppCompatActivity {
    private static final int RESOLVE_HINT = 10;
    private Button button3;
    private EditText editText;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private String username;
    TextView back;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Registering users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        firebaseAuth = FirebaseAuth.getInstance();
        back = (TextView) findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity2.this, Main2Activity.class));
            }
        });

        editText = (EditText) findViewById(R.id.username);

        button3 = (Button) findViewById(R.id.button3);
        progressDialog = new ProgressDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(firebaseAuth.getCurrentUser() == null) {
//                    loginUser(editText.getText().toString().trim(), editText2.getText().toString().trim());
//                }
//            }
//
//            private void loginUser(final String username, String password) {
//
//                if (TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
//                    progressDialog.dismiss();
//                    Toast.makeText(Activity2.this, "Please enter the username", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) ) {
//                    progressDialog.dismiss();
//                    Toast.makeText(Activity2.this, "Please enter the password", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(password) && TextUtils.isEmpty(username) ) {
//                    progressDialog.dismiss();
//                    Toast.makeText(Activity2.this, "Please enter the username and the password", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//
//
//                else {
//                    progressDialog.setMessage("You are logging in...");
//                    progressDialog.show();
//                    firebaseAuth.signInWithEmailAndPassword(username, password)
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (firebaseAuth.getCurrentUser() != null) {
//                                        final FirebaseUser user = firebaseAuth.getCurrentUser();
//                                        assert user != null;
//                                        final String currentUserId = user.getUid();
//
//                                        collectionReference
//                                                .whereEqualTo("UserId", currentUserId)
//                                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                                    @Override
//                                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                                                        if (e != null) {
//
//                                                        }
//                                                        assert queryDocumentSnapshots != null;
//                                                        if (!queryDocumentSnapshots.isEmpty()) {
//                                                            progressDialog.setMessage("You are logging in...");
//                                                            progressDialog.show();
//                                                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
//                                                                Users users = Users.getInstance();
//                                                                users.setName(snapshot.getString("Name"));
//                                                                users.setUserId(snapshot.getString("UserId"));
//                                                                users.setMobile(snapshot.getString("Mobile"));
//                                                                users.setAlternate(snapshot.getString("Alternate mobile"));
//                                                                users.setAddress(snapshot.getString("Address"));
//                                                                startActivity(new Intent(Activity2.this, Main2Activity.class));
//                                                                progressDialog.dismiss();
//                                                                Toast.makeText(Activity2.this, "Successfully logged in", Toast.LENGTH_LONG).show();
//                                                                editText.getText().clear();
//                                                                editText2.getText().clear();
//                                                            }
//                                                        }
//                                                    }
//                                                });
//                                    }
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    progressDialog.dismiss();
//                                    Toast.makeText(Activity2.this, "Something wrong happened", Toast.LENGTH_LONG).show();
//                                }
//                            });
//                }
//            }
//        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = editText.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    progressDialog.dismiss();
                    Toast.makeText(Activity2.this, "Please enter the mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (username.length() < 10){
                    progressDialog.dismiss();
                    Toast.makeText(Activity2.this, "Please enter the valid mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (username.length() > 10){
                    progressDialog.dismiss();
                    Toast.makeText(Activity2.this, "Please enter the mobile number without country code", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    Intent intent = new Intent(Activity2.this, OTPActivity.class);
                    intent.putExtra("phone", username);
                    startActivity(intent);
                }
            }
        });

      requestHint();

    }

    private void requestHint() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        GoogleApiClient apiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();;
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                apiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    // Obtain the phone number from the result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                // credential.getId(); <-- E.164 format phone number on 10.2.+ devices
                if (credential.getId().startsWith("+")) {
                    if (credential.getId().length() == 13) {
                        editText.setText(credential.getId().substring(3));
                    } else if (credential.getId().length() == 14) {
                        editText.setText(credential.getId().substring(4));
                    }
                } else {
                    editText.setText(credential.getId());
                }
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Main2Activity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
    @Override
    public void onBackPressed() {
       startActivity(new Intent(Activity2.this, Main2Activity.class));
    }
}