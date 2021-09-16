package com.groceries.urabanseed;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.groceries.urabanseed.Utils.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.CartItem;
import model.OrderForAdmin;
import ui.CartRecyclerAdapter;

public class MyCart extends AppCompatActivity {

    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    TextView offer;
    ImageView cartIcon;
    int d = 0;
    public List<CartItem> myCart;
    public RecyclerView recyclerView;
    public CartRecyclerAdapter cartRecyclerAdapter;
    DatabaseReference reference, orderReference, minReference;
    public float cost = 0, originalCost = 0;
    private TextView notext, nowarning, stotal, fintotal;
    private ImageView noimage;
    public TextView total, address, name, dcharge, dtotal, discount, original;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Registering users");
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();
    String currentUserPhone = user.getPhoneNumber();
    private ProgressBar progressBar;
    public Button change, buy, coupon;
    String charge, min;
    int ch = 0;
    int Min = 0;
    int g = 0;
    int fin = 0;
    float vhariginal = 0;
    public String a = "0", k;
    TextView empty;
    DatabaseReference dileveryReference, adminOrder;
    CardView card1, card2, card3;
    DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Stop");
    DatabaseReference donor = FirebaseDatabase.getInstance().getReference().child("Donors");
    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Takers");
    DatabaseReference marketers = FirebaseDatabase.getInstance().getReference().child("Marketers");
    String marketer = null;
    DatabaseReference giftReference = marketers.child(currentUserPhone);
    TextView totalSave;
    int payment=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        myCart = new ArrayList<CartItem>();
        notext = findViewById(R.id.notext);
        nowarning = findViewById(R.id.nowarning);
        noimage = findViewById(R.id.noimage);
        original = findViewById(R.id.original_num_price);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        offer = findViewById(R.id.offer);
        cartIcon = findViewById(R.id.cart_icon);
        totalSave = findViewById(R.id.total_save);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        empty = findViewById(R.id.empty);
        discount = findViewById(R.id.disconted_price);
        coupon = findViewById(R.id.coupon);
        buy = findViewById(R.id.buy_all);
        change = findViewById(R.id.change);
        address = findViewById(R.id.address_view);
        name = findViewById(R.id.name_view);
        total = findViewById(R.id.total_cost);
        dcharge = findViewById(R.id.num_charges);
        dtotal = findViewById(R.id.num_total);
        stotal = findViewById(R.id.num_savings);
        fintotal = findViewById(R.id.num_fin);
        recyclerView = (RecyclerView) findViewById(R.id.cart_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyCart.this));
        orderReference = FirebaseDatabase.getInstance().getReference().child("Order");
        reference = FirebaseDatabase.getInstance().getReference().child("cart");
        dileveryReference = FirebaseDatabase.getInstance().getReference().child("charge").child("delivery");
        minReference = FirebaseDatabase.getInstance().getReference().child("charge").child("Min");


        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("True")) {
                    noimage.setVisibility(View.VISIBLE);
                    notext.setVisibility(View.VISIBLE);
                    nowarning.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    card1.setVisibility(View.INVISIBLE);
                    card2.setVisibility(View.INVISIBLE);
                    card3.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noimage.setVisibility(View.INVISIBLE);
                    notext.setVisibility(View.INVISIBLE);
                    nowarning.setVisibility(View.INVISIBLE);

                    collectionReference.document(currentUserPhone).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            {
                                if (value.exists()) {
                                    {
                                        Users users = Users.getInstance();
                                        String temp = value.getString("Address");
                                        address.setText(temp);
                                        name.setText(value.getString("Name"));
                                    }
                                } else
                                    Toast.makeText(MyCart.this, "Couldn't find your address !", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    change.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v == change) {
                                startActivity(new Intent(MyCart.this, ChangeAddress.class));
                            }
                        }
                    });

                    {
                        {
                            {
                                total.setText("\u20B9" + "0");
                                dcharge.setText("\u20B9" + "0");
                                Query query = reference.orderByChild("userPhone").equalTo(currentUserPhone);
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            myCart = new ArrayList<CartItem>();
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            {
                                                final CartItem ameya = dataSnapshot1.getValue(CartItem.class);
                                                float temp = Float.parseFloat(ameya.getPrice());
                                                float temp2 = Float.parseFloat(ameya.getNumber());
                                                float temp3 = Float.parseFloat(ameya.getOriginal());
                                                cost = cost + temp * temp2;
                                                vhariginal = vhariginal + temp3*temp2;
                                                myCart.add(ameya);
                                            }
                                        }

                                        if (cost == 0) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            empty.setVisibility(View.VISIBLE);
                                            cartIcon.setVisibility(View.VISIBLE);
                                            offer.setVisibility(View.VISIBLE);
                                        } else {
                                            int b = getIntent().getIntExtra("discount", 0);
                                            marketer = getIntent().getStringExtra("gift");
                                            float c = (cost * b / 100);
                                            d = Math.round(c);
                                            discount.setText("\u20B9" + String.valueOf(d));
                                            minReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    min = dataSnapshot.getValue().toString();
                                                    Min = Integer.valueOf(min);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                            dileveryReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    charge = dataSnapshot.getValue().toString();
                                                    ch = Integer.valueOf(charge);

                                                    int a = 0;
                                                    a = getIntent().getIntExtra("discount", 0);

                                                    giftReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                stotal.setText("\u20B9" + snapshot.child("money").getValue().toString());
                                                                fin = Integer.parseInt(snapshot.child("money").getValue().toString());
                                                            }
                                                            if (cost >= Min) {
                                                                int h = (d + fin);
                                                                if (h + vhariginal - cost > 0) {
                                                                    totalSave.setEnabled(true);
                                                                    if (h > cost - d) {
                                                                        h = Math.round(cost - d);
                                                                    }
                                                                    totalSave.setText("You have saved " + "\u20B9" + Math.round(h + vhariginal - cost) + " on this order.");
                                                                }
                                                                original.setText("\u20B9" + Math.round(cost));
                                                                dcharge.setText("Free");
                                                                dcharge.setTextColor(Color.parseColor("#008000"));
                                                                dtotal.setText("\u20B9" + Math.round(cost - d));
                                                                if (fin > cost - d) {
                                                                    fin = Math.round(cost - d);
                                                                }
                                                                total.setText("\u20B9" + Math.round(cost - d - fin));
                                                                fintotal.setText("\u20B9" + Math.round(cost - d - fin));
                                                                payment = Math.round(cost - d - fin);
                                                            } else {
                                                                int h = (d + fin);
                                                                if (h + vhariginal - cost > 0) {
                                                                    totalSave.setEnabled(true);
                                                                    if (h > cost + ch - d) {
                                                                        h = Math.round(cost + ch - d);
                                                                    }
                                                                    totalSave.setText("You have saved " + "\u20B9" + Math.round(h+vhariginal - cost) + " on this order.");
                                                                }
                                                                original.setText("\u20B9" + Math.round(cost));
                                                                dcharge.setText("\u20B9" + charge);
                                                                dtotal.setText("\u20B9" + Math.round(cost + ch - d));
                                                                if (fin > cost - d) {
                                                                    fin = Math.round(cost - d + ch);
                                                                }
                                                                total.setText("\u20B9" + Math.round(cost + ch - d - fin));
                                                                fintotal.setText("\u20B9" + Math.round(cost - d - fin + ch));
                                                                payment = Math.round(cost - d - fin + ch);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            card3.setVisibility(View.VISIBLE);
                                            cartRecyclerAdapter = new CartRecyclerAdapter(MyCart.this, myCart);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            recyclerView.setAdapter(cartRecyclerAdapter);
                                            cartRecyclerAdapter.notifyDataSetChanged();
                                            card1.setVisibility(View.VISIBLE);
                                            card2.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }
                    buy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v == buy) {
                                Intent intent = new Intent(MyCart.this, ConfirmActivity.class);
                                if (getIntent().hasExtra("coupon")) {
                                    intent.putExtra("coupon", getIntent().getStringExtra("coupon"));
                                }
                                intent.putExtra("payment", payment);
                                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
                            }
                        }
                    });
                    coupon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            startActivity(new Intent(MyCart.this, CouponActivity.class));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result

        if(resultCode == RESULT_FIRST_USER) {
            String charge = data.getStringExtra("price");
            String time = data.getStringExtra("time");
            String date = data.getStringExtra("date");

            {
                for (CartItem cartItem : myCart) {
                    int b = getIntent().getIntExtra("discount", 0);
                    int c = (int) (Float.parseFloat(cartItem.getPrice()) * b * Float.parseFloat(cartItem.getNumber()) / 100);


                    String currentDateTimeString1 = java.text.DateFormat.getDateTimeInstance().format(new Date());
                    orderReference = FirebaseDatabase.getInstance().getReference().child("Order").child(currentUserPhone + cartItem.getItem() + cartItem.getPer() + currentDateTimeString1);
                    cartItem.setDiscount(String.valueOf(c));
                    cartItem.setDelivery(charge);
                    cartItem.setTime(time);
                    cartItem.setDate(date);
                    cartItem.setPaid("False");
                    cartItem.setCurrentTime(String.valueOf(System.currentTimeMillis()));
                    orderReference.setValue(cartItem);

                    OrderForAdmin orderForAdmin = new OrderForAdmin();
                    orderForAdmin.setItem(cartItem.getItem());
                    orderForAdmin.setNumber(cartItem.getNumber());
                    orderForAdmin.setPrice(cartItem.getPrice());
                    orderForAdmin.setUserId(currentUserPhone);
                    orderForAdmin.setDiscount(String.valueOf(c));
                    orderForAdmin.setCharge(charge);
                    orderForAdmin.setPaid("False");
                    orderForAdmin.setTime(time);
                    orderForAdmin.setDate(date);
                    orderForAdmin.setQuantity(cartItem.getPer());

                    final String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                    adminOrder = FirebaseDatabase.getInstance().getReference().child("Order for admin").child(user.getPhoneNumber()).child(cartItem.getItem() + cartItem.getPer() + cartItem.getNumber() + currentDateTimeString);
                    adminOrder.setValue(orderForAdmin);


                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getPhoneNumber());
                    final CollectionReference collectionReference = db.collection("Registering users");

                    collectionReference.document(currentUserPhone).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            {

                                if (value.exists()) {
                                    {

                                        Map<String, String> userObj = new HashMap<>();
                                        userObj.put("Mobile", value.getId());
                                        userObj.put("Name", value.getString("Name"));
                                        userObj.put("Pincode", value.getString("Pincode"));
                                        userObj.put("Address", value.getString("Address"));
                                        userObj.put("ReferralNumber", marketer);
                                        userObj.put("Time", time);
                                        userObj.put("Date", date);
                                        ref.setValue(userObj);
                                    }
                                }
                            }
                        }
                    });
                }

                Query query = reference.orderByChild("userPhone").equalTo(currentUserPhone);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            {
                                dataSnapshot1.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent fintent = new Intent(getBaseContext(), GalleryFragment.class);
                fintent.putExtra("PREVIOUS_ACTIVITY", this.getClass().getSimpleName());
                startActivity(fintent);
                finish();
            }
        }
        if(resultCode == RESULT_OK) {
            String charge = data.getStringExtra("price");
            String time = data.getStringExtra("time");
            String date = data.getStringExtra("date");

            {
                for (CartItem cartItem : myCart) {
                    int b = getIntent().getIntExtra("discount", 0);
                    int c = (int) (Float.parseFloat(cartItem.getPrice()) * b * Float.parseFloat(cartItem.getNumber()) / 100);


                    String currentDateTimeString1 = java.text.DateFormat.getDateTimeInstance().format(new Date());
                    orderReference = FirebaseDatabase.getInstance().getReference().child("Order").child(currentUserPhone + cartItem.getItem() + cartItem.getPer() + currentDateTimeString1);
                    cartItem.setDiscount(String.valueOf(c));
                    cartItem.setDelivery(charge);
                    cartItem.setTime(time);
                    cartItem.setDate(date);
                    cartItem.setPaid("True");
                    cartItem.setCurrentTime(String.valueOf(System.currentTimeMillis()));
                    orderReference.setValue(cartItem);

                    OrderForAdmin orderForAdmin = new OrderForAdmin();
                    orderForAdmin.setItem(cartItem.getItem());
                    orderForAdmin.setNumber(cartItem.getNumber());
                    orderForAdmin.setPrice(cartItem.getPrice());
                    orderForAdmin.setUserId(currentUserPhone);
                    orderForAdmin.setDiscount(String.valueOf(c));
                    orderForAdmin.setCharge(charge);
                    orderForAdmin.setTime(time);
                    orderForAdmin.setDate(date);
                    orderForAdmin.setPaid("True");

                    final String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                    adminOrder = FirebaseDatabase.getInstance().getReference().child("Order for admin").child(user.getPhoneNumber()).child(cartItem.getItem() + cartItem.getPer() + cartItem.getNumber() + currentDateTimeString);
                    adminOrder.setValue(orderForAdmin);


                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getPhoneNumber());
                    final CollectionReference collectionReference = db.collection("Registering users");

                    collectionReference.document(currentUserPhone).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            {

                                if (value.exists()) {
                                    {

                                        Map<String, String> userObj = new HashMap<>();
                                        userObj.put("Mobile", value.getId());
                                        userObj.put("Name", value.getString("Name"));
                                        userObj.put("Pincode", value.getString("Pincode"));
                                        userObj.put("Address", value.getString("Address"));
                                        userObj.put("ReferralNumber", marketer);
                                        userObj.put("Time", time);
                                        userObj.put("Date", date);
                                        ref.setValue(userObj);
                                    }
                                }
                            }
                        }
                    });
                }

                Query query = reference.orderByChild("userPhone").equalTo(currentUserPhone);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            {
                                dataSnapshot1.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent fintent = new Intent(getBaseContext(), GalleryFragment.class);
                fintent.putExtra("PREVIOUS_ACTIVITY", this.getClass().getSimpleName());
                startActivity(fintent);
                finish();
            }
        }
            if(resultCode == 1000) {
                String charge = "0";
                String time = data.getStringExtra("time");
                String date = data.getStringExtra("date");

                {
                    for (CartItem cartItem : myCart) {
                        int b = getIntent().getIntExtra("discount", 0);
                        int c = (int) (Float.parseFloat(cartItem.getPrice()) * b * Float.parseFloat(cartItem.getNumber()) / 100);


                        String currentDateTimeString1 = java.text.DateFormat.getDateTimeInstance().format(new Date());
                        orderReference = FirebaseDatabase.getInstance().getReference().child("Order").child(currentUserPhone + cartItem.getItem() + cartItem.getPer() + currentDateTimeString1);
                        cartItem.setDiscount(String.valueOf(c));
                        cartItem.setDelivery(charge);
                        cartItem.setTime(time);
                        cartItem.setDate(date);
                        cartItem.setPaid("False");
                        cartItem.setCurrentTime(String.valueOf(System.currentTimeMillis()));
                        orderReference.setValue(cartItem);

                        OrderForAdmin orderForAdmin = new OrderForAdmin();
                        orderForAdmin.setItem(cartItem.getItem());
                        orderForAdmin.setNumber(cartItem.getNumber());
                        orderForAdmin.setPrice(cartItem.getPrice());
                        orderForAdmin.setUserId(currentUserPhone);
                        orderForAdmin.setDiscount(String.valueOf(c));
                        orderForAdmin.setCharge(charge);
                        orderForAdmin.setTime(time);
                        orderForAdmin.setDate(date);
                        orderForAdmin.setPaid("False");

                        final String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                        adminOrder = FirebaseDatabase.getInstance().getReference().child("Order for admin").child(user.getPhoneNumber()).child(cartItem.getItem() + cartItem.getPer() + cartItem.getNumber() + currentDateTimeString);
                        adminOrder.setValue(orderForAdmin);


                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getPhoneNumber());
                        final CollectionReference collectionReference = db.collection("Registering users");

                        collectionReference.document(currentUserPhone).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                {

                                    if (value.exists()) {
                                        {

                                            Map<String, String> userObj = new HashMap<>();
                                            userObj.put("Mobile", value.getId());
                                            userObj.put("Name", value.getString("Name"));
                                            userObj.put("Pincode", value.getString("Pincode"));
                                            userObj.put("Address", value.getString("Address"));
                                            userObj.put("ReferralNumber", marketer);
                                            userObj.put("Time", time);
                                            userObj.put("Date", date);
                                            ref.setValue(userObj);
                                        }
                                    }
                                }
                            }
                        });
                    }

                    Query query = reference.orderByChild("userPhone").equalTo(currentUserPhone);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                {
                                    dataSnapshot1.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Intent fintent = new Intent(getBaseContext(), GalleryFragment.class);
                    fintent.putExtra("PREVIOUS_ACTIVITY", this.getClass().getSimpleName());
                    startActivity(fintent);
                    finish();
                }
            }
        }
    }
