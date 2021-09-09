package com.example.saatwikadmin;

//test
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.saatwikadmin.Model.Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.lang.reflect.Member;

import static android.app.Activity.RESULT_OK;

public class AddItems extends Fragment {
    private static final int Image_Request_Code = 7;
    // DatabaseReference databaseReference,databaseReferenceA, databaseReference1;
    DatabaseReference databaseReference, databaseReference1;
    StorageReference storageReference;
    String category, per, subcategory;

    EditText name, taste, price, delivery, quantity1, price1, quantity2, price2, quantity3, price3, quantity4, price4, discount, price5, price6, price7, price8;
    Boolean visibility;
    FirebaseDatabase database;
    Member member;
    CheckBox snacks, masale, other, homemade, vegetables, stationary, soaps, fish, chicken, dairy, spices, flours, gift, personal, juices, detergents, edible, beverages;

    private ImageButton imageButton;

    private Button button, update;
    Switch swit;

    private static final int GALLERY_CODE = 1;

    private long countPosts = 0;

    private ProgressDialog progressDialog;

    private Uri imageUri;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    String Sales, Marketing;

    public AddItems() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_items, container, false);

        discount = view.findViewById(R.id.original_price);
        quantity1 = view.findViewById(R.id.quantity1);
        price1 = view.findViewById(R.id.price1);
        quantity2 = view.findViewById(R.id.quantity2);
        price2 = view.findViewById(R.id.price2);
        quantity3 = view.findViewById(R.id.quantity3);
        price3 = view.findViewById(R.id.price3);
        quantity4 = view.findViewById(R.id.quantity4);
        price4 = view.findViewById(R.id.price4);
        price5 = view.findViewById(R.id.price5);
        price6 = view.findViewById(R.id.price6);
        price7 = view.findViewById(R.id.price7);
        price8 = view.findViewById(R.id.price8);
        snacks = view.findViewById(R.id.checkBox);
        masale = view.findViewById(R.id.checkBox2);
        other = view.findViewById(R.id.checkBox3);
        homemade = view.findViewById(R.id.checkBox4);
        vegetables = view.findViewById(R.id.checkBox5);
        stationary = view.findViewById(R.id.checkBox6);
        soaps = view.findViewById(R.id.checkBox7);
        fish = view.findViewById(R.id.checkBox8);
        chicken = view.findViewById(R.id.checkBox9);
        dairy = view.findViewById(R.id.checkBox10);
        spices = view.findViewById(R.id.checkBox11);
        flours = view.findViewById(R.id.checkBox12);
        gift = view.findViewById(R.id.checkBox13);
        personal = view.findViewById(R.id.checkBox14);
        juices = view.findViewById(R.id.checkBox15);
        detergents = view.findViewById(R.id.checkBox16);
        edible = view.findViewById(R.id.checkBox17);
        beverages = view.findViewById(R.id.checkBox18);
        progressDialog = new ProgressDialog(getActivity());
        name = view.findViewById(R.id.edit_name);
        imageButton = view.findViewById(R.id.upload_image);
        taste = view.findViewById(R.id.edit_taste);
        price = view.findViewById(R.id.edit_price);
        delivery = view.findViewById(R.id.edit_delivery);
        button = view.findViewById(R.id.add_item);
        update = view.findViewById(R.id.update_item);
        swit = view.findViewById(R.id.visibility);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateActivity.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();



        storageReference = FirebaseStorage.getInstance().getReference("Images");


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Uploading");
                progressDialog.show();


                UploadImage();


            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), imageUri);
                imageButton.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    public void UploadImage() {

        category = "Snacks";
        if(snacks.isChecked()){
            category = "Grain and Pulses";
        }
        else if(masale.isChecked()){
            category = "Biscuits";
        }
        else if (other.isChecked()){
            category = "Kitchen Essentials";
        }
        else if(homemade.isChecked()){
            category = "Homemade Food";
        }
        else if (vegetables.isChecked()){
            category = "Vegetables and Fruits";
        }
        else if(stationary.isChecked()){
            category = "Staionary";
        }
        else if (soaps.isChecked()){
            category = "Soaps";
        }
        else if(fish.isChecked()){
            category = "Fish";
        }
        else if (chicken.isChecked()){
            category = "Chicken and Eggs";
        }
        else if (dairy.isChecked()){
            category = "Dairy Products";
        }
        else if (spices.isChecked()){
            category = "Spices";
        }
        else if (flours.isChecked()){
            category = "Flours";
        }
        else if (gift.isChecked()){
            category = "Gift Items";
        }
        else if (personal.isChecked()){
            category = "Personal Care";
        }
        else if (juices.isChecked()){
            category = "Juices";
        }
        else if (detergents.isChecked()){
            category = "Detergents";
        }
        else if (edible.isChecked()){
            category = "Edible Oil";
        }
        else if (beverages.isChecked()){
            category = "Beverages";
        }

        subcategory = "";

        final String Name = name.getText().toString().trim();
        final String Taste = taste.getText().toString().trim();
        final String Price = price.getText().toString().trim();
        final String Delivery = delivery.getText().toString().trim();
        final String Category = category.toString();
        final String Subcategory = subcategory.toString();
        final String Quantity1 = quantity1.getText().toString().trim();
        final String Price1 = price1.getText().toString().trim();
        final String Quantity2 = quantity2.getText().toString().trim();
        final String Price2 = price2.getText().toString().trim();
        final String Quantity3 = quantity3.getText().toString().trim();
        final String Price3 = price3.getText().toString().trim();
        final String Quantity4 = quantity4.getText().toString().trim();
        final String Price4 = price4.getText().toString().trim();
        final String Price5 = price5.getText().toString().trim();
        final String Price6 = price6.getText().toString().trim();
        final String Price7 = price7.getText().toString().trim();
        final String Price8 = price8.getText().toString().trim();
        final String Original = discount.getText().toString().trim();
        if(swit.isChecked()){
            visibility = true;
        }
        else{
            visibility = false;
        }

        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        if (!TextUtils.isEmpty(Name) &&
                !TextUtils.isEmpty(Taste)
                && !TextUtils.isEmpty(Price)
                && !TextUtils.isEmpty(Original)
                && !TextUtils.isEmpty(Delivery)
                && !TextUtils.isEmpty(Quantity1)) {

            if (imageUri != null) {
                final StorageReference filepath = storageReference.child(Name);
                databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        countPosts = dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                filepath.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String imageUrl = uri.toString();
                                        //Todo: create a Journal Object - model
                                        Item item = new Item(Name.toString(), Taste.toString(), Price.toString(), Delivery.toString(), imageUrl.toString(), Category.toString(), Original.toString(), Subcategory.toString(), visibility);
                                        item.setItem(Name);
                                        item.setTaste(Taste);
                                        item.setPrice(Price);
                                        item.setDelivery(Delivery);
                                        item.setImageUrl(imageUrl);
                                        item.setCategory(Category);
                                        item.setSubcategory(Subcategory);
                                        item.setOriginal(Original);
                                        item.setVisibility(visibility);
//                                    item.setCountPosts(countPosts);
                                        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("items").child(Name);
                                        databaseReference1.setValue(item);
                                        if(!Quantity1.isEmpty()) {
                                            Item item1 = new Item(Quantity1.toString(), Price1.toString());
                                            item1.setQuantity(Quantity1);
                                            item1.setPriced(Price1);
                                            item1.setMrp(Price5);
                                            databaseReference1.child("Quantity").child("Quantity1").setValue(item1);
                                        }
                                        if(!Quantity2.isEmpty()) {
                                            Item item1 = new Item(Quantity2.toString(), Price2.toString());
                                            item1.setQuantity(Quantity2);
                                            item1.setPriced(Price2);
                                            item1.setMrp(Price6);
                                            databaseReference1.child("Quantity").child("Quantity2").setValue(item1);
                                        }
                                        if(!Quantity3.isEmpty()) {
                                            Item item1 = new Item(Quantity3.toString(), Price3.toString());
                                            item1.setQuantity(Quantity3);
                                            item1.setPriced(Price3);
                                            item1.setMrp(Price7);
                                            databaseReference1.child("Quantity").child("Quantity3").setValue(item1);
                                        }
                                        if(!Quantity4.isEmpty()) {
                                            Item item1 = new Item(Quantity4.toString(), Price4.toString());
                                            item1.setQuantity(Quantity4);
                                            item1.setPriced(Price4);
                                            item1.setMrp(Price8);
                                            databaseReference1.child("Quantity").child("Quantity4").setValue(item1);
                                        }
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();


                                        //Todo:invoke our collectionReference
                                    }
                                });


                            }
                        });
            }
            else {
                final StorageReference filepath = null;
                //Todo: create a Journal Object - model
                Item item = new Item(Name.toString(), Taste.toString(), Price.toString(), Delivery.toString(), Category.toString(), Original.toString(), Subcategory.toString(), visibility);
                item.setItem(Name);
                item.setTaste(Taste);
                item.setPrice(Price);
                item.setDelivery(Delivery);
                item.setCategory(Category);
                item.setSubcategory(Subcategory);
                item.setOriginal(Original);
                item.setVisibility(visibility);
                String a = Name.trim();
                databaseReference1 = FirebaseDatabase.getInstance().getReference().child("items").child(a);
                databaseReference1.setValue(item);
                if(!Quantity1.isEmpty()) {
                    Item item1 = new Item(Quantity1.toString(), Price1.toString());
                    item1.setQuantity(Quantity1);
                    item1.setPriced(Price1);
                    item1.setMrp(Price5);
                    databaseReference1.child("Quantity").child("Quantity1").setValue(item1);
                }
                if(!Quantity2.isEmpty()) {
                    Item item1 = new Item(Quantity2.toString(), Price2.toString());
                    item1.setQuantity(Quantity2);
                    item1.setPriced(Price2);
                    item1.setMrp(Price6);
                    databaseReference1.child("Quantity").child("Quantity2").setValue(item1);
                }
                if(!Quantity3.isEmpty()) {
                    Item item1 = new Item(Quantity3.toString(), Price3.toString());
                    item1.setQuantity(Quantity3);
                    item1.setPriced(Price3);
                    item1.setMrp(Price7);
                    databaseReference1.child("Quantity").child("Quantity3").setValue(item1);
                }
                if(!Quantity4.isEmpty()) {
                    Item item1 = new Item(Quantity4.toString(), Price4.toString());
                    item1.setQuantity(Quantity4);
                    item1.setPriced(Price4);
                    item1.setMrp(Price8);
                    databaseReference1.child("Quantity").child("Quantity4").setValue(item1);
                }
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
            }


        } else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Please enter all the details", Toast.LENGTH_LONG).show();
        }
    }
}