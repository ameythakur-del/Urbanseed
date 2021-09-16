package ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.groceries.urabanseed.BuildConfig;
import com.groceries.urabanseed.MyCart;
import com.groceries.urabanseed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.CartItem;
import model.History;
import model.HistoryOrder;

import static android.content.ContentValues.TAG;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<History> histories;
    List<HistoryOrder> historyOrders;
    private static final int TRACK_ACTIVITY_REQUEST_CODE = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    ProgressDialog progressDialog;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public HistoryRecyclerAdapter(Context context, List<History> histories) {
        this.context = context;
        this.histories = histories;
    }

    @NonNull
    @Override
    public HistoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.historyorder, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerAdapter.ViewHolder viewHolder, int position) {
        final History history = histories.get(position);

        viewHolder.date.setText(history.getDate());

        viewHolder.recyclerView.setHasFixedSize(true);
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        viewHolder.databaseReference.child(history.getDate()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyOrders = new ArrayList<>();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    HistoryOrder historyOrder = snapshot1.getValue(HistoryOrder.class);
                    historyOrders.add(historyOrder);
                }
                HistoryOrderRecyclerAdapter historyOrderRecyclerAdapter = new HistoryOrderRecyclerAdapter(context, historyOrders);
                viewHolder.recyclerView.setAdapter(historyOrderRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return histories.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView date;
        DatabaseReference databaseReference;
        RecyclerView recyclerView;
        Button repeat, bill;
        DatabaseReference itemReference, cartReference;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            date = itemView.findViewById(R.id.date);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Order History").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
            recyclerView = itemView.findViewById(R.id.recyclerview);

            bill = itemView.findViewById(R.id.bill);
            bill.setOnClickListener(this);
            itemReference = FirebaseDatabase.getInstance().getReference().child("items");

            repeat = itemView.findViewById(R.id.repeat);
            repeat.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v == repeat) {
                History history = histories.get(getAdapterPosition());

                databaseReference.child(history.getDate()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        historyOrders = new ArrayList<>();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            HistoryOrder historyOrder = snapshot1.getValue(HistoryOrder.class);

                            itemReference.child(historyOrder.getItem()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if ((snapshot.child("available").exists() && snapshot.child("available").getValue().equals("False")) || snapshot.child("visibility").getValue().equals(false)) {

                                    } else {
                                        CartItem cartItem = new CartItem();
                                        cartItem.setItem(historyOrder.getItem());
                                        cartItem.setTaste(snapshot.child("taste").getValue().toString());
                                        Query query = itemReference.child(historyOrder.getItem()).child("Quantity").orderByChild("quantity").equalTo(historyOrder.getQuantity());

                                        query.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                                if (datasnapshot.exists()) {
                                                    for (DataSnapshot snapshot1 : datasnapshot.getChildren()) {
                                                        cartItem.setPrice(snapshot1.child("priced").getValue().toString());
                                                    }
                                                    cartItem.setPer(historyOrder.getQuantity());
                                                    if (snapshot.child("imageUrl").exists()) {
                                                        cartItem.setImageUrl(snapshot.child("imageUrl").getValue().toString());
                                                    }
                                                    cartItem.setCategory(snapshot.child("category").getValue().toString());
                                                    cartItem.setDelivery(snapshot.child("delivery").getValue().toString());
                                                    cartItem.setOriginal(snapshot.child("original").getValue().toString());
                                                    cartItem.setNumber(historyOrder.getNumber());
                                                    cartItem.setUserPhone(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString());
                                                    String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                                                    cartItem.setTime(currentDateTimeString);
                                                    cartReference = FirebaseDatabase.getInstance().getReference().child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString() + cartItem.getItem());
                                                    cartReference.setValue(cartItem);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                context.startActivity(new Intent(context, MyCart.class));
            }
            if(v == bill){
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Generating Bill");
                progressDialog.show();
                History history = histories.get(getAdapterPosition());
                int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            (Activity) context,
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                }
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                } catch (DocumentException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
        private void createPdfWrapper() throws FileNotFoundException, DocumentException {
            int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_CONTACTS)) {
                        showMessageOKCancel("You need to allow access to Storage",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    REQUEST_CODE_ASK_PERMISSIONS);
                                        }
                                    }
                                });
                        return;
                    }
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
                return;
            } else {
                createPdf();
            }
        }
        private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
            new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton("OK", okListener)
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        }
        private void createPdf() throws FileNotFoundException, DocumentException {
            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
            if (!docsFolder.exists()) {
                docsFolder.mkdir();
                Log.i(TAG, "Created a new directory for PDF");
            }
            String pdfname = "Invoice.pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
            OutputStream output = new FileOutputStream(pdfFile);
            com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
            PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3});
            table.getDefaultCell().setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            table.getDefaultCell().setFixedHeight(50);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            Paragraph p1 = new Paragraph("Item");
            p1.setAlignment(Element.ALIGN_CENTER);
            PdfPCell cell = new PdfPCell();
            cell.addElement(p1);
            cell.setFixedHeight(30);
            table.addCell(cell);

            p1 = new Paragraph("Quantity");
            p1.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell();
            cell.addElement(p1);
            cell.setFixedHeight(30);
            table.addCell(cell);

            p1 = new Paragraph("Number of items");
            p1.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell();
            cell.addElement(p1);
            cell.setFixedHeight(30);
            table.addCell(cell);

            p1 = new Paragraph("Price");
            p1.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell();
            cell.addElement(p1);
            cell.setFixedHeight(30);
            table.addCell(cell);

            table.setHeaderRows(1);
