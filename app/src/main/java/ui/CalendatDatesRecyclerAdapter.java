package ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groceries.urabanseed.R;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import model.Model;

public class CalendatDatesRecyclerAdapter extends RecyclerView.Adapter<CalendatDatesRecyclerAdapter.myviewholder> {
    ArrayList<Model> arrayList;
    Context context;

    public CalendatDatesRecyclerAdapter(ArrayList<Model> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendardaterecycleritem, parent,false);
        return new CalendatDatesRecyclerAdapter.myviewholder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
//        java.text.DateFormat dateFormat = DateFormat.getDateFormat(context);

        if(arrayList.get(position).getaBoolean()){
            holder.frameLayout.setBackgroundColor(context.getResources().getColor(R.color.blue_btn_bg_color));
        }
        else{
            holder.frameLayout.setBackgroundColor(Color.WHITE);
        }

        if(arrayList.get(position).getDate().equals(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))){
            holder.day.setText("Today");
        }
        else {
            holder.day.setText(String.valueOf(DateFormat.format("EE", arrayList.get(position).getDate())));
        }
        holder.date.setText(String.valueOf(DateFormat.format("dd", arrayList.get(position).getDate())));
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                doctorsFragmentBookAppointment.clcikoncaldendar(holder, position, arrayList);
                for(int i=0; i<arrayList.size(); i++){
                    if(arrayList.get(i).getaBoolean()){
                        arrayList.get(i).setaBoolean(false);
                    }
                }

                Intent intent = new Intent("custom-message");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));

                ZoneId defaultZoneId = ZoneId.systemDefault();

                //Converting the date to Instant
                Instant instant = arrayList.get(position).getDate().toInstant();

                //Converting the Date to LocalDate
                LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();

                intent.putExtra("date", String.valueOf(localDate.getDayOfMonth() + " " + localDate.getMonth()));

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                arrayList.get(position).setaBoolean(true);
                notifyDataSetChanged();
//                interact.recyclerInteract(String.valueOf(arrayList.get(position).getDate().getTime()) + " " + String.valueOf(arrayList.get(position).getDate().getMonth()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        LinearLayout frameLayout;
        TextView date, day;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            frameLayout = itemView.findViewById(R.id.framelayout);
        }
    }
}