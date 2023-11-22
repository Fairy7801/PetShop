package com.example.petshop.fragment;

import static com.example.petshop.activity.MainActivity.toolbar;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.adapter.ProductAdapter;
import com.example.petshop.callback.ProductsCallback;
import com.example.petshop.dao.DaoProducts;
import com.example.petshop.databinding.FragmentProductsBinding;
import com.example.petshop.dialog.BottomSheefAddProducts;
import com.example.petshop.dialog.BottomSheefUpdateProducts;
import com.example.petshop.model.Products;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class FragmentProducts extends Fragment {
    private FragmentProductsBinding binding;
    private ArrayList<Products> productsArrayList;
    private DaoProducts daoProducts;
    private ProductAdapter productAdapter;
    private String idCategoy;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        toolbar.setVisibility(View.VISIBLE);
        daoProducts = new DaoProducts(requireActivity());
        productsArrayList = new ArrayList<>();

        Bundle args = getArguments();
        if (args != null) {
            setupRecyclerView();
            idCategoy = args.getString("idCategory");
            updateProductList(idCategoy);
        }

        binding.floatbtnaddproduct.setOnClickListener(v -> {
            Bundle args1 = new Bundle();
            args1.putString("idCategory", idCategoy);
            BottomSheefAddProducts bottomSheefAddProducts = new BottomSheefAddProducts();
            bottomSheefAddProducts.setArguments(args1);
            bottomSheefAddProducts.show(getFragmentManager(), bottomSheefAddProducts.getTag());
        });

        intswipe(view);
        return view;
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(productsArrayList, getActivity());
        binding.rcvproductfrag.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        binding.rcvproductfrag.setHasFixedSize(true);
        binding.rcvproductfrag.setAdapter(productAdapter);
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
                args2.putString("idP", productsArrayList.get(position).getIdP()+"");
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
        itemTouchHelper.attachToRecyclerView(binding.rcvproductfrag);
    }
    private void showDeleteConfirmationDialog(final int position, String idCategoy) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông Báo");
        builder.setMessage("Bạn có chắc muốn xóa không");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            String idCategoryDelete = productsArrayList.get(position).getIdP();
            daoProducts = new DaoProducts(requireActivity());
            daoProducts.delete(idCategoryDelete);

            new Handler().postDelayed(() -> updateProductList(idCategoy), 500);

            dialog.cancel();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
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
            }
        });
    }
}
