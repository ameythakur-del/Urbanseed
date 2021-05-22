package com.example.saatwikadmin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saatwikadmin.Model.Gift;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GiftRecyclerAdapter extends RecyclerView.Adapter<GiftRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Gift> gifts;
    private List<Gift> giftsFull;

    public GiftRecyclerAdapter(Context context, List<Gift> gifts) {
        this.context = context;
        this.gifts = gifts;
        giftsFull = new ArrayList<>(gifts);
    }

    @NonNull
    @Override
    public GiftRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.gift, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftRecyclerAdapter.ViewHolder viewHolder, int position) {
        final Gift gift = gifts.get(position);

        viewHolder.mobile.setText(gift.getMobile());
        viewHolder.price.setText("\u20B9" + gift.getPrice());

    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Gift> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(gifts);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Gift gift : giftsFull) {
                    if (gift.getMobile() != null) {
                        if (gift.getMobile().toLowerCase().contains(filterPattern)) {
                            filteredList.add(gift);
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            gifts.clear();
            gifts.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView
               mobile, price;
        DatabaseReference reference, totalReference;
        public Button paid;
        ProgressBar progressBar = new ProgressBar(context);

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            mobile = itemView.findViewById(R.id.mobile_number);
            price = itemView.findViewById(R.id.gift_price);
            paid = itemView.findViewById(R.id.paid_button);
            paid.setOnClickListener(this);
            reference = FirebaseDatabase.getInstance().getReference().child("Marketers");
            totalReference = FirebaseDatabase.getInstance().getReference().child("Total Money");
        }

        @Override
        public void onClick(View v) {

            if (v == paid) {
                int position = getAdapterPosition();
                Gift gift = gifts.get(position);
                totalReference.child(gift.getMobile()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            long a = (long) dataSnapshot.getValue();
                            Toast.makeText(context, dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                            a+=gift.getPrice();
                            totalReference.child(gift.getMobile()).setValue(a);
                        }
                        else{
                            totalReference.child(gift.getMobile()).setValue(gift.getPrice());
                        }
                        reference.child(gift.getMobile()).removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}




