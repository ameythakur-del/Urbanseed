package com.example.saatwikadmin;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saatwikadmin.Model.Model;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;

public class NotServing extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Model> totalDates = new ArrayList<>();
    Button stop, start, date;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stop");
    DatabaseReference offReference = FirebaseDatabase.getInstance().getReference().child("Off Day");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_serving);

        stop = findViewById(R.id.stop_serving);
        start = findViewById(R.id.start_serving);
        date = findViewById(R.id.date_button);
        stop.setOnClickListener(this);
        start.setOnClickListener(this);
        date.setOnClickListener(this);

        LocalDate today = LocalDate.now();
        LocalDate firstDayOfTheMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfTheMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate squareCalendarMonthDayStart = today;
        LocalDate squareCalendarMonthDayEnd = today.plusDays(29);
        int i = 0;
        while (!squareCalendarMonthDayStart.isAfter(squareCalendarMonthDayEnd)) {
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Model model = new Model();
            if(i==0){
                model = new Model(Date.from(squareCalendarMonthDayStart.atStartOfDay(defaultZoneId).toInstant()), true);
            }
            else {
                model = new Model(Date.from(squareCalendarMonthDayStart.atStartOfDay(defaultZoneId).toInstant()), false);
            }
            totalDates.add(model);
            squareCalendarMonthDayStart = squareCalendarMonthDayStart.plusDays(1);
            i++;
        }

        RecyclerView recyclerView2 = findViewById(R.id.dates);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setHasFixedSize(true);
        CalendatDatesRecyclerAdapter calendatDatesRecyclerAdapter = new CalendatDatesRecyclerAdapter(totalDates, this);
        recyclerView2.setAdapter(calendatDatesRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == stop){
            reference.setValue("True");
            Toast.makeText(NotServing.this, "Done", Toast.LENGTH_LONG).show();
        }
        if (v == start){
            reference.setValue("False");
            Toast.makeText(NotServing.this, "Done", Toast.LENGTH_LONG).show();
        }
        if (v == date){
            for(int i=0; i<totalDates.size(); i++){
                if(totalDates.get(i).getaBoolean()){
                    offReference.child(totalDates.get(i).getDate().toString()).setValue(totalDates.get(i).getDate().toString());
                    break;
                }
            }
        }
    }
}
