package com.example.petshop.dialog;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.petshop.R;
import com.example.petshop.dao.DaoCategories;
import com.example.petshop.dao.DaoProducts;
import com.example.petshop.model.Categories;
import com.example.petshop.model.Products;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class BottomSheefAddCategory extends BottomSheetDialogFragment {

    EditText edt_idcategory, edt_namecategory, edt_mota;
    Button btnaddimg, btnaddcategory;
    ImageView imghinhshow;
    DaoCategories databaseCategories;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheef_add_category, container, false);
        edt_idcategory = view.findViewById(R.id.edt_id_category);
        edt_namecategory = view.findViewById(R.id.edt_namecategory);
        edt_mota = view.findViewById(R.id.edt_motacategory);
        btnaddimg = view.findViewById(R.id.btnaddhinhcategory);
        imghinhshow = view.findViewById(R.id.imghinhshowCategory);
        btnaddcategory = view.findViewById(R.id.btnaddCategory);

        databaseCategories = new DaoCategories(getActivity());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        btnaddimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        btnaddcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertModel();
            }
        });

        return view;
    }

    private void InsertModel() {
        if (filePath != null) {
            final StorageReference imageFolder = storageReference.child("Categories/" + UUID.randomUUID().toString());
            imageFolder.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Categories theLoai = new Categories();
                            theLoai.setId(edt_idcategory.getText().toString());
                            theLoai.setName(edt_namecategory.getText().toString());
                            theLoai.setMoTa(edt_mota.getText().toString());
                            theLoai.setTrangthai(true);
                            theLoai.setImage(uri.toString());
                            databaseCategories = new DaoCategories(getActivity());
                            databaseCategories.insert(theLoai);
                            dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContext().getContentResolver(),
                                filePath);
                imghinhshow.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}