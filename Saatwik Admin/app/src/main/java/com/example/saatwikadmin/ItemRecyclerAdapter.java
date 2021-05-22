package com.example.saatwikadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saatwikadmin.Model.Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Item> itemList;
    private List<Item> itemListFull;

    public ItemRecyclerAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
        itemListFull = new ArrayList<>(itemList);
    }

    @NonNull
    @Override
    public ItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_row, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecyclerAdapter.ViewHolder viewHolder, int position) {
        Item item = itemList.get(position);
        String imageUrl;

        viewHolder.ietem.setText(item.getItem());
        viewHolder.price.setText("\u20B9" + item.getPrice());
        imageUrl = item.getImageUrl();


        Picasso.get()
                .load(imageUrl)
                .fit()
                .into(viewHolder.image);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Item> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Item item : itemListFull) {
                    if (item.getItem().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemList.clear();
            itemList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView
                ietem,
                price;
        DatabaseReference databaseReference;
        ProgressDialog progressDialog;
        Boolean addedToCart;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId, number;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Images");

        public long countPosts = 0;
        public Button buy, not, available, visible, not_visible;
        public FirebaseAuth firebaseAuth;
        public FirebaseAuth.AuthStateListener authStateListener;


        public ImageButton image;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            
            firebaseAuth = FirebaseAuth.getInstance();
            ietem = itemView.findViewById(R.id.item_name_list);
            price = itemView.findViewById(R.id.price);

            image = itemView.findViewById(R.id.item_image_list);
            image.setOnClickListener(this);
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            buy = itemView.findViewById(R.id.buy_now);
            not = itemView.findViewById(R.id.not_available);
            not.setOnClickListener(this);
            buy.setOnClickListener(this);
            available = itemView.findViewById(R.id.available);
            available.setOnClickListener(this);

            visible = itemView.findViewById(R.id.visible);
            not_visible = itemView.findViewById(R.id.not_visible);

            visible.setOnClickListener(this);
            not_visible.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v == buy) {
                int position = getAdapterPosition();
                Item item = itemList.get(position);
                String name = item.getItem();
                storageReference.child(name).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Item deleted with the image", Toast.LENGTH_LONG).show();
                    }
                });
                databaseReference = FirebaseDatabase.getInstance().getReference().child("items").child(name);
                databaseReference.removeValue();
            }
            if (v == not) {
                int position = getAdapterPosition();
                Item item = itemList.get(position);
                String name = item.getItem();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("items").child(name);
                databaseReference.child("available").setValue("False");
                Toast.makeText(context, "Done", Toast.LENGTH_LONG).show();
            }
            if (v == available){
                int position = getAdapterPosition();
                Item item = itemList.get(position);
                String name = item.getItem();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("items").child(name);
                databaseReference.child("available").setValue("True");
                Toast.makeText(context, "Done", Toast.LENGTH_LONG).show();
            }
            if (v == visible){
                int position = getAdapterPosition();
                Item item = itemList.get(position);
                String name = item.getItem();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("items").child(name);
                databaseReference.child("visibility").setValue(true);
                Toast.makeText(context, "Done", Toast.LENGTH_LONG).show();
            }
            if (v == not_visible){
                int position = getAdapterPosition();
                Item item = itemList.get(position);
                String name = item.getItem();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("items").child(name);
                databaseReference.child("visibility").setValue(false);
                Toast.makeText(context, "Done", Toast.LENGTH_LONG).show();
            }
        }
    }
}



