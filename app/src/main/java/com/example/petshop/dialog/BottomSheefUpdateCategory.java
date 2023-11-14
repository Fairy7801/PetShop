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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.petshop.R;
import com.example.petshop.callback.CategoriesCallback;
import com.example.petshop.dao.DaoCategories;
import com.example.petshop.fragment.FragmentCategory;
import com.example.petshop.model.Categories;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class BottomSheefUpdateCategory extends BottomSheetDialogFragment {
    EditText edt_idcategory, edt_namecategory, edt_mota;
    TextView tvNewItem;
    Button btnaddimg, btnaddcategory;
    ImageView imghinhshow;
    DaoCategories daoCategories;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    ArrayList<Categories> categoriesArrayList;
    String nameCategory, idC,  motaCategory, imageCategory, tokenCategory;
    Boolean trangThaiCategory;
    ProgressBar progressBar;

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
        progressBar = view.findViewById(R.id.progressbaUpdaterCategory);
        tvNewItem = view.findViewById(R.id.tvNewItem);
        tvNewItem.setText("Update Category");
        btnaddcategory.setText("Update Category");
        categoriesArrayList = new ArrayList<>();
        daoCategories = new DaoCategories(getActivity());

        Bundle args = getArguments();

        if (args != null) {
            String idCategory = args.getString("idCategory");
            daoCategories.getAll(new CategoriesCallback() {
                @Override
                public void onSuccess(ArrayList<Categories> lists) {
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).getId().equalsIgnoreCase(idCategory)) {
                            idC = lists.get(i).getId();
                            nameCategory = lists.get(i).getName();
                            motaCategory = lists.get(i).getMoTa();
                            imageCategory = lists.get(i).getImage();
                            trangThaiCategory = lists.get(i).getTrangthai();
                            tokenCategory = lists.get(i).getToken();
                        }
                    }

                    edt_idcategory.setText(idC);
                    edt_namecategory.setText(nameCategory);
                    edt_mota.setText(motaCategory);
                    if (imageCategory == null) {
                        Picasso.get().load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").into(imghinhshow);
                    } else {
                        Picasso.get().load(imageCategory).into(imghinhshow);
                    }
                }

                @Override
                public void onError(String message) {

                }
            });
        }

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
                progressBar.setVisibility(View.VISIBLE);
                if (filePath != null) {
                    updateCategoryWithImage();
                } else {
                    updateCategoryWithoutImage();
                }
            }
        });
        return view;
    }

    private void updateCategoryWithImage() {
        String id = edt_idcategory.getText().toString();
        String name = edt_namecategory.getText().toString();
        String moTa = edt_mota.getText().toString();
        final StorageReference imageFolder = storageReference.child("Categories/" + UUID.randomUUID().toString());
        imageFolder.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressBar.setVisibility(View.GONE);
                        Categories categories = new Categories();
                        categories.setId(id);
                        categories.setName(name);
                        categories.setMoTa(moTa);
                        categories.setImage(uri.toString());
                        categories.setTrangthai(trangThaiCategory);
                        categories.setToken(tokenCategory);
                        daoCategories = new DaoCategories(getContext());
                        daoCategories.update(categories);
                        onCategoryUpdated();
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

    private void updateCategoryWithoutImage() {
        progressBar.setVisibility(View.GONE);
        String id = edt_idcategory.getText().toString();
        String name = edt_namecategory.getText().toString();
        String moTa = edt_mota.getText().toString();
        Categories categories = new Categories();
        categories.setId(id);
        categories.setName(name);
        categories.setMoTa(moTa);
        categories.setImage(imageCategory);
        categories.setTrangthai(trangThaiCategory);
        categories.setToken(tokenCategory);
        daoCategories = new DaoCategories(getContext());
        daoCategories.update(categories);
        onCategoryUpdated();
    }

    private void onCategoryUpdated() {
        dismiss();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(FragmentCategory.class.getSimpleName());
        if (fragment instanceof FragmentCategory) {
            FragmentCategory fragmentCategory = (FragmentCategory) fragment;
            fragmentCategory.updateCategoryList();
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