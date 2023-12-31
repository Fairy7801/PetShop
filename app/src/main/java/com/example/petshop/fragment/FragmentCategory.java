package com.example.petshop.fragment;

import static com.example.petshop.activity.MainActivity.toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.example.petshop.adapter.CategoriesAdapter;
import com.example.petshop.callback.CategoriesCallback;
import com.example.petshop.dao.DaoCategories;
import com.example.petshop.dialog.BottomSheefAddCategory;
import com.example.petshop.dialog.BottomSheefUpdateCategory;
import com.example.petshop.model.Categories;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class FragmentCategory extends Fragment {
    public static RecyclerView rcvcategory;
    FloatingActionButton floatbtnthem;
    ArrayList<Categories> categoriesArrayList;
    DaoCategories daoCategories;
    CategoriesAdapter categoriesAdapter;
    FirebaseStorage storage;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        rcvcategory = view.findViewById(R.id.rcvcategory);
        toolbar.setVisibility(View.VISIBLE);
        floatbtnthem = view.findViewById(R.id.floatbtnthem);
        daoCategories = new DaoCategories(getActivity());
        categoriesArrayList = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(categoriesArrayList, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rcvcategory.setLayoutManager(gridLayoutManager);
        rcvcategory.setHasFixedSize(true);
        rcvcategory.setAdapter(categoriesAdapter);
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        updateCategoryList();

        floatbtnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheefAddCategory bottomSheefAddCategory = new BottomSheefAddCategory();
                bottomSheefAddCategory.show(getFragmentManager(), bottomSheefAddCategory.getTag());
            }
        });
        intswipe(view);
        return view;
    }

    public void intswipe(final View v) {
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
                updateCategoryList();
                Bundle args = new Bundle();
                args.putString("idCategory", categoriesArrayList.get(position).getId() + "");
                args.putString("tokenstore",mAuth.getUid().toString());

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        showDeleteConfirmationDialog(position);
                        break;
                    case ItemTouchHelper.RIGHT:
                        BottomSheefUpdateCategory bottomSheefUpdateCategory = new BottomSheefUpdateCategory();
                        bottomSheefUpdateCategory.setArguments(args);
                        bottomSheefUpdateCategory.show(getActivity().getSupportFragmentManager(), bottomSheefUpdateCategory.getTag());
                        break;
                    case ItemTouchHelper.DOWN:
                    case ItemTouchHelper.UP:
                        FragmentProducts fragmentProducts = new FragmentProducts();
                        fragmentProducts.setArguments(args);
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
        itemTouchHelper.attachToRecyclerView(rcvcategory);
    }

    private void showDeleteConfirmationDialog(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông Báo");
        builder.setMessage("Bạn có chắc muốn xóa không");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String idCategory = categoriesArrayList.get(position).getToken();
                daoCategories.delete(idCategory);
                updateCategoryList();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateCategoryList();
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void updateCategoryList() {
        daoCategories.getAll(new CategoriesCallback() {
            @Override
            public void onSuccess(ArrayList<Categories> lists) {
                categoriesArrayList.clear();
                categoriesArrayList.addAll(lists);
                categoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
            }
        });
    }

}
