
package com.example.epicure.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.epicure.Domain.Foods;
import com.example.epicure.Helper.ManagmentCart;
import com.example.epicure.databinding.ActivityDetailedBinding;

public class DetailedActivity extends FireBaseActivity {
    ActivityDetailedBinding binding;
    private Foods object;
    private int num = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    @SuppressLint("SetTextI18n")
    private void setVariable() {
        managmentCart = new ManagmentCart(this);
        binding.backBtnDetail.setOnClickListener(view -> finish());

        Glide.with(DetailedActivity.this)
                .load(object.getImagePath())
                .into(binding.imageDetail);

        binding.priceDetail.setText("$"+object.getPrice());
        binding.titleDetail.setText(object.getTitle());
        binding.ratingDetail.setText(object.getStar()+" Rating");
        binding.descriptionTxt.setText(object.getDescription());
        binding.ratingBar.setRating((float) object.getStar());   // FLOAT....
        binding.totalDetailPrice.setText(num*object.getPrice()+"$");
        binding.timeDetail.setText(object.getTimeValue()+" min");

        // PLUS BUTTON FOR HOW MUCH QUANTITY YOU WANT TO ADD IN YOUR CART............
        binding.plusBtn.setOnClickListener(view -> {
            num = num + 1;
            binding.numText.setText(num+" ");
            binding.totalDetailPrice.setText("$"+(num*object.getPrice()));
        });

        // MINUS BUTTON TO CUT THE QUANTITY...........
        binding.minusBtn.setOnClickListener(view -> {
            if(num>1) {
                num = num - 1;
                binding.numText.setText(num + " ");
                binding.totalDetailPrice.setText("$" + (num * object.getPrice()));
            }
        });

        // ADD TO CART BUTTON TO ADD ITEMS OR OBJECTS IN CART....
        binding.addToCartBtn.setOnClickListener(view -> {
            object.setNumberInCart(num);
            managmentCart.insertFood(object);
        });
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}