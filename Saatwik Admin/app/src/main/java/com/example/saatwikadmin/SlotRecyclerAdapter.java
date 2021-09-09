package com.example.saatwikadmin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SlotRecyclerAdapter extends RecyclerView.Adapter<SlotRecyclerAdapter.ViewHolder>{
    private Context context;
    private List<Slot> slots;

    public SlotRecyclerAdapter(Context context, List<Slot> slots) {
        this.context = context;
        this.slots = slots;
    }

    @NonNull
    @Override
    public SlotRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.slot, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotRecyclerAdapter.ViewHolder viewHolder, int position) {
        Slot slot = slots.get(position);
        
        viewHolder.time.setText(slot.getStarttime() + " - " + slot.getClosetime());
        viewHolder.charge.setText(slot.getCharges());
       
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView
                time,
                charge;

        Button delete;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Delivery Slots");

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            time = itemView.findViewById(R.id.time);
            charge = itemView.findViewById(R.id.charge);

            delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Slot slot = slots.get(getAdapterPosition());

            databaseReference.child(slot.getStarttime() + " - " + slot.getClosetime()).removeValue();

            ((Activity) context).recreate();
        }
    }
}



