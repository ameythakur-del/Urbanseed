package com.groceries.urabanseed.ui.home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groceries.urabanseed.ContactUs;
import com.groceries.urabanseed.CouponActivity;
import com.groceries.urabanseed.GalleryFragment;
import com.groceries.urabanseed.MyCart;
import com.groceries.urabanseed.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import model.Image;
import model.Item;
import ui.ItemRecyclerAdapter;
import ui.SliderAdapterExample;

import static android.content.Context.NOTIFICATION_SERVICE;


public class HomeFragment extends Fragment {

    final String CHANNEL_ID = "personal notifications";
    final int NOTIFICATION_ID = 001;
    private RecyclerView recyclerView, recyclerview2;
    private EditText searchView;
    DatabaseReference reference,reference2, reference3;
    public List<Item> itemList;
    ChipGroup chipGroup;
    String tal;
    Timer timer;
    private Handler mHandler;
    public SliderView horizontal;
    public List<Image> snacksList;
    DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Stop");
    DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference().child("cart");
    DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference().child("Order History");
    float a, b, c;
    CardView cardView;
    TextView number, price;
    MaterialCardView linearLayout;
    ItemRecyclerAdapter hospitalRecyclerAdapter;
    private String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        return root;
    }

    private void sendNotification(){
        historyReference.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    createNotificationChannel();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID);
                    builder.setSmallIcon(R.drawable.transparent_logo);
                    builder.setContentTitle("Here is a Surprise Offer for you!!");
                    builder.setContentText("Tap on this notification and get 5% OFF on your first order");
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);

                    Intent intent = new Intent(getActivity(), CouponActivity.class);
                    intent.putExtra("coupon", "9403768656");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
                    notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            CharSequence name = "Personal Notifications";
            String description = "Include all personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: 1");
        cardView = (CardView) view.findViewById(R.id.cart_card);
        mHandler = new Handler();
        searchView = view.findViewById(R.id.search_bar);

        price = view.findViewById(R.id.total_price);
        linearLayout = view.findViewById(R.id.view_cart);
        number = view.findViewById(R.id.number_items);

        reference2 = FirebaseDatabase.getInstance().getReference().child("Snacks");
        recyclerView = view.findViewById(R.id.view);
        reference = FirebaseDatabase.getInstance().getReference().child("items");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        chipGroup = view.findViewById(R.id.chipGroup);

        ProgressBar progressBar = view.findViewById(R.id.progress);
        CardView cardView2 = view.findViewById(R.id.cardView8);

        horizontal = view.findViewById(R.id.horizontal_view);

        reference3 = FirebaseDatabase.getInstance().getReference().child("Off Tag");
        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


        FloatingActionButton fab = view.findViewById(R.id.fab2);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String text = "Hey there, I want some help regarding the urbanseed app.";// Replace with your message.

                    String toNumber = "919403768656"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
                    //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Log.d(TAG, "onViewCreated: Z");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("True")){
                    {
                        {
                            {
                                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        snacksList = new ArrayList<Image>();
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            {
                                                final Image image = dataSnapshot1.getValue(Image.class);
                                                {
                                                    snacksList.add(image);
                                                }
                                            }
                                        }
                                        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(getActivity(), snacksList);
                                        horizontal.setSliderAdapter(sliderAdapterExample);
                                        sliderAdapterExample.notifyDataSetChanged();
                                        horizontal.startAutoCycle();
                                        horizontal.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                                        horizontal.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
                                        horizontal.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                    }
                    Log.d(TAG, "onDataChange: 3A");
                }

                else{
                    {
                        {
                            {
                                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.d(TAG, "onDataChange: Z1");
                                        snacksList = new ArrayList<Image>();
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            {
                                                final Image image = dataSnapshot1.getValue(Image.class);
                                                {
                                                    snacksList.add(image);
                                                }
                                            }
                                        }
                                        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(getActivity(), snacksList);
                                        horizontal.setSliderAdapter(sliderAdapterExample);
                                        sliderAdapterExample.notifyDataSetChanged();
                                        horizontal.startAutoCycle();
                                        horizontal.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                                        horizontal.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
                                        horizontal.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                        Log.d(TAG, "onDataChange: Z2");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                Log.d(TAG, "onDataChange: 3B");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: 30");
                itemList = new ArrayList<Item>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    {
                        Item item = dataSnapshot1.getValue(Item.class);
                        if(item.getVisibility().equals(true)) {
                            itemList.add(item);
                        }
                    }
                }
                hospitalRecyclerAdapter = new ItemRecyclerAdapter(getActivity(), itemList);
                progressBar.setVisibility(View.INVISIBLE);
                cardView2.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(hospitalRecyclerAdapter);
                hospitalRecyclerAdapter.notifyDataSetChanged();
                Log.d(TAG, "onDataChange: 40");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Log.d(TAG, "onDataChange: 50");

                Chip chip = chipGroup.findViewById(i);

                if(chip != null) {
                    tal = chip.getText().toString();
                    hospitalRecyclerAdapter.filterList(tal);
                }
                Log.d(TAG, "onDataChange: 60");
            }
        });

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: 70");
                itemList = new ArrayList<Item>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    {
                        Item item = dataSnapshot1.getValue(Item.class);
                        if(item.getVisibility().equals(true)) {
                            itemList.add(item);
                        }
                    }
                }
                ItemRecyclerAdapter itemRecyclerAdapter = new ItemRecyclerAdapter(getActivity(), itemList);

                searchView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }



                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        itemRecyclerAdapter.getFilter().filter(s);
                                        recyclerView.setAdapter(itemRecyclerAdapter);
                                    }
                                });
                            }
                        }, 1000);
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {

                    }
                });
                Log.d(TAG, "onDataChange: 80");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d(TAG, "onDataChange: 5");

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Query query = cartReference.orderByChild("userPhone");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, "onDataChange: 90");
                    int count = 0;
                    c=0;
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        {
                            if(dataSnapshot1.child("userPhone").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString())){
                                cardView.setVisibility(View.VISIBLE);
                                count++;
                                a = Float.parseFloat(dataSnapshot1.child("number").getValue().toString());
                                b = Float.parseFloat(dataSnapshot1.child("price").getValue().toString());
                                c = c + a*b;
                            }
                        }
                    }
                    if(String.valueOf(count).equals("0")){
                        cardView.setVisibility(View.INVISIBLE);
                        fab.setTranslationY(0);
                    }
                    else if(String.valueOf(count).equals("1")){
                        String a = String.valueOf(count);
                        number.setText(a + " item");
                        fab.setTranslationY(-150);

                        sendNotification();
                    }
                    else {
                        fab.setTranslationY(-150);
                        String a = String.valueOf(count);
                        number.setText(a + " items");

                        sendNotification();
                    }
                    String k = String.valueOf(Math.round(c));
                    price.setText("\u20B9"+k);
                    Log.d(TAG, "onDataChange: 100");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), MyCart.class));
                }
            });
        }
        Log.d(TAG, "onDataChange: 6");
    }
}