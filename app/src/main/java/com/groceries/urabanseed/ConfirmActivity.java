package com.groceries.urabanseed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.groceries.urabanseed.Utils.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import model.Model;
import model.Slot;
import ui.CalendatDatesRecyclerAdapter;
import ui.SlotRecyclerAdapter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ConfirmActivity extends AppCompatActivity{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Registering users");
    TextView name, address, mobile1;
    Button button, confirm;
    String currentUserId;
    TextView checkBox1;
    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    Timer timer;
    final String CHANNEL_ID = "personal notifications";
    final int NOTIFICATION_ID = 001;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Delivery Slots");
    RecyclerView recyclerView;
    SlotRecyclerAdapter slotRecyclerAdapter;
    ArrayList<Slot> slots;
    String price="0", time, amount;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Availed");
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    int payment=0;
    Boolean bool = true;
    DatabaseReference offDay = FirebaseDatabase.getInstance().getReference().child("Off Day");
    Boolean flag = true;

    LocalDate squareCalendarMonthDayStart;
    LocalDate squareCalendarMonthDayEnd;
    ArrayList<Model> totalDates = new ArrayList<>();
    int i = 0;

    String date2;
    String date = String.valueOf(LocalDate.now().getDayOfMonth()) + " " +  String.valueOf(LocalDate.now().getMonth());

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            if(intent.hasExtra("price")) {
                price = intent.getStringExtra("price");
                time = intent.getStringExtra("time");
                amount = intent.getStringExtra("amount");
            }
            if(intent.hasExtra("date")) {
                date = intent.getStringExtra("date");
                for(Slot slot: slots){
                    slot.setBool(false);
                }
                slotRecyclerAdapter = new SlotRecyclerAdapter(ConfirmActivity.this, slots, payment, date);
                slotRecyclerAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(slotRecyclerAdapter);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        LocalBroadcastManager.getInstance(ConfirmActivity.this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        checkBox1 = findViewById(R.id.home_check);
        confirm = findViewById(R.id.change);
        name = findViewById(R.id.name_view);
        address = findViewById(R.id.address_view);
        mobile1 = findViewById(R.id.mobile1);
        button = findViewById(R.id.confirm);

        String currentUserPhone = user.getPhoneNumber();

        recyclerView = findViewById(R.id.recyclerview);

        slots = new ArrayList<>();

        payment = getIntent().getIntExtra("payment", 0);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ConfirmActivity.this));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Slot slot = dataSnapshot1.getValue(Slot.class);
                    slot.setBool(false);
                    slots.add(slot);
                }
                slotRecyclerAdapter = new SlotRecyclerAdapter(ConfirmActivity.this, slots, payment, date);
                recyclerView.setAdapter(slotRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LocalDate today = LocalDate.now();
        LocalDate firstDayOfTheMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfTheMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        squareCalendarMonthDayStart = today;
        squareCalendarMonthDayEnd = today.plusDays(29);
        offDay.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                while (!squareCalendarMonthDayStart.isAfter(squareCalendarMonthDayEnd)) {
                    ZoneId defaultZoneId = ZoneId.systemDefault();
                    Model model;
                    model = new Model(Date.from(squareCalendarMonthDayStart.atStartOfDay(defaultZoneId).toInstant()), false);

                    flag = true;

                    if(snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (model.getDate().toString().equals(dataSnapshot.getValue().toString())) {
                                flag = false;
                                break;
                            }
                        }
                    }
                    if(flag) {
                        if (i == 0) {
                            model = new Model(Date.from(squareCalendarMonthDayStart.atStartOfDay(defaultZoneId).toInstant()), true);
                        }
                        totalDates.add(model);
                        i++;
                    }
                    squareCalendarMonthDayStart = squareCalendarMonthDayStart.plusDays(1);
                }

                RecyclerView recyclerView2 = findViewById(R.id.dates);
                recyclerView2.setLayoutManager(new LinearLayoutManager(ConfirmActivity.this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView2.setHasFixedSize(true);
                CalendatDatesRecyclerAdapter calendatDatesRecyclerAdapter = new CalendatDatesRecyclerAdapter(totalDates, ConfirmActivity.this);
                recyclerView2.setAdapter(calendatDatesRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        collectionReference.document(currentUserPhone).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                {

                    if (value.exists()) {
                        {
                            Users users = Users.getInstance();
                            String temp = value.getString("Address");
                            address.setText(temp + ".");
                            name.setText(value.getString("Name"));
                            mobile1.setText(value.getId());
                        }
                    }else
                        Toast.makeText(ConfirmActivity.this, "Couldn't find your address !", Toast.LENGTH_LONG).show();
                }
            }
        });
     confirm.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if (amount != null) {
                 if (getIntent().hasExtra("coupon")) {
                     reference.child(getIntent().getStringExtra("coupon")).child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).setValue(getIntent().getStringExtra("coupon"));
                 }

                 if (payment <= Integer.parseInt(amount)) {
                     payment += Integer.parseInt(price);
                 } else {
                     bool = false;
                 }
                 if (payment == 0) {
                     Intent intent = new Intent();
                     intent.putExtra("time", time);
                     setResult(1000, intent);
                     finish();
                     return;
                 }
                 Intent intent = new Intent(ConfirmActivity.this, PaymentActivity.class);
                 intent.putExtra("payment", payment);
                 startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);

//             {
//                 Intent intent = new Intent();
//                 intent.putExtra("price", price);
//                 intent.putExtra("time", time);
//                 setResult(RESULT_FIRST_USER, intent);
//                 finish();
//             }
             }
             else {
                 Toast.makeText(ConfirmActivity.this, "Kindly select the time slot", Toast.LENGTH_LONG).show();
             }
         }
     });
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(ConfirmActivity.this, ChangeAddress.class));
        }
    });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = new Intent();
        if(bool) {
            intent.putExtra("price", price);
        }
        else{
            intent.putExtra("price", "0");
        }
        intent.putExtra("time", time);
        intent.putExtra("date", date);
        setResult(resultCode, intent);
        finish();
    }
}
