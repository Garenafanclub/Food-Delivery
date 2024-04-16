package com.example.epicure.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epicure.Adaptor.FoodListAdapter;
import com.example.epicure.Domain.Foods;
import com.example.epicure.databinding.ActivityListFoodsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodsActivity extends FireBaseActivity {
        ActivityListFoodsBinding binding;
        private RecyclerView.Adapter adapterListFood;
        private int categoryId;
        private String categoryName;
        private String searchTxt;
        private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListFoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        Log.d("ListFoodsActivity", "Category ID received: " + categoryId); // Add this line
        initList();

    }

    private void initList() {
        DatabaseReference dRef = database.getReference("Foods");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();
        Query query;
        if(isSearch)
        {
            query = dRef.orderByChild("Title").startAt(searchTxt).endAt(searchTxt+'\uf8ff');
        }
        else {
            query = dRef.orderByChild("CategoryId").equalTo(categoryId);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("ListFoodsActivity", "Query executed");
                if(snapshot.exists()) {
                    for(DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Foods.class));
                        Log.d("ListFoodsActivity", "Food item added: " + issue.getValue(Foods.class).getTitle());
                    }
                    if(list.size() > 0) {
                        binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this, 2));
                        adapterListFood = new FoodListAdapter(list);
                        binding.foodListView.setAdapter(adapterListFood);
                        Log.d("ListFoodsActivity", "Adapter set with " + list.size() + " items");
                    }
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    Log.d("ListFoodsActivity", "No data found");
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ListFoodsActivity", "Query cancelled: " + error.getMessage());
                binding.progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId",0);
        categoryName = getIntent().getStringExtra("CategoryName");
        searchTxt = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch",false);

        binding.titleTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(view -> finish());
    }
}



/*
getIntent().getExtras() is used to get values from intent that are stored in bundle.
Intent class is used to switch between activities.
But sometimes we need to send data from one activity to another. So, at this particular moment
we need to set some values to intent that can be transferred to destination activity.
 */