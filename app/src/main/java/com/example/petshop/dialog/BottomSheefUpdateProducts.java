package com.example.petshop.dialog;

import static android.app.Activity.RESULT_OK;

import static com.example.petshop.activity.MainActivity.idstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.petshop.R;
import com.example.petshop.callback.CategoriesCallback;
import com.example.petshop.dao.DaoCategories;
import com.example.petshop.dao.DaoProducts;
import com.example.petshop.model.Categories;
import com.example.petshop.model.Products;
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
import java.util.UUID;

public class BottomSheefUpdateProducts extends BottomSheetDialogFragment {
    EditText edt_idfood,edt_namefood,edt_gia,edt_soluong,edt_diachi,edt_mota;
    Button btnaddimg, btnadd;
    ImageView imghinhshow;
    ArrayList<Products> foodArrayList;
    ArrayList<Categories> categoriesArrayList = new ArrayList<>();
    DaoCategories databaseCategories;
    DaoProducts databaseFood;
    Spinner sp_status,sp_theloai;
    ArrayAdapter<Categories> categoriesArrayAdapter;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    String matl="";
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheef_update_products,container,false);
        edt_idfood = view.findViewById(R.id.edt_idfood);
        edt_namefood = view.findViewById(R.id.edt_namefood);
        edt_mota = view.findViewById(R.id.edt_mota);
        edt_gia = view.findViewById(R.id.edt_gia);
        edt_soluong = view.findViewById(R.id.edt_soluong);
        sp_status = view.findViewById(R.id.sp_status);
        edt_diachi = view.findViewById(R.id.edt_diachi);
        sp_theloai = view.findViewById(R.id.sp_theloai);
        imghinhshow = view.findViewById(R.id.imghinhshow);
        btnaddimg = view.findViewById(R.id.btnaddhinh);
        btnadd = view.findViewById(R.id.btnadd);
        databaseFood = new DaoProducts(getActivity());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        Bundle getdata = getArguments();
        edt_idfood.setText(getdata.getString("idfood"));
        edt_gia.setText(getdata.getString("gia"));
        edt_soluong.setText(getdata.getString("soluong"));
        edt_diachi.setText(getdata.getString("diachi"));
        edt_namefood.setText(getdata.getString("namefood"));
        edt_mota.setText(getdata.getString("mota"));


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_status.setAdapter(adapter);
        String buoi = getdata.getString("status");
        String matl1 = getdata.getString("matl");
        selectSpinnerValue(sp_status, buoi);

        showadaptertl();
        categoriesArrayList = new ArrayList<>();
        databaseCategories = new DaoCategories(getActivity());

        sp_theloai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                matl = categoriesArrayList.get(sp_theloai.getSelectedItemPosition()).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        databaseCategories.getAll(new CategoriesCallback() {
            @Override
            public void onSuccess(ArrayList<Categories> lists) {
                categoriesArrayList.clear();
                categoriesArrayList.addAll(lists);

                for (int i =0;i<categoriesArrayList.size();i++){
                    if (categoriesArrayList.get(i).getId().equalsIgnoreCase(matl1)){
                        sp_theloai.setSelection(i);
                        break;
                    }
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
                Insertmodel();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getActivity());
    }

    private void Insertmodel() {
        if(filePath!=null){

            final StorageReference imageFolder = storageReference.child("Food/"+ UUID.randomUUID().toString());
            imageFolder.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Products theLoai = new Products();
                            String status = sp_status.getSelectedItem().toString();
                            theLoai.setIdP(edt_idfood.getText().toString());
                            theLoai.setNameP(edt_namefood.getText().toString());
                            theLoai.setPrice(Double.parseDouble(edt_gia.getText().toString()));
                            theLoai.setQuantity(Integer.parseInt(edt_soluong.getText().toString()));
                            theLoai.setAddress(edt_diachi.getText().toString());
                            theLoai.setDescription(edt_mota.getText().toString());
                            theLoai.setStatus(status);
                            theLoai.setId(matl);
                            theLoai.setIdStore(idstore);
                            theLoai.setImage(uri.toString());
                            theLoai.setTokenStore(mAuth.getUid());
                            databaseFood = new DaoProducts(getActivity());
                            databaseFood.update(theLoai);

                            dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT ).show();
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
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
    public void showadaptertl(){
        categoriesArrayList = new ArrayList<>();
        databaseCategories = new DaoCategories(getActivity());

        databaseCategories.getAll(new CategoriesCallback() {
            @Override
            public void onSuccess(ArrayList<Categories> lists) {
                categoriesArrayList.clear();
                categoriesArrayList.addAll(lists);
                categoriesArrayAdapter = new ArrayAdapter<Categories>(getActivity(), android.R.layout.simple_spinner_item,categoriesArrayList);
                categoriesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_theloai.setAdapter(categoriesArrayAdapter);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void selectSpinnerValue(Spinner spinner, String myString)
    {
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(myString)){
                spinner.setSelection(i);
                break;
            }
        }
    }
}