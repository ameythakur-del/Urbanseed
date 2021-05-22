package com.mondkars.saatwik;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.mondkars.saatwik.Utils.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    public Integer cost = 0, originalCost=0;
    private TextView notext, nowarning, stotal, fintotal;
    private ImageView noimage;
    public TextView total, address, name, dprice, dcharge, dtotal, discount, original;
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
    int g=0;
    int fin=0;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        myCart = new ArrayList<CartItem>();
        notext = findViewById(R.id.notext);
        nowarning = findViewById(R.id.nowarning);
        noimage = findViewById(R.id.noimage);
        original = findViewById(R.id.original_num_price);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        offer = findViewById(R.id.offer);
        cartIcon = findViewById(R.id.cart_icon);
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
        dprice = findViewById(R.id.num_price);
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

        giftReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    stotal.setText("\u20B9" + snapshot.child("money").getValue().toString());
                    fin = Integer.parseInt(snapshot.child("money").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                                                int temp = Integer.parseInt(ameya.getPrice());
                                                int temp2 = Integer.parseInt(ameya.getNumber());
                                                int temp3 = Integer.parseInt(ameya.getOriginal());
                                                cost = cost + temp * temp2;
                                                originalCost = originalCost + temp2*temp3;
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
                                            int c = (cost * b / 100);
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

                                                    original.setText("\u20B9" + originalCost);
                                                    if (cost >= Min) {
                                                        total.setText("\u20B9" + (cost - d));
                                                        dprice.setText("\u20B9" + (originalCost - cost));
                                                        dcharge.setText("Free");
                                                        dcharge.setTextColor(Color.parseColor("#008000"));
                                                        dtotal.setText("\u20B9" + (cost - d));
                                                        fintotal.setText("\u20B9" + (cost - d - fin));
                                                    } else {
                                                        total.setText("\u20B9" + (cost + ch - d));
                                                        dprice.setText("\u20B9" + (originalCost - cost));
                                                        dcharge.setText("\u20B9" + charge);
                                                        dtotal.setText("\u20B9" + (cost + ch - d));
                                                        fintotal.setText("\u20B9" + (cost - d - fin));
                                                    }
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
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            donor.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(user.getPhoneNumber()).exists()) {
                        return;
                    } else {
                        donor.child(user.getPhoneNumber()).setValue(user.getPhoneNumber());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            if (marketer != null) {
                {
                    marketers.child(marketer).child("money").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                int temp = Integer.parseInt(snapshot.getValue().toString());
                                d += temp;
                                snapshot.getRef().setValue(d);
                            } else {
                                snapshot.getRef().setValue(d);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            if (resultCode == RESULT_OK) {

                for (CartItem cartItem : myCart) {
                    String currentDateTimeString1 = java.text.DateFormat.getDateTimeInstance().format(new Date());
                    orderReference = FirebaseDatabase.getInstance().getReference().child("Order").child(user.getPhoneNumber() + cartItem.getItem() + currentDateTimeString1);
                    int b = getIntent().getIntExtra("discount", 0);
                    int c = (Integer.parseInt(cartItem.getPrice()) * b * Integer.parseInt(cartItem.getNumber()) / 100);
                    giftReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
//                                stotal.setText("\u20B9" + snapshot.child("money").getValue().toString());
//                                fintotal.setText("\u20B9" + (cost + ch - d - Integer.parseInt(snapshot.child("money").getValue().toString())));
                                g = Integer.parseInt(snapshot.child("money").getValue().toString());

                            }
                            else{
                                g=0;
                            }
                            OrderForAdmin orderForAdmin = new OrderForAdmin();
                            orderForAdmin.setItem(cartItem.getItem());
                            orderForAdmin.setNumber(cartItem.getNumber());
                            orderForAdmin.setPrice(cartItem.getPrice());
                            orderForAdmin.setUserPhone(cartItem.getUserPhone());
                            orderForAdmin.setDiscount(String.valueOf(c + g));
                            final String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                            adminOrder = FirebaseDatabase.getInstance().getReference().child("Order for admin").child(user.getPhoneNumber()).child(cartItem.getItem() + cartItem.getPer() + cartItem.getNumber() + currentDateTimeString);
                            adminOrder.setValue(orderForAdmin);

                            if (!orderForAdmin.getDiscount().equals("0")) {
                                databaseReference1.child(user.getPhoneNumber()).setValue(user.getPhoneNumber());
                            }

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
                                                ref.setValue(userObj);
                                            }
                                        }
                                    }
                                }
                            });
                            Intent fintent = new Intent(getBaseContext(), GalleryFragment.class);
                            fintent.putExtra("PREVIOUS_ACTIVITY", this.getClass().getSimpleName());
                            startActivity(fintent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            if (resultCode == RESULT_FIRST_USER) {

                for (CartItem cartItem : myCart) {
                    String currentDateTimeString1 = java.text.DateFormat.getDateTimeInstance().format(new Date());
                    orderReference = FirebaseDatabase.getInstance().getReference().child("Order").child(currentUserPhone + cartItem.getItem() + cartItem.getNumber() + currentDateTimeString1);
                    int b = getIntent().getIntExtra("discount", 0);
                    int c = (Integer.parseInt(cartItem.getPrice()) * b * Integer.parseInt(cartItem.getNumber()) / 100);

                    giftReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
//                                stotal.setText("\u20B9" + snapshot.child("money").getValue().toString());
//                                fintotal.setText("\u20B9" + (cost + ch - d - Integer.parseInt(snapshot.child("money").getValue().toString())));
                                g = Integer.parseInt(snapshot.child("money").getValue().toString());
                            }
                            else{
                                g = 0;
                            }

                            cartItem.setDiscount(String.valueOf(c + g));
                            orderReference.setValue(cartItem);

                            OrderForAdmin orderForAdmin = new OrderForAdmin();
                            orderForAdmin.setItem(cartItem.getItem());
                            orderForAdmin.setNumber(cartItem.getNumber());
                            orderForAdmin.setPrice(cartItem.getPrice());
                            orderForAdmin.setUserPhone(cartItem.getUserPhone());
                            orderForAdmin.setDiscount(String.valueOf(c + g));

                            final String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                            adminOrder = FirebaseDatabase.getInstance().getReference().child("Order for admin").child(user.getPhoneNumber()).child(cartItem.getItem() + cartItem.getPer() + cartItem.getNumber() + currentDateTimeString);
                            adminOrder.setValue(orderForAdmin);

                            if (!orderForAdmin.getDiscount().equals("0")) {
                                databaseReference1.child(user.getPhoneNumber()).setValue(user.getPhoneNumber());
                            }

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
                                                ref.setValue(userObj);
                                            }
                                        }
                                    }
                                }
                            });
                            Intent fintent = new Intent(getBaseContext(), GalleryFragment.class);
                            fintent.putExtra("PREVIOUS_ACTIVITY", this.getClass().getSimpleName());
                            startActivity(fintent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        }
    }
}