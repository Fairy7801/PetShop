package com.example.petshop.dialog;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshop.R;
import com.example.petshop.callback.CategoriesCallback;
import com.example.petshop.callback.StoreCallback;
import com.example.petshop.dao.DaoCategories;
import com.example.petshop.dao.DaoProducts;
import com.example.petshop.dao.DaoStore;
import com.example.petshop.databinding.FragmentBottomSheefAddProductsBinding;
import com.example.petshop.model.Categories;
import com.example.petshop.model.Products;
import com.example.petshop.model.Store;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class BottomSheefAddProducts extends BottomSheetDialogFragment {
    private FragmentBottomSheefAddProductsBinding binding;
    DaoProducts databaseFood;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    String idP;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomSheefAddProductsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        databaseFood = new DaoProducts(getActivity());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        binding.tvNewItem.setText("ADD PRODUCT");
        binding.tvTheloai.setText(idP);

        // Khởi tạo đối tượng SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("store_info", Context.MODE_PRIVATE);
        binding.btnaddimgproduct.setOnClickListener(v -> SelectImage());
        binding.btnaddproduct.setOnClickListener(v -> InsertModel(sharedPreferences.getString("nameStore", ""), sharedPreferences.getString("tokenStore", "")));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idP = getArguments().getString("idCategory");
        }
        FirebaseApp.initializeApp(requireActivity());
    }

    private void InsertModel(String name, String token) {
        if (filePath != null) {
            final StorageReference imageFolder = storageReference.child("Product/" + UUID.randomUUID().toString());
            imageFolder.putFile(filePath).addOnSuccessListener(taskSnapshot ->
                    imageFolder.getDownloadUrl().addOnSuccessListener(uri -> {
                        Products theLoai = new Products();
                        theLoai.setNameP(binding.edtNamefood.getText().toString());
                        theLoai.setPrice(Double.parseDouble(binding.edtGia.getText().toString()));
                        theLoai.setQuantity(Integer.parseInt(binding.edtSoluong.getText().toString()));
                        theLoai.setAddress(binding.edtDiachi.getText().toString());
                        theLoai.setDescription(binding.edtMota.getText().toString());
                        theLoai.setId(getArguments().getString("idCategory"));
                        theLoai.setIdStore(name);
                        theLoai.setImage(uri.toString());
                        theLoai.setTokenStore(token);
                        databaseFood = new DaoProducts(requireActivity());
                        databaseFood.insert(theLoai);
                        dismiss();
                    }).addOnFailureListener(e -> Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show()));
        } else {
            Toast.makeText(getActivity(), "Lam on chon anh san pham", Toast.LENGTH_SHORT).show();
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
            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContext().getContentResolver(),
                                filePath);
                binding.imghinhshow.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
