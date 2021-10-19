package ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groceries.urabanseed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import model.MyOrder;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<MyOrder> myOrders;
    String selection = "The product is arriving at a later time";

    public OrderRecyclerAdapter(Context context, List<MyOrder> myOrders) {
        this.context = context;
        this.myOrders = myOrders;
    }

    @NonNull
    @Override
    public OrderRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.order, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final MyOrder order = myOrders.get(position);
        final String imageUrl;

        if(!order.getCurrentTime().equals(null)) {
            long orderTime = Long.parseLong(order.getCurrentTime());
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - orderTime) > 300) {
                ViewGroup layout = (ViewGroup) viewHolder.cancel.getParent();
                if (null != layout) //for safety only  as you are doing onClick
                    layout.removeView(viewHolder.cancel);
            }
        }

        if(order.getPaid().equals("True")){
            viewHolder.paid.setText("Amount Paid");
        }
        else{
            viewHolder.paid.setText("Cash on Delivery");
        }

        viewHolder.ietem.setText(order.getItem());
        viewHolder.price.setText("\u20B9" +order.getPrice());
        viewHolder.per.setText(order.getPer());

        viewHolder.quantity.setText(order.getNumber());

        imageUrl = order.getImageUrl();

        if (imageUrl != null) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(viewHolder.image);
        }
        else {
            viewHolder.image.setImageResource(R.drawable.transparent_logo);
            viewHolder.image.setAdjustViewBounds(true);
        }

    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView
                ietem,
                price,
                quantity,
                per,
        paid;
        DatabaseReference databaseReference;
        public FirebaseAuth firebaseAuth;

        public ImageButton image;

        DatabaseReference orderReference, orderForAdminReference, userReference;

        Button cancel;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            databaseReference = FirebaseDatabase.getInstance().getReference();
            per = itemView.findViewById(R.id.order_per);
            quantity = itemView.findViewById(R.id.order_quantity);
            firebaseAuth = FirebaseAuth.getInstance();
            ietem = itemView.findViewById(R.id.order_name_list);
            price = itemView.findViewById(R.id.order_price);
            cancel = itemView.findViewById(R.id.cancel);
            image = itemView.findViewById(R.id.order_image_list);
            paid = itemView.findViewById(R.id.paid);
            image.setOnClickListener(this);
            cancel.setOnClickListener(this);

            orderForAdminReference = FirebaseDatabase.getInstance().getReference().child("Order for admin").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
            orderReference = FirebaseDatabase.getInstance().getReference().child("Order");
            userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        }

        @Override
        public void onClick(View v) {
            MyOrder order = myOrders.get(getAdapterPosition());
            {
                ArrayList selectedItems = new ArrayList(); // Where we track the selected items
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                final CharSequence[] choice = {"The product is arriving at a later time", "Product is being delivered to a wrong address", "Product is not required anymore", "Cheaper alternative available for lesser price", "Bad review from friends/relatives after ordering the product"};

                // Set the dialog title
                builder.setTitle("Let us know the reason of your cancellation")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setMultiChoiceItems(choice, null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {
                                        if (isChecked) {
                                            // If the user checked the item, add it to the selected items
                                            selectedItems.add(choice[which]);
                                        } else if (selectedItems.contains(which)) {
                                            // Else, if the item is already in the array, remove it
                                            selectedItems.remove(Integer.valueOf(which));
                                        }
                                    }
                                })
                        // Set the action buttons
                        .setPositiveButton("Confirm Cancellation", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK, so save the selectedItems results somewhere
                                // or return them to the component that opened the dialog
                                userReference.child(order.getUserPhone()).child("deleteReason").child(order.getItem()).setValue(selectedItems);
                                Query query = orderReference.orderByChild("userPhone").equalTo(order.getUserPhone());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            if(dataSnapshot.child("item").getValue().equals(order.getItem())){
                                                dataSnapshot.getRef().removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                orderForAdminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            if(dataSnapshot.child("item").getValue().equals(order.getItem())){
                                                dataSnapshot.getRef().removeValue();
                                                ((Activity) context).recreate();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.show();
            }
        }
            }
}
