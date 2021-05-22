package com.example.saatwikadmin;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.saatwikadmin.Utils.Tag;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class TagImages extends Fragment {


    public TagImages(){

    }

    private static final int Image_Request_Code = 10;
    private Button upload, change, delete;
    private ImageButton imageButton;
    private ProgressDialog progressDialog;
    StorageReference storageReference;
    DatabaseReference databaseReference1;
    private Uri imageUri;
    private long countPosts = 0;
    private CheckBox snacks, masale, padartha;
    String category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tag_images, container, false);

        storageReference = FirebaseStorage.getInstance().getReference("Images");

        imageButton = (ImageButton) view.findViewById(R.id.action_image);
        upload = (Button) view.findViewById(R.id.image_upload);
        change = view.findViewById(R.id.change_tabs);
        progressDialog = new ProgressDialog(getActivity());
        delete = view.findViewById(R.id.delete_activity);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TagImageNavigation.class));
            }
        });

        snacks = (CheckBox) view.findViewById(R.id.snacks);
        masale = (CheckBox) view.findViewById(R.id.Masale);
        padartha = (CheckBox) view.findViewById(R.id.padartha);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Uploading");
                progressDialog.show();


                UploadImage();


            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChangeTabs.class));

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

        ContentResolver contentResolver = this.getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    public void UploadImage() {


        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Tag Images");
        if (imageUri != null) {

            final StorageReference filepath = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));

            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    if(snacks.isChecked()){
                                        category = "Snacks";
                                    }

                                    else if(masale.isChecked()){
                                        category = "masale";
                                    }

                                    else if (padartha.isChecked()){
                                        category = "padartha";
                                    }

                                    String imageUrl = uri.toString();
                                    final String Category = category;

                                    //Todo: create a Journal Object - model

                                    Tag tag = new Tag();
                                    tag.setImageUrl(imageUrl);
                                    String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                                    databaseReference1 = FirebaseDatabase.getInstance().getReference().child(Category).child(currentDateTimeString);
                                    databaseReference1.setValue(tag);
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();


                                    //Todo:invoke our collectionReference


                                }
                            });
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }
}