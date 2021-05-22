package com.example.saatwikadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saatwikadmin.Model.MyOrder;
import com.example.saatwikadmin.Model.OrderForAdmin;
import com.example.saatwikadmin.Model.Pricing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<MyOrder> myCart;
    public Integer cost=0;
    String charge, min;
    public Integer ch=0;
    int Min = 0;
    String discount = "0", k = "0";
    int f =0;
    float d = 0;
    public String r="0";
    int off = 0;
    public CartRecyclerAdapter(Context context, List<MyOrder> myCart) {
        this.context = context;
        this.myCart = myCart;
    }

    @NonNull
    @Override
    public CartRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.cart_row, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartRecyclerAdapter.ViewHolder viewHolder, int position) {
        final MyOrder cartItem = myCart.get(position);
        final DatabaseReference dileveryReference, minReference, discountReference;
        dileveryReference = FirebaseDatabase.getInstance().getReference().child("charge").child("delivery");
        String h="amey";
        if(cartItem.getMobile() != null){
            h = cartItem.getMobile();
        }
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(h);
        minReference = FirebaseDatabase.getInstance().getReference().child("charge").child("Min");
        final DatabaseReference limit = FirebaseDatabase.getInstance().getReference().child("Availed").child(h);
        final DatabaseReference limitnot = FirebaseDatabase.getInstance().getReference().child("limit");
        final DatabaseReference adminOrder = FirebaseDatabase.getInstance().getReference().child("Order for admin").child(h);

        viewHolder.name.setText(cartItem.getName());
        viewHolder.alternate.setText(cartItem.getPincode());
        viewHolder.mobile.setText(cartItem.getMobile());

        final Pricing pricing = new Pricing();

        minReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                min = dataSnapshot.getValue().toString();
                pricing.setMin(min);

                dileveryReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        charge = dataSnapshot.getValue().toString();
                        pricing.setCharge(charge);

                        reference.child("Take Away").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    viewHolder.address.setText("Take Away");
                                    viewHolder.delivered.setText("Done");
                                    charge = "0";
                                    pricing.setCharge(charge);
                                }
                                else {
                                    viewHolder.address.setText(cartItem.getAddress());
                                }
                                limit.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        {
                                            {
                                                {
                                                    if (dataSnapshot.exists()) {
                                                        discount = dataSnapshot.child("percent").getValue().toString();
                                                        k =  dataSnapshot.child("code").getValue().toString();
                                                        pricing.setDiscount(discount);
                                                        pricing.setK(k);

                                                        limitnot.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()) {
                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                        if (dataSnapshot1.exists()) {
                                                                            if (dataSnapshot1.getValue().toString().equals(cartItem.getMobile() + pricing.getK())) {
                                                                                pricing.setDiscount("0");
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                    else {
                                                        pricing.setDiscount("0");
                                                        pricing.setK(null);
                                                    }

                                                            adminOrder.addValueEventListener(new ValueEventListener() {

                                                                @Override

                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    final List<OrderForAdmin> orderForAdminList;
                                                                    orderForAdminList = new ArrayList<>();
                                                                    cost = 0;
                                                                    off = 0;
                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                        {
                                                                            OrderForAdmin orderForAdmin = dataSnapshot1.getValue(OrderForAdmin.class);
                                                                            orderForAdminList.add(orderForAdmin);
                                                                            int temp = Integer.parseInt(orderForAdmin.getPrice());
                                                                            int temp2 = Integer.parseInt(orderForAdmin.getNumber());
                                                                            off = off + Integer.parseInt(orderForAdmin.getDiscount());
                                                                            cost = cost + temp*temp2;
                                                                        }
                                                                    }
                                                                    String c = String.valueOf(cost);
                                                                    viewHolder.numprice.setText("\u20B9" +c);
                                                                    f = Math.round(off);
                                                                    String g = String.valueOf(f);
                                                                    viewHolder.numdiscount.setText("\u20B9" + g);

                                                                    Min = Integer.parseInt(pricing.getMin());

                                                                    if(cost >= Min){
                                                                        viewHolder.numdelivery.setText("\u20B9" + "0");
                                                                        pricing.setCharge("0");
                                                                        ch = 0;
                                                                    }

                                                                    else {
                                                                        viewHolder.numdelivery.setText(pricing.getCharge());
                                                                        ch = Integer.valueOf(pricing.getCharge());
                                                                    }
                                                                    d = (Integer.parseInt(pricing.getDiscount()) * cost / 100);
                                                                    int t = cost + ch - f;
                                                                    String tot = String.valueOf(t);
                                                                    viewHolder.total.setText("\u20B9" + tot);
                                                                    viewHolder.orderRecyclerAdapter = new OrderRecyclerAdapter(context, orderForAdminList);
                                                                    viewHolder.recyclerView.setAdapter(viewHolder.orderRecyclerAdapter);
                                                                }
                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }

                                                            });
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (cartItem.getStatus() == null){
            viewHolder.pending.setText("Pending Aproval");
            viewHolder.pending.setTextColor(Color.parseColor("#ff0000"));
        }

        if (cartItem.getStatus() != null){
            viewHolder.pending.setText("status : " + cartItem.getStatus());
            viewHolder.pending.setTextColor(Color.parseColor("#008000"));
        }
        if (cartItem.getStatus() == "Approved by the admin, Waiting for Arrival !"){
            viewHolder.aprove.setText("Aproved");
        }

        if (cartItem.getStatus() == "Sorry ! We cant place your order."){
            viewHolder.decline.setText("Declined");
        }

        if (cartItem.getStatus() == "Out for delivery !"){
            viewHolder.out.setText("Pressed");
        }
    }

    @Override
    public int getItemCount() {
        return myCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView
                mobile, alternate, name, address, pending, numprice, numdelivery, numdiscount, total;
        public OrderRecyclerAdapter orderRecyclerAdapter;
        public RecyclerView recyclerView;
        DatabaseReference reference, totalReference;

        Button delivered, aprove, decline, out;
        DatabaseReference orderReference, databaseReference;

        public FirebaseAuth firebaseAuth;

        private String m_Text = "";

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            numprice = itemView.findViewById(R.id.num_price);
            numdelivery = itemView.findViewById(R.id.del_charge);
            numdiscount = itemView.findViewById(R.id.discount);
            pending = itemView.findViewById(R.id.writing_status);
            recyclerView = itemView.findViewById(R.id.cart_recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            name = itemView.findViewById(R.id.name);
            orderReference = FirebaseDatabase.getInstance().getReference().child("Users");
            databaseReference = FirebaseDatabase.getInstance().getReference().child("cart");
            mobile = itemView.findViewById(R.id.mobile);
            alternate = itemView.findViewById(R.id.alternate);
            delivered = itemView.findViewById(R.id.dilevered);
            delivered.setOnClickListener(this);
            total = itemView.findViewById(R.id.total_price);
            aprove = itemView.findViewById(R.id.aprove);
            aprove.setOnClickListener(this);
            decline = itemView.findViewById(R.id.decline);
            decline.setOnClickListener(this);
            address = itemView.findViewById(R.id.address);
            firebaseAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference().child("Marketers");
            totalReference = FirebaseDatabase.getInstance().getReference().child("Total Money");
            out = itemView.findViewById(R.id.out);
            out.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == delivered){
                int position = getAdapterPosition();
                final MyOrder cartItem = myCart.get(position);
                DatabaseReference limit = FirebaseDatabase.getInstance().getReference().child("Availed").child(cartItem.getMobile());
                final DatabaseReference limitnot = FirebaseDatabase.getInstance().getReference().child("limit");
                final String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                DatabaseReference availed = FirebaseDatabase.getInstance().getReference().child("Availed").child(cartItem.getMobile());

                totalReference.child(cartItem.getMobile()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(context, dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                            if(cartItem.getPrice() != null){
//                                a += Integer.parseInt(cartItem.getPrice());
//                                totalReference.child(cartItem.getMobile()).setValue(a);
                                reference.child(cartItem.getMobile()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot napshot) {
                                        if (napshot.exists()) {
                                            int a = Integer.parseInt(napshot.child("money").getValue().toString());
                                            a += Integer.parseInt(napshot.getValue().toString());
                                            Toast.makeText(context, "1", Toast.LENGTH_LONG).show();
                                            totalReference.child(cartItem.getMobile()).setValue(a);
                                            reference.child(cartItem.getMobile()).removeValue();
                                        }
                                        else{
                                            Toast.makeText(context, "2", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        else{
                            reference.child(cartItem.getMobile()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot napshot) {
                                    if (napshot.exists()) {
                                        int a = Integer.parseInt(napshot.child("money").getValue().toString());
                                        Toast.makeText(context, "3", Toast.LENGTH_LONG).show();
                                        totalReference.child(cartItem.getMobile()).setValue(a);
                                        reference.child(cartItem.getMobile()).removeValue();
                                    }
                                    else{
                                        Toast.makeText(context, "4", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                availed.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String k = dataSnapshot.child("code").getValue().toString();
                            limitnot.child(cartItem.getMobile() + currentDateTimeString).setValue(cartItem.getMobile() + k);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                final String a = cartItem.getMobile();
                Query query = FirebaseDatabase.getInstance().getReference().child("Order").orderByChild("userPhone").equalTo(a);
//                Query query = orderReference.orderByChild("userId").equalTo(a);
                final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Order history");
                FirebaseDatabase.getInstance().getReference().child("Special users").child(cartItem.getMobile()).removeValue();
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            {
                                   {
                                       String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                                       data.child(dataSnapshot1.getKey() + currentDateTimeString).setValue(dataSnapshot1.getValue());
                                       dataSnapshot1.getRef().removeValue();
                                    }
                                }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Query query2 = databaseReference.orderByChild("userPhone").equalTo(a);
//                Query query = orderReference.orderByChild("userId").equalTo(a);
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                        for (final DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                            {
                                {
                                    dataSnapshot4.getRef().removeValue();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, "Couldnt remove", Toast.LENGTH_LONG).show();
                    }
                });
                DatabaseReference adminOrder = FirebaseDatabase.getInstance().getReference().child("Order for admin").child(cartItem.getMobile());
                adminOrder.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (final DataSnapshot dataSnapshot4 : dataSnapshot.getChildren()) {
                            {
                                {
                                    dataSnapshot4.getRef().removeValue();
                                }
                            }
                        }
                    }





                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("Users").child(cartItem.getMobile());
                user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (final DataSnapshot dataSnapshot4 : dataSnapshot.getChildren()) {
                            {
                                {
                                    dataSnapshot4.getRef().removeValue();
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                ((Activity) context).recreate();
            }
            if (v == aprove){
                int position = getAdapterPosition();
                final MyOrder cartItem = myCart.get(position);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(cartItem.getMobile());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        {
                            cartItem.setStatus("Your order is approved by our team, Waiting for Arrival !");

                            final String a = cartItem.getMobile();
                            Query query = orderReference.orderByChild("Mobile").equalTo(a);

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        {
                                            {
                                                cartItem.setStatus("Your order is approved by our team, Waiting for Arrival !");
                                                HashMap map = new HashMap();
                                                map.put("status", "Your order is approved by our team, Waiting for Arrival !");
                                                dataSnapshot1.getRef().updateChildren(map);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            if (v == decline){
                int position = getAdapterPosition();
                final MyOrder cartItem = myCart.get(position);
                final DatabaseReference availed = FirebaseDatabase.getInstance().getReference().child("Availed").child(cartItem.getMobile());

                    final String a = cartItem.getMobile();
                        Query query = orderReference.orderByChild("Mobile").equalTo(a);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    {
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle("Sorry ! We cant place your order since");

// Set up the input
                                            final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                                            builder.setView(input);

// Set up the buttons
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String m_Text = "";
                                                    m_Text = input.getText().toString();
                                                    cartItem.setStatus("Sorry ! We cant place your order since ");
                                                    HashMap map = new HashMap();
                                                    map.put("status", "Sorry ! We cant place your order since ");
                                                    map.put("reason", m_Text);
                                                    dataSnapshot1.getRef().updateChildren(map);
                                                    availed.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()){
                                                                dataSnapshot.getRef().removeValue();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            });
                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });

                                            builder.show();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }

            if (v == out){

                int position = getAdapterPosition();
                final MyOrder cartItem = myCart.get(position);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(cartItem.getMobile());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            cartItem.setStatus("We are Out for delivery. Coming soon !");

                            final String a = cartItem.getMobile();
                            Query query = orderReference.orderByChild("Mobile").equalTo(a);

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        {
                                            {
                                                cartItem.setStatus("We are Out for delivery. Coming soon !");
                                                HashMap map = new HashMap();
                                                map.put("status", "We are Out for delivery. Coming soon !");
                                                dataSnapshot1.getRef().updateChildren(map);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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








