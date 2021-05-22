package ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mondkars.saatwik.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.MyOrder;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<MyOrder> myOrders;

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
    public void onBindViewHolder(@NonNull OrderRecyclerAdapter.ViewHolder viewHolder, int position) {
        final MyOrder order = myOrders.get(position);
        final String imageUrl;

        viewHolder.ietem.setText(order.getItem());
        viewHolder.price.setText("\u20B9" +order.getPrice());
        viewHolder.delivery.setText(order.getDelivery());
        viewHolder.per.setText(order.getPer());

        viewHolder.quantity.setText(order.getNumber());

        imageUrl = order.getImageUrl();


        if (imageUrl != null) {
            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .into(viewHolder.image);
        }
        else {
            viewHolder.image.setImageResource(R.drawable.posts);
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
                delivery,
                quantity,
                per;
        DatabaseReference databaseReference;
        public FirebaseAuth firebaseAuth;

        public ImageButton image;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            databaseReference = FirebaseDatabase.getInstance().getReference();
            per = itemView.findViewById(R.id.order_per);
            quantity = itemView.findViewById(R.id.order_quantity);
            firebaseAuth = FirebaseAuth.getInstance();
            ietem = itemView.findViewById(R.id.order_name_list);
            price = itemView.findViewById(R.id.order_price);
            delivery = itemView.findViewById(R.id.order_delivers_in);
            image = itemView.findViewById(R.id.order_image_list);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
            }
}
