package com.example.epicure.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.epicure.R;
import com.example.epicure.databinding.ActivitySignUpBinding;

import java.util.Objects;

public class SignUpActivity extends FireBaseActivity {

   ActivitySignUpBinding signUpBinding;
   ProgressDialog pd;
   String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());
        pd = new ProgressDialog(this);

        pd.setTitle(getString(R.string.string_title_signup));
        pd.setMessage(getString(R.string.string_message_signup));
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        signUpBinding.SignUpButtonPage.setOnClickListener(view -> createUser());
    }

    private void createUser() {
        String name = signUpBinding.NameEditSignup.getText().toString().trim();
        String email = signUpBinding.EmailNameSignup.getText().toString().trim();
        String password = signUpBinding.PasswordNameLogin.getText().toString().trim();
        String repassword = signUpBinding.SamePasswordNameLogin.getText().toString().trim();

        if(TextUtils.isEmpty(name))
        {
            signUpBinding.NameEditSignup.setError("Name is required");
            signUpBinding.NameEditSignup.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(email))
        {
            signUpBinding.EmailNameSignup.setError("Email is required");
            signUpBinding.EmailNameSignup.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            signUpBinding.PasswordNameLogin.setError("Password is required");
            signUpBinding.PasswordNameLogin.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(repassword))
        {
            signUpBinding.SamePasswordNameLogin.setError("Re-Password is required");
            signUpBinding.SamePasswordNameLogin.requestFocus();
            return;
        }
         if(password.length()<=6)
         {
             signUpBinding.PasswordNameLogin.setError("Password must be >=6 characters");
             signUpBinding.PasswordNameLogin.requestFocus();
             return;
         }
         if(!repassword.equals(password))
         {
            signUpBinding.SamePasswordNameLogin.setError("Please write your same password as you provided above");
            signUpBinding.SamePasswordNameLogin.requestFocus();
            return;
         }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signUpBinding.EmailNameSignup.setError("Please provide a valid Email");
            signUpBinding.EmailNameSignup.requestFocus();
            return;
        }
         pd.show();
         mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpActivity.this, task -> {
              if(task.isSuccessful())
              {
                  pd.dismiss();
                  Log.i(TAG,"onComplete: ");
                  Toast.makeText(getApplicationContext(),"User registered successfully",Toast.LENGTH_SHORT).show();
                  user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                  startActivity(new Intent(getApplicationContext(), Main_DashBoard.class));
              }
              else{
                  pd.dismiss();
                  Log.i(TAG,"failure: " + Objects.requireNonNull(task.getException()).getMessage());
                  Toast.makeText(this, "User already exist...Please Login", Toast.LENGTH_SHORT).show();
              }
         });
    }
}