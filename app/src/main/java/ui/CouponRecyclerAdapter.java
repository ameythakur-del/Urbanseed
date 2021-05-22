package ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mondkars.saatwik.MyCart;
import com.mondkars.saatwik.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
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
            viewHolder.percent.setText("GET " + coupon.getPercent()+ "% OFF");
        }
    }
    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Coupon> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(couponListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Coupon coupon : couponListFull) {
                    if (coupon.getCode().toLowerCase().contains(filterPattern)) {
                        filteredList.add(coupon);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            couponList.clear();
            couponList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView
               code,
                percent,
                apply;

        DatabaseReference reference;
        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

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
                String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                reference = FirebaseDatabase.getInstance().getReference().child("Availed").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                reference.setValue(coupon);
                ((Activity) context).finish();
                context.startActivity(new Intent(context, MyCart.class));
            }
        }
    }
}




