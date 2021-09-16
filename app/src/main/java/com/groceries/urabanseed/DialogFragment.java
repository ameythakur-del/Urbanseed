package com.groceries.urabanseed;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import model.CartItem;
import model.Item;
import model.Quantity;
import ui.QuantityRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogFragment extends androidx.fragment.app.DialogFragment implements View.OnClickListener {

    Item item;
    TextView title, description, delivery;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Quantity> quantities;
    QuantityRecyclerAdapter quantityRecyclerAdapter;
    TextView textView;
    NumberPicker numberPicker;
    MaterialButton button;
    DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Stop");
    HorizontalNumberPicker quantity;
    String amey=null, price, mrp;
    ImageView image;

    public static DialogFragment newInstance(Item item) {
        DialogFragment frag = new DialogFragment();
        Bundle args = new Bundle();
        frag.setItem(item);
        frag.setArguments(args);
        return frag;
    }

    private void setItem(Item item) {
        this.item = item;
    }


    public DialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        return dialog;
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            amey = intent.getStringExtra("quantity");
            price = intent.getStringExtra("price");
            mrp = intent.getStringExtra("mrp");
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
        return inflater.inflate(R.layout.fragment_dialog, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        CardView cardView = view.findViewById(R.id.card);
        cardView.setBackgroundResource(R.drawable.card);
        image = view.findViewById(R.id.item_image);
        button = view.findViewById(R.id.dialog_cart);
        button.setOnClickListener(this);
        quantity = view.findViewById(R.id.number_dialog);

        title = view.findViewById(R.id.dialog_title);
        description = view.findViewById(R.id.dialog_discription);
        delivery = view.findViewById(R.id.dialog_delivery);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("items").child(item.getItem());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                title.setText(snapshot.child("item").getValue().toString());
                description.setText(snapshot.child("taste").getValue().toString());
                delivery.setText(snapshot.child("delivery").getValue().toString());
                if (snapshot.child("imageUrl").getValue() != null) {
                    Picasso.get()
                            .load(snapshot.child("imageUrl").getValue().toString())
                            .fit()
                            .into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference = databaseReference.child("Quantity");

        int c=0;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quantities = new ArrayList<Quantity>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Quantity quantity = dataSnapshot.getValue(Quantity.class);
                    quantities.add(quantity);
                }
                QuantityRecyclerAdapter quantityRecyclerAdapter = new QuantityRecyclerAdapter(getActivity(), quantities);
                recyclerView.setAdapter(quantityRecyclerAdapter);
                quantityRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onClick(View v) {
        if(v == button){
            {
               {
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue().toString().equals("True")){
                                Toast.makeText(getActivity(), "Sorry! We are not serving now", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if(amey == null){
                                Toast.makeText(getActivity(), "Please select the quantity", Toast.LENGTH_LONG).show();
                                return;
                            }

//                            if(Quantity.equals(null)){
//                                Toast.makeText(getContext(), "Please select the quantity", Toast.LENGTH_LONG).show();
//                            }
                            else if (!String.valueOf(quantity.getValue()).equals("0")) {
                                        final String name = item.getItem();
                                        final String taste = item.getTaste();
                                        final String Price = price;
                                        final String deliviery = item.getDelivery();
                                        final String category = item.getCategory();
                                        final String imageUrl = item.getImageUrl();
                                        final String per = amey;
                                        final String Number = String.valueOf(quantity.getValue());
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        final String mobile = user.getPhoneNumber();
                                        CartItem cartItem = new CartItem();
                                        cartItem.setItem(name);
                                        cartItem.setTaste(taste);
                                        cartItem.setPrice(price);
                                        cartItem.setPer(per);
                                        cartItem.setImageUrl(imageUrl);
                                        cartItem.setCategory(category);
                                        cartItem.setDelivery(deliviery);
                                        cartItem.setOriginal(mrp);
                                        cartItem.setNumber(String.valueOf(quantity.getValue()));
                                        cartItem.setUserPhone(user.getPhoneNumber());
                                        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                                        cartItem.setTime(currentDateTimeString);
                                        databaseReference = FirebaseDatabase.getInstance().getReference().child("cart").child(user.getPhoneNumber().toString() + cartItem.getItem() + amey);
                                        databaseReference.setValue(cartItem);
                                        getDialog().cancel();
                                        Toast.makeText(getActivity(), "Added to cart", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Please select the number of items you want to buy", Toast.LENGTH_LONG).show();
                                    }
                                }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        }
    }
}