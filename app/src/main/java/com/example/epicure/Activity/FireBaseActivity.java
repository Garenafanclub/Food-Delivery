package com.example.epicure.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseActivity extends AppCompatActivity {
  FirebaseAuth mAuth;
  FirebaseDatabase database;
  public String TAG= "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
//        getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));
    }
}