//            PdfPCell[] cells = table.getRow(0).getCells();
//            for (int j = 0; j < cells.length; j++) {
//                cells[j].setBackgroundColor(BaseColor.GRAY);
//            }

            History history = histories.get(getAdapterPosition());
            databaseReference.child(history.getDate()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    historyOrders = new ArrayList<>();
                    int total=0;
                    int charge = 0;
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        HistoryOrder historyOrder = snapshot1.getValue(HistoryOrder.class);
                        PdfPCell cell = new PdfPCell();
                        Font f=new Font(Font.FontFamily.UNDEFINED,14,Font.BOLD,BaseColor.BLACK);
                        Paragraph p1 = new Paragraph(historyOrder.getItem(), f);
                        p1.setAlignment(Element.ALIGN_CENTER);
                        cell.addElement(p1);
                        cell.setFixedHeight(30);// Control the fixed height of each cellyy
                        table.addCell(cell);

                        PdfPCell cell2 = new PdfPCell();
                        p1 = new Paragraph(historyOrder.getQuantity());
                        p1.setAlignment(Element.ALIGN_CENTER);
                        cell2.addElement(p1);
                        cell2.setFixedHeight(30);// Control the fixed height of each cell
                        table.addCell(cell2);

                        PdfPCell cell3 = new PdfPCell();
                        p1 = new Paragraph(historyOrder.getNumber());
                        p1.setAlignment(Element.ALIGN_CENTER);
                        cell3.addElement(p1);
                        cell3.setFixedHeight(30);// Control the fixed height of each cell
                        table.addCell(cell3);

                        PdfPCell cell4 = new PdfPCell();
                        p1 = new Paragraph(historyOrder.getPrice() + " " + "Rs");
                        p1.setAlignment(Element.ALIGN_CENTER);
                        cell4.addElement(p1);
                        cell4.setFixedHeight(30);// Control the fixed height of each cell
                        table.addCell(cell4);

                        float a= Float.parseFloat(historyOrder.getPrice());
                        float b = Float.parseFloat(historyOrder.getNumber());

                        total+=Float.parseFloat(String.valueOf(a*b));
                        charge = Integer.parseInt(historyOrder.getCharge());
                    }
                    total += charge;
                    Font f=new Font(Font.FontFamily.UNDEFINED,15.0f,Font.NORMAL,BaseColor.BLACK);
                    Paragraph p1 = new Paragraph("Delivery Charges : " + charge +" "+ "Rs", f);
                    p1.setAlignment(Element.ALIGN_RIGHT);
                    Font f2=new Font(Font.FontFamily.UNDEFINED,20.0f,Font.BOLD,BaseColor.BLACK);
                    Paragraph p2 = new Paragraph("Total : " + total +" "+ "Rs", f2);
                    p2.setAlignment(Element.ALIGN_RIGHT);
                    try {
                        PdfWriter.getInstance((com.itextpdf.text.Document) document, output);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }

                    document.open();
                    try {
                        Rectangle rectDoc = document.getPageSize();
                        float width = rectDoc.getWidth();
                        float height = rectDoc.getHeight();
                        float imageStartX = 25;
                        float imageStartY = height - 100;

                        System.gc();

                        Drawable d = context.getResources().getDrawable(R.drawable.transparent_logo);
                        BitmapDrawable bitDw = ((BitmapDrawable) d);
                        Bitmap bmp = bitDw.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();

                        bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                        byte[] byteArray = stream.toByteArray();
                        // PdfImage img = new PdfImage(arg0, arg1, arg2)

                        // Converting byte array into image Image img =
                        Image img = null; // img.scalePercent(50);
                        try {
                            img = Image.getInstance(byteArray);
                        } catch (IOException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                        img.setAlignment(Image.TEXTWRAP);
                        img.scaleAbsolute(100f, 100f);
                        img.setAbsolutePosition(imageStartX, imageStartY); // Adding Image
                        document.add(img);
                        Paragraph prSub= new Paragraph();
                        prSub.add("\n");
                        prSub.add("\n");
                        prSub.add("\n");
                        document.add(prSub);

                        // Start New Paragraph
//                        Paragraph prHead = new Paragraph();
//                        prHead.add("Android\n");
//                        document.add(prHead);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm aa");
                        String formattedDate = history.getDate().toString();



                        Paragraph prSubHead = new Paragraph();

                        prSubHead.add("Order time : " + formattedDate);
                        prSubHead.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                        prSubHead.add("\n");
                        prSubHead.add("\n");
                        prSubHead.add("\n");
                        document.add(prSubHead);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference collectionReference = db.collection("Registering users");
                        collectionReference.document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                {
                                    if (value.exists()) {
                                        {
                                            Paragraph prSub= new Paragraph();
                                            prSub.add("\n");
                                            prSub.add("Customer Details: " + '\n');
                                            prSub.add("Name: " + value.getString("Name") + '\n');
                                            prSub.add("Address: " + value.getString("Address") + '\n');
                                            prSub.add("Mobile No. : " + value.getId() + '\n');
                                            prSub.add("\n");
                                            prSub.add("\n");
                                            try {
                                                document.add(prSub);
                                                document.add(table);
                                                try {
                                                    document.add(p1);
                                                    document.add(p2);
                                                } catch (DocumentException e) {
                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                    e.printStackTrace();
                                                }
                                                Paragraph pro= new Paragraph();
                                                pro.add("\n");
                                                pro.add("Amount Paid" + '\n');
                                                document.add(pro);
                                                Paragraph pr= new Paragraph();
                                                pr.add("\n");
                                                pr.add("URBANSEED" + '\n');
                                                pr.add("Sawantwadi"+ '\n');
                                                pr.add("+91 9403768656"+ '\n');
                                                pr.add("info@urbanseed.com" + '\n');
                                                pr.setAlignment(Element.ALIGN_RIGHT);
                                                document.add(pr);
                                                progressDialog.dismiss();
                                            } catch (DocumentException e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();                                                e.printStackTrace();
                                            }
                                            document.close();
                                            previewPdf();
                                        }
                                    } else
                                        progressDialog.dismiss();
                                }
                            }
                        });
                    } catch (DocumentException e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        private void previewPdf() {
            PackageManager packageManager = context.getPackageManager();
            Intent testIntent = new Intent(Intent.ACTION_VIEW);
            testIntent.setType("application/pdf");
            List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", pdfFile);
                intent.setDataAndType(uri, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



