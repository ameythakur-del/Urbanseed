package com.example.saatwikadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saatwikadmin.Model.OrderForAdmin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder>{
    private Context context;
    private List<OrderForAdmin> orderForAdminList;

    public OrderRecyclerAdapter(Context context, List<OrderForAdmin> orderForAdmins) {
        this.context = context;
        this.orderForAdminList = orderForAdmins;
    }

    @NonNull
    @Override
    public OrderRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.soul, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecyclerAdapter.ViewHolder viewHolder, int position) {
        OrderForAdmin orderForAdmin = orderForAdminList.get(position);

        if(orderForAdmin.getPaid() != null) {
            if (orderForAdmin.getPaid().equals("True")) {
                viewHolder.paid.setText("Paid");
            } else {
                viewHolder.paid.setText("Not Paid");
            }
        }
        viewHolder.quantity.setText(orderForAdmin.getQuantity());
        viewHolder.ietem.setText(orderForAdmin.getItem());
        viewHolder.per.setText(orderForAdmin.getNumber());
        int a = Integer.parseInt(orderForAdmin.getNumber());
        float b = Float.parseFloat(orderForAdmin.getPrice());
        float c = a*b;
        viewHolder.price.setText("\u20B9" + c);
    }

    @Override
    public int getItemCount() {
        return orderForAdminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView
                ietem,
                price,
                per, paid, quantity;
        DatabaseReference databaseReference;

        public FirebaseAuth firebaseAuth;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            per = itemView.findViewById(R.id.number);
            firebaseAuth = FirebaseAuth.getInstance();
            ietem = itemView.findViewById(R.id.item);
            price = itemView.findViewById(R.id.price);
            paid = itemView.findViewById(R.id.paid);
            quantity = itemView.findViewById(R.id.quantity);
            final FirebaseUser user = firebaseAuth.getCurrentUser();
        }
    }
}



