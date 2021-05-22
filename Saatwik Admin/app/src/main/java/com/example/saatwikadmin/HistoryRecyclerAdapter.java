package com.example.saatwikadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saatwikadmin.Model.History;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder>{
    private Context context;
    private List<History> HistoryList;

    public HistoryRecyclerAdapter(Context context, List<History> HistoryList) {
        this.context = context;
        this.HistoryList = HistoryList;
    }

    @NonNull
    @Override
    public HistoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.history, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
     History history = HistoryList.get(position);
     FirebaseFirestore db = FirebaseFirestore.getInstance();
     CollectionReference collectionReference = db.collection("Registering users");

     holder.item.setText(history.getItem());
     holder.number.setText(history.getNumber());
     holder.time.setText(history.getTime());

     String userId = history.getUserPhone();

        collectionReference.document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                {
                    if (value.exists()) {
                        {
                            holder.name.setText(value.getString("Name"));
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return HistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView
                name, item, time, number;
        public Button ten;
        public DatabaseReference databaseReference1, databaseReference2, databaseReference3;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Special users");
            ten = itemView.findViewById(R.id.ten);
            ten.setOnClickListener(this);
           name = itemView.findViewById(R.id.history_name);
           item = itemView.findViewById(R.id.history_item);
           time = itemView.findViewById(R.id.history_time);
           number = itemView.findViewById(R.id.history_number);

        }

        @Override
        public void onClick(View v) {
            if(v == ten){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Coupon code");
                LinearLayout lila1= new LinearLayout(context);
                lila1.setOrientation(LinearLayout.VERTICAL);
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                final EditText input1 = new EditText(context);
                input.setHint("Coupon code");
                input1.setHint("Discount percent (Only in numbers)");
                input1.setInputType(InputType.TYPE_CLASS_NUMBER);
                lila1.addView(input);
                lila1.addView(input1);
                builder.setView(lila1);
                builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String code = input.getText().toString();
                        String percent = input1.getText().toString();
                        int position = getAdapterPosition();
                        History history = HistoryList.get(position);
                        databaseReference1.child(history.getUserPhone()).child("code").setValue(code);
                        databaseReference1.child(history.getUserPhone()).child("percent").setValue(percent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        }
    }
}



