package com.example.petshop.dialog;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.petshop.R;
import com.example.petshop.callback.ProductsCallback;
import com.example.petshop.dao.DaoProducts;
import com.example.petshop.fragment.FragmentProducts;
import com.example.petshop.model.Products;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class BottomSheefUpdateProducts extends BottomSheetDialogFragment {
    EditText edt_namefood, edt_gia, edt_soluong, edt_diachi, edt_mota;
    Button btnaddimg, btnadd;
    TextView tvNewItem, tv_theloai;
    ImageView imghinhshow;
    DaoProducts daoProducts;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    String address, description, id, idP, idStore, image, nameP, tokenStore;
    int quantity;
    double price;
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idP = getArguments().getString("idP");
        }
        FirebaseApp.initializeApp(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheef_add_products, container, false);

        edt_namefood = view.findViewById(R.id.edt_namefood);
        edt_mota = view.findViewById(R.id.edt_mota);
        edt_gia = view.findViewById(R.id.edt_gia);
        edt_soluong = view.findViewById(R.id.edt_soluong);
        edt_diachi = view.findViewById(R.id.edt_diachi);
        tv_theloai = view.findViewById(R.id.tv_theloai);
        imghinhshow = view.findViewById(R.id.imghinhshow);
        btnadd = view.findViewById(R.id.btnaddproduct);
        btnaddimg = view.findViewById(R.id.btnaddimgproduct);
        tvNewItem = view.findViewById(R.id.tvNewItem);
        progressBar = view.findViewById(R.id.progressbaUpdaterProduct);
        tvNewItem.setText("UPDATE PRODUCT");
        btnadd.setText("Update Product");

        daoProducts = new DaoProducts(getActivity());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        daoProducts.getAll(new ProductsCallback() {
            @Override
            public void onSuccess(ArrayList<Products> lists) {
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getIdP().equalsIgnoreCase(idP)) {
                        address = lists.get(i).getAddress();
                        description = lists.get(i).getDescription();
                        id = lists.get(i).getId();
                        idP = lists.get(i).getIdP();
                        idStore = lists.get(i).getIdStore();
                        image = lists.get(i).getImage();
                        nameP = lists.get(i).getNameP();
                        price = lists.get(i).getPrice();
                        quantity = lists.get(i).getQuantity();
                        tokenStore = lists.get(i).getTokenStore();
                    }
                }

                edt_gia.setText(String.valueOf((int) price));
                edt_namefood.setText(nameP);
                edt_soluong.setText(String.valueOf((int) quantity));
                edt_diachi.setText(address);
                edt_mota.setText(description);
                tv_theloai.setText(id);
                if (image == null) {
                    Glide.with(getContext()).load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").into(imghinhshow);
                } else {
                    Glide.with(getContext()).load(image).into(imghinhshow);
                }
            }

            @Override
            public void onError(String message) {

            }
        });
        btnaddimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (filePath != null) {
                    updateProductWithImage();
                } else {
                    updateProductWithoutImage();
                }
            }
        });
        return view;
    }

    private void updateProductWithoutImage() {
        progressBar.setVisibility(View.GONE);
        String price = edt_gia.getText().toString();
        String nameP = edt_namefood.getText().toString();
        String quantity = edt_soluong.getText().toString();
        String address = edt_diachi.getText().toString();
        String des = edt_mota.getText().toString();
        Products categories = new Products();
        categories.setAddress(address);
        categories.setDescription(des);
        categories.setId(id);
        categories.setIdP(idP);
        categories.setIdStore(idStore);
        categories.setImage(image);
        categories.setNameP(nameP);
        categories.setPrice(Double.parseDouble(price));
        categories.setQuantity(Integer.parseInt(quantity));
        categories.setTokenStore(tokenStore);
        daoProducts = new DaoProducts(getContext());
        daoProducts.update(categories);
        onProductUpdated();
    }

    private void updateProductWithImage() {
        String price = edt_gia.getText().toString();
        String nameP = edt_namefood.getText().toString();
        String quantity = edt_soluong.getText().toString();
        String address = edt_diachi.getText().toString();
        String des = edt_mota.getText().toString();
        final StorageReference imageFolder = storageReference.child("Products/" + UUID.randomUUID().toString());
        imageFolder.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressBar.setVisibility(View.GONE);
                        Products categories = new Products();
                        categories.setAddress(address);
                        categories.setDescription(des);
                        categories.setId(id);
                        categories.setIdP(idP);
                        categories.setIdStore(idStore);
                        categories.setImage(uri.toString());
                        categories.setNameP(nameP);
                        categories.setPrice(Double.parseDouble(price));
                        categories.setQuantity(Integer.parseInt(quantity));
                        categories.setTokenStore(tokenStore);
                        daoProducts = new DaoProducts(getContext());
                        daoProducts.update(categories);
                        onProductUpdated();
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

    private void onProductUpdated() {
        dismiss();
        Glide.with(this).clear(imghinhshow);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(FragmentProducts.class.getSimpleName());
        if (fragment instanceof FragmentProducts) {
            FragmentProducts fragmentCategory = (FragmentProducts) fragment;
            fragmentCategory.updateProductList(id);
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
                imghinhshow.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}
