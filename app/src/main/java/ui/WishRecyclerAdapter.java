package ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.groceries.urabanseed.Activity2;
import com.groceries.urabanseed.MyCart;
import com.groceries.urabanseed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import model.CartItem;
import model.Wish;

public class WishRecyclerAdapter extends RecyclerView.Adapter<WishRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Wish> wishList;

    public WishRecyclerAdapter(Context context, List<Wish> wishList) {
        this.context = context;
        this.wishList = wishList;
    }

    @NonNull
    @Override
    public WishRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.wish_row, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull WishRecyclerAdapter.ViewHolder viewHolder, int position) {
        final Wish wish = wishList.get(position);
        final String imageUrl;
        final ElegantNumberButton quantity;
        String number;

        viewHolder.ietem.setText(wish.getItem());
        viewHolder.price.setText("\u20B9" +wish.getPrice());
        viewHolder.delivery.setText(wish.getDelivery());
        viewHolder.per.setText(wish.getPer());
        viewHolder.quantity.setText(wish.getNumber());
        imageUrl = wish.getImageUrl();


        if (imageUrl != null) {
            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .into(viewHolder.image);
        }
        else {
            viewHolder.image.setImageResource(R.drawable.transparent_logo);
            viewHolder.image.setAdjustViewBounds(true);
        }


    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView
                ietem,
                price,
                delivery,
                quantity,
                per;
        DatabaseReference databaseReference, reference;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId;
        public Button buy, wishlist;
        ProgressBar progressBar = new ProgressBar(context);

        public FirebaseAuth firebaseAuth;

        public ImageButton image;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            wishlist = itemView.findViewById(R.id.wish_wishlist);
            databaseReference = FirebaseDatabase.getInstance().getReference();
            per = itemView.findViewById(R.id.wish_per);
            quantity = itemView.findViewById(R.id.wish_quantity);
            firebaseAuth = FirebaseAuth.getInstance();
            ietem = itemView.findViewById(R.id.wish_name_list);
            price = itemView.findViewById(R.id.wish_price);
            delivery = itemView.findViewById(R.id.wish_delivers_in);
            image = itemView.findViewById(R.id.wish_image_list);
            image.setOnClickListener(this);
            buy = itemView.findViewById(R.id.wish_zala);
            buy.setOnClickListener(this);
            wishlist.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v == buy) {
                int position = getAdapterPosition();
                Wish wish = wishList.get(position);
//
                reference = FirebaseDatabase.getInstance().getReference().child("wish").child(wish.getUserPhone() + wish.getItem());
                reference.removeValue();
            }

            if (v == wishlist){
                if (user != null) {
                    if (user != null) {
                         {
                            int position = getAdapterPosition();
                            Wish wish = wishList.get(position);
                            final String name = wish.getItem();
                            final String taste = wish.getTaste();
                            final String price = wish.getPrice();
                            final String deliviery = wish.getDelivery();
                            final String category = wish.getCategory();
                            final String imageUrl = wish.getImageUrl();
                            final String per = wish.getPer();
                            final String Number = wish.getNumber();
                            final String userId = currentUserId;
                            CartItem cartItem = new CartItem();
                            cartItem.setItem(name);
                            cartItem.setTaste(taste);
                            cartItem.setPrice(price);
                            cartItem.setPer(per);
                            cartItem.setImageUrl(imageUrl);
                            cartItem.setCategory(category);
                            cartItem.setDelivery(deliviery);
                            cartItem.setNumber(Number);
                            cartItem.setUserPhone(wish.getUserPhone());
                            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                            cartItem.setTime(currentDateTimeString);
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("cart").child(wish.getUserPhone() + wish.getItem());
                            databaseReference.setValue(cartItem);
                            context.startActivity(new Intent(context, MyCart.class));
                        }
                    } else {
                        Toast.makeText(context, "You need to log in first !", Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context, Activity2.class));
                        firebaseAuth.getCurrentUser();
                    }
                }
            }
        }
    }
}



