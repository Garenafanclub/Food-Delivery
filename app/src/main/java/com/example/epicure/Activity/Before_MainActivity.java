package com.example.epicure.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.epicure.databinding.ActivityBeforeMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.concurrent.Executor;

public class Before_MainActivity extends FireBaseActivity {
   ActivityBeforeMainBinding binding;
   public static int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBeforeMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Executor executor = ContextCompat.getMainExecutor(this);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if(mAuth.getCurrentUser() != null || acct != null) {
            BiometricPrompt biometricPrompt = new BiometricPrompt(Before_MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(getApplicationContext(), "LOGIN VIA EMAIL: " + errString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    // NO USE OF IF ELSE STATEMENT...
                        startActivity(new Intent(getApplicationContext(), Main_DashBoard.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getApplicationContext(), "Authentication error: ", Toast.LENGTH_SHORT).show();
                }
            });

            //noinspection deprecation
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Unlock Epicure")
                    .setDescription("Unlock your screen with PIN, pattern, password, face, or fingerprint")
                    .setConfirmationRequired(true)
                    .setDeviceCredentialAllowed(true)
                    .build();

            biometricPrompt.authenticate(promptInfo);
        }
       setVariable();
    }


    private void setVariable() {
        binding.loginText.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),LoginActivity.class)));

        binding.signInText.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
    }
}

/*
                if(mAuth.getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), Main_DashBoard.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else {
                    startActivity(new Intent(getApplicationContext(),Before_MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
 */