package ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groceries.urabanseed.R;

import java.util.List;

import model.Quantity;

public class QuantityRecyclerAdapter extends RecyclerView.Adapter<QuantityRecyclerAdapter.ViewHolder>{

    private Context context;
    private List<Quantity> quantities;
    private CompoundButton lastCheckedRB = null;
    static int count = 0;
    String mrpv = "0";

    public QuantityRecyclerAdapter(Context context, List<Quantity> quantities) {
        this.context = context;
        this.quantities = quantities;
    }

    @NonNull
    @Override
    public QuantityRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.quantity, parent, false);
        return new QuantityRecyclerAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull QuantityRecyclerAdapter.ViewHolder holder, int position) {
        Quantity quantity = quantities.get(position);

        holder.quantity.setText(quantity.getQuantity());
        holder.price.setText("\u20B9" + quantity.getPriced());
        if(quantity.getMrp() != null){
            if(quantity.getMrp().equals(quantity.getPriced())){

            }
            else {
                mrpv = quantity.getMrp();
                holder.mrp.setText("\u20B9" + quantity.getMrp());
                holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int tag = (int) buttonView.getTag();
                if (lastCheckedRB == null) {
                    lastCheckedRB = buttonView;
                } else if (tag != (int) lastCheckedRB.getTag()) {
                    lastCheckedRB.setChecked(false);
                    lastCheckedRB = buttonView;
                }
                String Quantity = null, price = null;
                Quantity = quantity.getQuantity();
                price = quantity.getPriced().toString();
                Intent intent = new Intent("custom-message");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intent.putExtra("quantity", Quantity);
                intent.putExtra("price", price);
                intent.putExtra("mrp", mrpv);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        holder.radioButton.setTag(position);
        if(holder.radioButton.getTag().equals(0)){
            holder.radioButton.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return quantities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView quantity, price, mrp;
        RadioButton radioButton;

        public ViewHolder(View view, Context context) {
            super(view);

            quantity = view.findViewById(R.id.quantity1);
            price = view.findViewById(R.id.price1);
            radioButton = view.findViewById(R.id.radio);
            mrp = view.findViewById(R.id.price2);

        }
    }
}
