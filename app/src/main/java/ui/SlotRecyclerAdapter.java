package ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groceries.urabanseed.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Slot;

public class SlotRecyclerAdapter extends RecyclerView.Adapter<SlotRecyclerAdapter.ViewHolder>{
    private Context context;
    private List<Slot> slots;
    private CompoundButton lastCheckedRB = null;
    int payment;
    String date;
    String start, end;

    public SlotRecyclerAdapter(Context context, List<Slot> slots, int payment, String date) {
        this.context = context;
        this.slots = slots;
        this.payment = payment;
        this.date = date;
    }

    @NonNull
    @Override
    public SlotRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.slot, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull SlotRecyclerAdapter.ViewHolder viewHolder, int position) {
        Slot slot = slots.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String str = sdf.format(new Date());

        if(Integer.parseInt(slot.getStarttime()) < 12){
            start = slot.getStarttime() + " AM";
        }

        else if(Integer.parseInt(slot.getStarttime()) == 12){
            start = slot.getStarttime() + " PM";
        }

        else{
            start = (Integer.parseInt(slot.getStarttime()) - 12) + " PM";
        }

        if(Integer.parseInt(slot.getClosetime()) < 12){
            end = slot.getClosetime() + " AM";
        }

        else if(Integer.parseInt(slot.getClosetime()) == 12){
            end = slot.getClosetime() + " PM";
        }

        else{
            end = (Integer.parseInt(slot.getClosetime()) - 12) + " PM";
        }

        viewHolder.time.setText(start + " - " + end);
        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));

            Boolean a = false;
            for(int i=0; i<slots.size(); i++){
                if(slots.get(i).getBool()){
                   a = true;
                }
            }
            if(!a){
                int i=0;
                while (((i < slots.size()) && (date.equals(String.valueOf(LocalDate.now().getDayOfMonth()) + " " + String.valueOf(LocalDate.now().getMonth()))) && (Integer.parseInt(str) + 2 >= Integer.parseInt(slots.get(i).getStarttime())))){
                    i++;
                }
                if(i < slots.size()) {
                    String price = slots.get(i).getCharges().toString();
                    if (Integer.parseInt(slots.get(i).getStarttime()) < 12) {
                        start = slots.get(i).getStarttime() + " AM";
                    } else if (Integer.parseInt(slots.get(i).getStarttime()) == 12) {
                        start = slots.get(i).getStarttime() + " PM";
                    } else {
                        start = (Integer.parseInt(slots.get(i).getStarttime()) - 12) + " PM";
                    }

                    if (Integer.parseInt(slots.get(i).getClosetime()) < 12) {
                        end = slots.get(i).getClosetime() + " AM";
                    } else if (Integer.parseInt(slots.get(i).getClosetime()) == 12) {
                        end = slots.get(i).getClosetime() + " PM";
                    } else {
                        end = (Integer.parseInt(slots.get(i).getClosetime()) - 12) + " PM";
                    }
                    String time = start + " - " + end;
                    String amount = slots.get(i).getAmount().toString();

                    Intent intent = new Intent("custom-message");
                    slots.get(i).setBool(true);
                    intent.putExtra("price", price);
                    intent.putExtra("time", time);
                    intent.putExtra("amount", amount);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }

        if(slots.get(position).getBool()){
            viewHolder.card.setBackgroundColor(context.getResources().getColor(R.color.blue_btn_bg_color));
        }
        else{
            viewHolder.card.setBackgroundColor(Color.WHITE);
        }

        if((date.equals(String.valueOf(LocalDate.now().getDayOfMonth()) + " " + String.valueOf(LocalDate.now().getMonth()))) && (Integer.parseInt(str) + 2 >= Integer.parseInt(slot.getStarttime()))){
            viewHolder.charge.setText("Choose Different date to select this time slot");
        }
        else
            {
            if (slot.getCharges().equals("0")) {
                viewHolder.charge.setText("Free Delivery");
                viewHolder.charge.setTextColor(Color.parseColor("#28a745"));
            } else {
                if (payment <= Integer.parseInt(slot.getAmount())) {
                    viewHolder.charge.setText("Delivery charges of " + "\u20B9" + slot.getCharges() + " will be applied");
                } else {
                    viewHolder.charge.setText("Free Delivery");
                    viewHolder.charge.setTextColor(Color.parseColor("#28a745"));
                }
            }


            viewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                doctorsFragmentBookAppointment.clcikoncaldendar(holder, position, arrayList);
                    {
                        for (int i = 0; i < slots.size(); i++) {
                            if (slots.get(i).getBool()) {
                                slots.get(i).setBool(false);
                            }
                        }

                        String price = null;

                        price = slot.getCharges().toString();
                        String time = start + " - " + end;
                        String amount = slot.getAmount().toString();

                        Intent intent = new Intent("custom-message");
                        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                        intent.putExtra("price", price);
                        intent.putExtra("time", time);
                        intent.putExtra("amount", amount);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                        slots.get(position).setBool(true);
                        notifyDataSetChanged();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView
                charge;

        TextView time;

        CardView card;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Delivery Slots");

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            time = itemView.findViewById(R.id.time);
            charge = itemView.findViewById(R.id.charge);
            card = itemView.findViewById(R.id.card);
        }
    }
}



