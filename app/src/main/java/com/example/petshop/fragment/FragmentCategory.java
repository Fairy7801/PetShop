package com.example.petshop.fragment;

import static com.example.petshop.activity.MainActivity.idstore;
import static com.example.petshop.activity.MainActivity.toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petshop.R;
import com.example.petshop.adapter.ProductAdapter;
import com.example.petshop.callback.ProductsCallback;
import com.example.petshop.dao.DaoProducts;
import com.example.petshop.dialog.BottomSheefAddProducts;
import com.example.petshop.dialog.BottomSheefUpdateProducts;
import com.example.petshop.model.Products;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class FragmentCategory extends Fragment {
    public static RecyclerView rcvcategory;
    FloatingActionButton floatbtnthem;
    ArrayList<Products> foodArrayList;
    DaoProducts daoFood;
    ProductAdapter foodAdapter;
    FirebaseStorage storage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category,container,false);
        rcvcategory = view.findViewById(R.id.rcvcategory);
        toolbar.setVisibility(View.VISIBLE);
        floatbtnthem=view.findViewById(R.id.floatbtnthem);
        daoFood = new DaoProducts(getActivity());
        foodArrayList = new ArrayList<>();
        foodAdapter = new ProductAdapter(foodArrayList,getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        rcvcategory.setLayoutManager(gridLayoutManager);
        rcvcategory.setHasFixedSize(true);
        rcvcategory.setAdapter(foodAdapter);
        storage = FirebaseStorage.getInstance();
        daoFood.getAll(new ProductsCallback() {
            @Override
            public void onSuccess(ArrayList<Products> lists) {
                foodArrayList.clear();
                for (int i =0;i<lists.size();i++){
                    if (lists.get(i).getIdStore()!=null && lists.get(i).getIdStore().equalsIgnoreCase(idstore)){
                        foodArrayList.add(lists.get(i));
                        foodAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onError(String message) {

            }
        });
        floatbtnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheefAddProducts bottomSheef_add_category = new BottomSheefAddProducts();
                bottomSheef_add_category.show(getFragmentManager(),bottomSheef_add_category.getTag());
            }
        });
        intswipe(view);
        return view;
    }
    public void intswipe(final View v) {

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                Bundle args = new Bundle();
                daoFood =new DaoProducts(getContext());

                daoFood.getAll(new ProductsCallback() {
                    @Override
                    public void onSuccess(ArrayList<Products> lists) {
                        foodArrayList.clear();
                        foodArrayList.addAll(lists);
                        foodAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                args.putString("idfood", foodArrayList.get(position).getIdP() + "");
                args.putString("namefood", foodArrayList.get(position).getNameP() + "");
                args.putString("gia", foodArrayList.get(position).getPrice() + "");
                args.putString("soluong", foodArrayList.get(position).getQuantity() + "");
                args.putString("mota", foodArrayList.get(position).getDescription() + "");
                args.putString("diachi", foodArrayList.get(position).getAddress() + "");
                args.putString("status", foodArrayList.get(position).getStatus() + "");
                args.putString("idstore", foodArrayList.get(position).getIdStore() + "");
                args.putString("matl", foodArrayList.get(position).getId() + "");
                args.putString("img", foodArrayList.get(position).getImage());
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Thông Báo");
                        builder.setMessage("Bạn có chắc muốn xóa không");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String maloai = foodArrayList.get(position).getIdP();
                                String vitri = foodArrayList.get(position).getImage();
                                foodArrayList = new ArrayList<>();
                                daoFood = new DaoProducts(getContext());
                                daoFood.delete(maloai);
                                String storageUrl = vitri;
                                StorageReference img = storage.getReferenceFromUrl(storageUrl);
                                img.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // File deleted successfully
                                        Log.d("TAG", "onSuccess: deleted file");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Uh-oh, an error occurred!
                                        Log.d("TAG", "onFailure: did not delete file");
                                    }
                                });
                                daoFood.getAll(new ProductsCallback() {
                                    @Override
                                    public void onSuccess(ArrayList<Products> lists) {
                                        foodArrayList.clear();
                                        foodArrayList.addAll(lists);
                                        foodAdapter = new ProductAdapter(foodArrayList,getActivity());
                                        rcvcategory.setAdapter(foodAdapter);
                                    }

                                    @Override
                                    public void onError(String message) {

                                    }
                                });
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                foodArrayList = new ArrayList<>();

                                daoFood =new DaoProducts(getContext());

                                daoFood.getAll(new ProductsCallback() {
                                    @Override
                                    public void onSuccess(ArrayList<Products> lists) {
                                        foodArrayList.clear();
                                        foodArrayList.addAll(lists);
                                        foodAdapter = new ProductAdapter(foodArrayList,getActivity());
                                        rcvcategory.setAdapter(foodAdapter);
                                        foodAdapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onError(String message) {

                                    }
                                });

                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    case ItemTouchHelper.RIGHT:
                        BottomSheefUpdateProducts bottomSheef_update_theLoai = new BottomSheefUpdateProducts();
                        bottomSheef_update_theLoai.setArguments(args);
                        bottomSheef_update_theLoai.show(getActivity().getSupportFragmentManager(), bottomSheef_update_theLoai.getTag());

                        break;

                }


            }

            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.Do))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                        .create()
                        .decorate();

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.Xanh))
                        .addSwipeRightActionIcon(R.drawable.ic_sua)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcvcategory);
    }
}
