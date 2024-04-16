package com.example.epicure.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epicure.Adaptor.BestFoodsAdapter;
import com.example.epicure.Adaptor.CategoryAdapter;
import com.example.epicure.Domain.Category;
import com.example.epicure.Domain.Foods;
import com.example.epicure.Domain.Location;
import com.example.epicure.Domain.Price;
import com.example.epicure.Domain.Time;
import com.example.epicure.R;
import com.example.epicure.databinding.ActivityMainDashBoardBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Main_DashBoard extends FireBaseActivity {

    ActivityMainDashBoardBinding MainBinding;
    private static final String WEB_CLIENT_ID = "978951774440-ptn5egjpekrnnlij2ju92d3f03fso1qp.apps.googleusercontent.com";
    private GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    private FirebaseAuth mAuth; // Optional for Firebase Auth sign-out
    TextView userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainBinding = ActivityMainDashBoardBinding.inflate(getLayoutInflater());
        setContentView(MainBinding.getRoot());

        initLocation();
        initTime();
        initPrice();
        initBestFood();
        initCategory();
        setVariable();

        userName = findViewById(R.id.user_name_fetch);

         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        if (hasUserInfo()) {
            String userName = retrieveUserInfo();
            displayUserName(userName);
        } else {
            displayLoginRequiredMessage();
        }
    }

    private void setVariable() {
        // LOG OUT FROM BOTH GOOGLE AS WELL AS EMAIL...
        MainBinding.logOut.setOnClickListener(view -> {
            mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
                // Sign out successful, handle next steps
                if (task.isSuccessful()) {
                    // Optional: Sign out from Firebase Auth as well
                    if (mAuth != null) {
                        mAuth.signOut();
                        finish();
                    }
                    // ... (Clear user data, redirect to login, etc.)
                }else {
                    Log.w("Main_DashBoard", "signOut:failure", task.getException());
                }
            });
            finish();
            mAuth.signOut();
            Toast.makeText(Main_DashBoard.this, "Successfully logout", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // SEARCH FOOD...
        MainBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = MainBinding.searchButton.getText().toString();
                if(!text.isEmpty())
                {
                    Intent intent = new Intent(getApplicationContext(),ListFoodsActivity.class);
                    intent.putExtra("text",text);
                    intent.putExtra("isSearch",true);
                    startActivity(intent);
                }
            }
        });
    }

    private void initBestFood() {
        DatabaseReference dRef = database.getReference("Foods");
        MainBinding.progressBarBestFood.setVisibility(View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();
        Query query = dRef.orderByChild("BestFood").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot issue: snapshot.getChildren())
                    {
                        list.add(issue.getValue(Foods.class));
                    }
                    if(list.size()>0)
                    {
                        MainBinding.BestFoodView.setLayoutManager(new LinearLayoutManager(Main_DashBoard.this, LinearLayoutManager.HORIZONTAL,false));
                        RecyclerView.Adapter<BestFoodsAdapter.viewholder> adapter = new BestFoodsAdapter(list);  // see video...
                        MainBinding.BestFoodView.setAdapter(adapter);
                    }
                    MainBinding.progressBarBestFood.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initCategory() {
        DatabaseReference dRef = database.getReference("Category");
        MainBinding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list = new ArrayList<>();
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot issue: snapshot.getChildren())
                    {
                        list.add(issue.getValue(Category.class));
                    }
                    if(list.size()>0)
                    {
                        MainBinding.categoryView.setLayoutManager(new GridLayoutManager(Main_DashBoard.this,4));
                        RecyclerView.Adapter<CategoryAdapter.viewholder> adapter = new CategoryAdapter(list);  // see video...
                        MainBinding.categoryView.setAdapter(adapter);
                    }
                    MainBinding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initLocation() {
        DatabaseReference myRef = database.getReference("Location");
        ArrayList<Location> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Location.class));
                    }
                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(Main_DashBoard.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    MainBinding.locationSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initTime() {
        DatabaseReference myRef = database.getReference("Time");
        ArrayList<Time> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Time.class));
                    }
                    ArrayAdapter<Time> adapter = new ArrayAdapter<>(Main_DashBoard.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    MainBinding.timeSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initPrice() {
        DatabaseReference myRef = database.getReference("Price");
        ArrayList<Price> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Price.class));
                    }
                    ArrayAdapter<Price> adapter = new ArrayAdapter<>(Main_DashBoard.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    MainBinding.priceSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String retrieveUserInfo() {
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        return prefs.getString("name", null);
    }

    private boolean hasUserInfo() {
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        return prefs.contains("name");
    }

    private void displayUserName(String username) {
        userName.setText(username);
    }

    private void displayLoginRequiredMessage() {
        userName.setText("Please sign in to see your name.");
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main_DashBoard.this);

        builder.setTitle("Exit Confirmation");
        builder.setMessage("Are you sure you want to exit the app?");

        builder.setPositiveButton("Yes", (dialog, which) -> finishAffinity());

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}