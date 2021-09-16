package ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groceries.urabanseed.MyCart;
import com.groceries.urabanseed.R;

import java.util.ArrayList;
import java.util.List;

import model.Coupon;

public class CouponRecyclerAdapter extends RecyclerView.Adapter<CouponRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Coupon> couponList;
    private List<Coupon> couponListFull;

    public CouponRecyclerAdapter(Context context, List<Coupon> couponList) {
        this.context = context;
        this.couponList = couponList;
        couponListFull = new ArrayList<>(couponList);
    }

    @NonNull
    @Override
    public CouponRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.coupen, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponRecyclerAdapter.ViewHolder viewHolder, int position) {
        Coupon coupon = couponList.get(position);

        if(coupon != null)
        {
            viewHolder.code.setText(coupon.getCode());
            viewHolder.percent.setText(coupon.getPercent()+ "% OFF");
            viewHolder.condition.setText(coupon.getCondition());
        }
    }
    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView
                code,
                percent,
                apply, condition;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Availed");
        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            condition = itemView.findViewById(R.id.condition);
            code = itemView.findViewById(R.id.text_coupon);
            percent = itemView.findViewById(R.id.text_percent);
            apply = itemView.findViewById(R.id.apply10);
            apply.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == apply){
                int position = getAdapterPosition();
                Coupon coupon = couponList.get(position);

                reference.child(coupon.getCode()).child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(context, "You have already availed this coupon once.", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(context, "Availed", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, MyCart.class);
                            intent.putExtra("discount", Integer.parseInt(coupon.getPercent()));
                            intent.putExtra("coupon", coupon.getCode());
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }
}




