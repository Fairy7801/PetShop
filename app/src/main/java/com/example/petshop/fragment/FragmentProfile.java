package com.example.petshop.fragment;

import static com.example.petshop.activity.MainActivity.toolbar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshop.R;
import com.example.petshop.activity.DangNhapActivity;
import com.example.petshop.activity.GiaoDichActivity;
import com.example.petshop.callback.StoreCallback;
import com.example.petshop.dao.DaoStore;
import com.example.petshop.model.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfile extends Fragment {

    CircleImageView profileCircleImageView;
    TextView usernameTextView, email, logout, history, txteditprofile, txtchangepassword, listorder;
    FirebaseUser firebaseUser;
    DaoStore daoStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileCircleImageView = view.findViewById(R.id.profileCircleImageView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        email = view.findViewById(R.id.email);
        toolbar.setVisibility(View.VISIBLE);
        logout = view.findViewById(R.id.logout);
        txtchangepassword = view.findViewById(R.id.txtchangepassword);
        txteditprofile = view.findViewById(R.id.txteditprofile);
        history = view.findViewById(R.id.history);
        listorder = view.findViewById(R.id.listorder);
        daoStore = new DaoStore(getActivity());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        daoStore.getAll(new StoreCallback() {
            @Override
            public void onSuccess(ArrayList<Store> lists) {
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getTokenStore().equalsIgnoreCase(firebaseUser.getUid())) {
                        email.setText(lists.get(i).getEmail());
                        usernameTextView.setText(lists.get(i).getName());
                        Picasso.get()
                                .load(lists.get(i).getImage()).into(profileCircleImageView);

                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fr_l, new FragmentHistory()).commit();
            }
        });
        txteditprofile.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fr_l, new FragmentEditProfile()).commit());
        txtchangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fr_l, new FragmentChangePassword()).commit();
            }
        });

        listorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GiaoDichActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "See you later", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), DangNhapActivity.class));
            }
        });
        return view;
    }
}
