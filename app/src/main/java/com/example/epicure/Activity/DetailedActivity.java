
package com.example.epicure.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.epicure.Domain.Foods;
import com.example.epicure.R;
import com.example.epicure.databinding.ActivityDetailedBinding;

public class DetailedActivity extends FireBaseActivity {
    ActivityDetailedBinding binding;
    private Foods object;
    private int num = 1;

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
        binding.timeDetail.setText(object.getTimeValue()+"min");
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}