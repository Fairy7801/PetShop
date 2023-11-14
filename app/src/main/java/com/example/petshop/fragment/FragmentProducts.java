package com.example.petshop.fragment;

import static com.example.petshop.activity.MainActivity.toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import android.widget.TextView;

import com.example.petshop.R;
import com.example.petshop.adapter.CategoriesAdapter;
import com.example.petshop.adapter.ProductAdapter;
import com.example.petshop.callback.CategoriesCallback;
import com.example.petshop.callback.ProductsCallback;
import com.example.petshop.callback.StoreCallback;
import com.example.petshop.dao.DaoCategories;
import com.example.petshop.dao.DaoProducts;
import com.example.petshop.dao.DaoStore;
import com.example.petshop.dialog.BottomSheefAddCategory;
import com.example.petshop.dialog.BottomSheefAddProducts;
import com.example.petshop.dialog.BottomSheefUpdateCategory;
import com.example.petshop.dialog.BottomSheefUpdateProducts;
import com.example.petshop.model.Categories;
import com.example.petshop.model.Products;
import com.example.petshop.model.Store;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class FragmentProducts extends Fragment {
    public static RecyclerView rcvProduct;
    FloatingActionButton floatbtnthem;
    ArrayList<Products> productsArrayList;
    DaoProducts daoProducts;
    DaoStore daoStore;
    ProductAdapter productAdapter;
    FirebaseStorage storage;
    String idCategoy, nameStore;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        rcvProduct = view.findViewById(R.id.rcvproductfrag);
        floatbtnthem=view.findViewById(R.id.floatbtnaddproduct);
        toolbar.setVisibility(View.VISIBLE);
        daoProducts = new DaoProducts(getActivity());
        productsArrayList = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Bundle args = getArguments();
        if (args != null) {
            setupRecyclerView();
            idCategoy = args.getString("idCategory");
            updateProductList(idCategoy);
        }

        floatbtnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args1 = new Bundle();
                args1.putString("idCategory", idCategoy);
                BottomSheefAddProducts bottomSheefAddProducts = new BottomSheefAddProducts();
                bottomSheefAddProducts.setArguments(args1);
                bottomSheefAddProducts.show(getFragmentManager(), bottomSheefAddProducts.getTag());
            }
        });

        intswipe(view);
        return view;
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(productsArrayList, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rcvProduct.setLayoutManager(gridLayoutManager);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setAdapter(productAdapter);
    }

    private void intswipe(final View v) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP;
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                updateProductList(idCategoy);
                Bundle args2 = new Bundle();
                getNameStore();
                args2.putString("idP", productsArrayList.get(position).getIdP()+"");
                args2.putString("nameStore", nameStore);
                args2.putString("idCate", productsArrayList.get(position).getId()+"");

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        showDeleteConfirmationDialog(position, idCategoy);
                        break;
                    case ItemTouchHelper.RIGHT:
                        BottomSheefUpdateProducts bottomSheefUpdateProducts = new BottomSheefUpdateProducts();
                        bottomSheefUpdateProducts.setArguments(args2);
                        bottomSheefUpdateProducts.show(getActivity().getSupportFragmentManager(), bottomSheefUpdateProducts.getTag());
                        break;
                    case ItemTouchHelper.DOWN:
                    case ItemTouchHelper.UP:
                        FragmentDetailProduct fragmentProducts = new FragmentDetailProduct();
                        fragmentProducts.setArguments(args2);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fr_l, fragmentProducts)
                                .addToBackStack(null)
                                .commit();
                        break;
                }
            }
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dY > 0) {
                        // Vuốt xuống
                        View itemView = viewHolder.itemView;
                        Paint p = new Paint();
                        p.setColor(ContextCompat.getColor(getContext(), R.color.cam));
                        c.drawRect((float) viewHolder.itemView.getLeft(), (float) viewHolder.itemView.getTop(),
                                (float) viewHolder.itemView.getRight(), (float) viewHolder.itemView.getBottom(), p);
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                    } else if (dY < 0) {
                        // Vuốt lên
                        Paint p1 = new Paint();
                        p1.setColor(ContextCompat.getColor(getContext(), R.color.cam));
                        c.drawRect((float) viewHolder.itemView.getLeft(), (float) viewHolder.itemView.getTop(),
                                (float) viewHolder.itemView.getRight(), (float) viewHolder.itemView.getBottom(), p1);
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    } else {
                        if (dX > 0) {
                            // Vuốt phải
                            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.Xanh))
                                    .addSwipeRightActionIcon(R.drawable.ic_sua)
                                    .addSwipeRightLabel("Sửa")
                                    .setSwipeRightLabelColor(R.color.Xanh)
                                    .create()
                                    .decorate();
                        } else {
                            // Vuốt trái
                            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.Do))
                                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                                    .addSwipeLeftLabel("Xóa")
                                    .setSwipeLeftLabelColor(R.color.Do)
                                    .create()
                                    .decorate();
                        }
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcvProduct);
    }
    private void showDeleteConfirmationDialog(final int position, String idCategoy) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông Báo");
        builder.setMessage("Bạn có chắc muốn xóa không");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String idCategoryDelete = productsArrayList.get(position).getIdP();
                daoProducts = new DaoProducts(getContext());
                daoProducts.delete(idCategoryDelete);
                updateProductList(idCategoy);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void updateProductList(String categoryId) {
        daoProducts.getAll(new ProductsCallback() {
            @Override
            public void onSuccess(ArrayList<Products> lists) {
                productsArrayList.clear();
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getId() != null && lists.get(i).getId().equalsIgnoreCase(categoryId)) {
                        productsArrayList.add(lists.get(i));
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    public void getNameStore() {
        daoStore = new DaoStore(getContext());
        daoStore.getAll(new StoreCallback() {
            @Override
            public void onSuccess(ArrayList<Store> lists) {
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getTokenStore() != null && lists.get(i).getTokenStore().equalsIgnoreCase(mAuth.getUid())) {
                        nameStore = lists.get(i).getName();
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

}
