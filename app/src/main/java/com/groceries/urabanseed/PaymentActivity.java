package com.groceries.urabanseed;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    MaterialCardView cardView1, cardView2;
    Button button;
    final String CHANNEL_ID = "personal notifications";
    final int NOTIFICATION_ID = 001;
    TextView price;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Registering users");
    String email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        cardView1 = findViewById(R.id.online);
        button = findViewById(R.id.confirm);

        price = findViewById(R.id.price);
        price.setText("\u20B9" + getIntent().getIntExtra("payment", 0));

        cardView1.setChecked(true);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cardView.setChecked(!cardView.isChecked());
                cardView1.toggle();
                cardView2.setChecked(false);
                button.setText("Pay Now");
            }
        });

        cardView2 = findViewById(R.id.cash);

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cardView.setChecked(!cardView.isChecked());
                cardView2.toggle();
                cardView1.setChecked(false);
                button.setText("Confirm Order");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardView1.isChecked()){
                    int sAmount = getIntent().getIntExtra("payment", 0);

                    int amount = Math.round(sAmount * 100);
                        collectionReference.document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                Checkout checkout = new Checkout();
                                checkout.setKeyID("rzp_live_skt1ysJd5Rg5d4");
                                checkout.setImage(R.drawable.transparent_logo);

                                JSONObject object = new JSONObject();
                                try {
                                    object.put("name", "URBANSEED");
                                    object.put("description", "Order Charges");
                                    object.put("theme.color", "#1D3C78");
                                    object.put("currency", "INR");
                                    object.put("amount", amount);
                                    object.put("prefill.contact", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                                    if(value.getString("Email") != null) {
                                        object.put("prefill.email", value.getString("Email"));
                                    }
                                    checkout.open(PaymentActivity.this, object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                }
                else{
                    Intent intent = new Intent();
                    setResult(RESULT_FIRST_USER, intent);
                    createNotificationChannel();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(PaymentActivity.this, CHANNEL_ID);
                    builder.setSmallIcon(R.drawable.transparent_logo);
                    builder.setContentTitle("Thank you for ordering with us!");
                    builder.setContentText("We are coming soon..");
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    builder.setAutoCancel(true);

                    Intent fintent = new Intent(PaymentActivity.this, ContactUs.class);
                    fintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(PaymentActivity.this, 0, fintent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(PaymentActivity.this);
                    notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                    finish();
                }
            }
        });

//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                 Intent intent = new Intent();
//                 setResult(RESULT_OK, intent);
//                 finish();
//            }
//        });
    }
    @Override
    public void onPaymentSuccess(String s) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);

        createNotificationChannel();
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(PaymentActivity.this, CHANNEL_ID);
        builder2.setSmallIcon(R.drawable.transparent_logo);
        builder2.setContentTitle("Thank you for ordering with us !");
        builder2.setContentText("We are coming soon...");
        builder2.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder2.setAutoCancel(true);

        Intent fintent = new Intent(PaymentActivity.this, ContactUs.class);
        fintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(PaymentActivity.this, 0, fintent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder2.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(PaymentActivity.this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder2.build());

        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(PaymentActivity.this, "Transaction Failed", Toast.LENGTH_LONG).show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            CharSequence name = "Personal Notifications";
            String description = "Include all personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}