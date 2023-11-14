package com.example.petshop.fragment;

import static android.app.Activity.RESULT_OK;
import static com.example.petshop.activity.MainActivity.bnv;
import static com.example.petshop.activity.MainActivity.toolbar;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.petshop.Helper.ValidationHelper;
import com.example.petshop.R;
import com.example.petshop.callback.StoreCallback;
import com.example.petshop.dao.DaoStore;
import com.example.petshop.model.Store;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentEditProfile extends Fragment {
    public static ImageView back;
    DaoStore daoUser;
    EditText edtname, edtphone, edtmail, edtaddress;
    Button btnupdateprofile;
    CircleImageView imgprofile;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    String pass;

    FirebaseUser firebaseUser;
    String mail, name, phone, diachi, anh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        bnv.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        back = view.findViewById(R.id.back);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fr_l, new FragmentProfile()).commit();
                bnv.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
            }
        });
        edtname = view.findViewById(R.id.profilename);
        edtphone = view.findViewById(R.id.profilephone);
        edtmail = view.findViewById(R.id.profilemail);
        edtaddress = view.findViewById(R.id.profileaddress);
        imgprofile = view.findViewById(R.id.imgprofile);
        btnupdateprofile = view.findViewById(R.id.updateprofile);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        daoUser = new DaoStore(getContext());
        daoUser.getAll(new StoreCallback() {
            @Override
            public void onSuccess(ArrayList<Store> lists) {
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getTokenStore().equalsIgnoreCase(firebaseUser.getUid())) {
                        name = lists.get(i).getName();
                        diachi = lists.get(i).getAddress();
                        mail = lists.get(i).getEmail();
                        phone = lists.get(i).getPhone();
                        pass = lists.get(i).getPass();
                        anh = lists.get(i).getImage();
                    }
                }

                edtaddress.setText(diachi);
                edtmail.setText(mail);
                edtname.setText(name);
                edtphone.setText(phone);
                if (anh == null) {
                    Glide.with(getContext()).load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").into(imgprofile);
                } else if (anh != null) {
                    Glide.with(getContext()).load(anh).into(imgprofile);
                }
            }

            @Override
            public void onError(String message) {

            }
        });

        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        btnupdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
                    updateProfileWithImage();
                } else {
                    updateProfileWithoutImage();
                }
            }
        });

        return view;
    }

    private void updateProfileWithImage() {
        String email = edtmail.getText().toString().trim();
        String phone = edtphone.getText().toString().trim();
        String ten = edtname.getText().toString().trim();
        String diachi = edtaddress.getText().toString().trim();

        if (isInputValid(email, phone, ten, diachi)) {
            change();
        }
    }

    private void updateProfileWithoutImage() {
        String email = edtmail.getText().toString().trim();
        String phone = edtphone.getText().toString().trim();
        String ten = edtname.getText().toString().trim();
        String diachi = edtaddress.getText().toString().trim();

        if (isInputValid(email, phone, ten, diachi)) {
            Store store = new Store();
            store.setEmail(email);
            store.setName(ten);
            store.setPhone(phone);
            store.setAddress(diachi);
            store.setPass(pass);
            store.setImage(anh);
            store.setTokenStore(firebaseUser.getUid());
            daoUser = new DaoStore(getContext());
            daoUser.update(store);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fr_l, new FragmentProfile()).commit();
            bnv.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    private boolean isInputValid(String email, String phone, String ten, String diachi) {
        if (!ValidationHelper.isNotEmpty(email) || !ValidationHelper.isNotEmpty(phone) || !ValidationHelper.isNotEmpty(ten) || !ValidationHelper.isNotEmpty(diachi)) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ValidationHelper.isValidEmail(email)) {
            Toast.makeText(getActivity(), "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ValidationHelper.isValidPhoneNumber(phone)) {
            Toast.makeText(getActivity(), "Vui lòng nhập đúng số điện thoại!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void change() {

        final StorageReference imageFolder = storageReference.child("Store/" + UUID.randomUUID().toString());
        imageFolder.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Store store = new Store();
                        store.setEmail(edtmail.getText().toString());
                        store.setName(edtname.getText().toString());
                        store.setPhone(edtphone.getText().toString());
                        store.setAddress(edtaddress.getText().toString());
                        store.setImage(uri.toString());
                        store.setPass(pass);
                        store.setTokenStore(firebaseUser.getUid());
                        daoUser = new DaoStore(getContext());
                        daoUser.update(store);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fr_l, new FragmentProfile()).commit();
                        bnv.setVisibility(View.VISIBLE);
                        toolbar.setVisibility(View.VISIBLE);
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
                imgprofile.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}
