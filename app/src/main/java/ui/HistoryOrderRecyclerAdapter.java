package ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groceries.urabanseed.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.HistoryOrder;

public class HistoryOrderRecyclerAdapter extends RecyclerView.Adapter<HistoryOrderRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<HistoryOrder> historyOrders;

    public HistoryOrderRecyclerAdapter(Context context, List<HistoryOrder> historyOrders) {
        this.context = context;
        this.historyOrders = historyOrders;
    }

    @NonNull
    @Override
    public HistoryOrderRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.history, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryOrderRecyclerAdapter.ViewHolder viewHolder, int position) {
        final HistoryOrder historyOrder = historyOrders.get(position);

       viewHolder.item.setText(historyOrder.getItem());
       viewHolder.number.setText("Qty: " + historyOrder.getNumber());
       viewHolder.quantity.setText(historyOrder.getQuantity());

        viewHolder.databaseReference.child(historyOrder.getItem()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.child("imageUrl").exists()) {
                        String imageUrl = snapshot.child("imageUrl").getValue().toString();

                        if (imageUrl != null) {
                            Picasso.get()
                                    .load(imageUrl)
                                    .fit()
                                    .into(viewHolder.imageView);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

       viewHolder.databaseReference.child(historyOrder.getItem()).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()) {
                   if ((snapshot.child("available").exists() && snapshot.child("available").getValue().equals("False")) || snapshot.child("visibility").getValue().equals(false)) {
                       viewHolder.price.setText("Not Available");
                       viewHolder.price.setTextColor(Color.RED);
                   } else {
                      viewHolder.price.setText("\u20B9" + historyOrder.getPrice());
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


    }

    @Override
    public int getItemCount() {
        return historyOrders.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView item, price, number, quantity;
        DatabaseReference databaseReference;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            imageView = itemView.findViewById(R.id.image);
            item = itemView.findViewById(R.id.item);
            price = itemView.findViewById(R.id.price);
            number = itemView.findViewById(R.id.number);
            quantity = itemView.findViewById(R.id.quantity);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("items");
        }
    }
}



