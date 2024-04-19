package com.example.epicure.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epicure.Adaptor.CartAdapter;
import com.example.epicure.Helper.ManagmentCart;
import com.example.epicure.databinding.ActivityCartBinding;

public class CartActivity extends FireBaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        setVariable();
        calculateCart();
        initList();
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshPage(); // Call your refresh logic when the activity resumes
    }
    private void refreshPage() {
        // Implement your refresh logic here
        // For example, if you're refreshing a WebView, you might use:
        // webView.reload();
        // If you're refreshing a RecyclerView, you might call:
         adapter.notifyDataSetChanged();
        // Or you might fetch new data from a server, etc.
    }

    private void initList() {
        if(managmentCart.getListCart().isEmpty())
        {
            binding.animation.setVisibility(View.VISIBLE);
            binding.EmptyCart.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }else {
            binding.animation.setVisibility(View.GONE);
            binding.EmptyCart.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculateCart());
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.02;  // tax is 2%
        double delivery = 10;  // in dollar

//        tax = Math.round((managmentCart.getTotalFee() * percentTax) * 100.0) /100 ;
//
//        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) /100;

        tax = Math.floor((managmentCart.getTotalFee() * percentTax) * 10) / 10.0;

        double itemTotal = managmentCart.getTotalFee();
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) /100;


        binding.SubtotalTxt.setText("$" + itemTotal);
        binding.DeliveryTxt.setText("$" + delivery);
        binding.TaxTxt.setText("$" + tax);
        binding.TotalTxt.setText("$" + total);
    }

    private void setVariable() {
        binding.backBtnCart.setOnClickListener(view -> {
            Log.d("ImageView Click", "ImageView clicked!");
            finish();
        });
        
        binding.placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CartActivity.this, "Incredible taste, confirmed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}