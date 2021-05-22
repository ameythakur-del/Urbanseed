package com.example.saatwikadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saatwikadmin.Utils.Tag;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageRecyclerAdapter2 extends RecyclerView.Adapter<ImageRecyclerAdapter2.ViewHolder> {
    private Context context;
    private List<Tag> imageList;

    public ImageRecyclerAdapter2(Context context, List<Tag> imageList) {
        this.context = context;
        this.imageList = imageList;
    }
    @NonNull
    @Override
    public ImageRecyclerAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.image, viewGroup, false);
        return new ImageRecyclerAdapter2.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageRecyclerAdapter2.ViewHolder holder, int position) {
        Tag tag = imageList.get(position);
        String imageUrl = tag.getImageUrl();
        Picasso.get()
                .load(imageUrl)
                .fit()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        public Button button;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("masale");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            context = context;
            button = itemView.findViewById(R.id.delete_button);
            imageView = itemView.findViewById(R.id.image_fragment);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Tag tag = imageList.get(position);
            StorageReference photoRef = firebaseStorage.getReferenceFromUrl(tag.getImageUrl());
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Tag image deleted", Toast.LENGTH_LONG).show();
                }
            });
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        if(dataSnapshot1.child("imageUrl").getValue().toString().equals(tag.getImageUrl().toString())){
                            dataSnapshot1.getRef().removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
