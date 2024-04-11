package com.example.epicure.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.epicure.R;
import com.example.epicure.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends FireBaseActivity {

   ActivityLoginBinding loginBinding;
    ProgressDialog progressDialog;
    private static final int RC_SIGN_IN = 9001;
    ImageButton google_image_id;
    private GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        progressDialog = new ProgressDialog(this);
        google_image_id = findViewById(R.id.google_image_id);

        progressDialog.setTitle(getString(R.string.string_title_login));
        progressDialog.setMessage(getString(R.string.string_message_login));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        mAuth = FirebaseAuth.getInstance();

         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken("978951774440-ptn5egjpekrnnlij2ju92d3f03fso1qp.apps.googleusercontent.com")
                .requestEmail() // Request email for additional information
                .build();

        // Build GoogleSignInClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // LOGIN VIA GOOGLE...
        findViewById(R.id.google_image_id).setOnClickListener(v -> signIn());

        // RECOVERY YOUR PASSWORD...
        loginBinding.RecoveryYourPassword.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Forgot_password.class)));

        // LOGIN VIA EMAIL...
        loginBinding.loginButton.setOnClickListener(view -> {
            if(mAuth.getCurrentUser() != null)
            {
                startActivity(new Intent(getApplicationContext(),Main_DashBoard.class));
            }
            else {
                loginUser();
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...)
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
            if(account != null) {
                String username = account.getDisplayName();
                storeUserInfo(username);
                launchDashboardActivity();
            }
        } catch (ApiException e) {
            // Sign in failed, log the exception
            Toast.makeText(this, "Error in getting Google's Info", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeUserInfo(String userName) {
        // Consider using SharedPreferences with encryption or a secure database
        // This is a simplified example for demonstration purposes
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        prefs.edit().putString("name", userName).apply();
    }

    private void launchDashboardActivity() {
        startActivity(new Intent(getApplicationContext(),Main_DashBoard.class));
    }

    private void firebaseAuthWithGoogle(String idToken)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful())
                    {
                        mAuth.getCurrentUser();
                        startActivity(new Intent(getApplicationContext(),Main_DashBoard.class));
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Problem found in Firebase Login",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loginUser() {
        String email = loginBinding.EmailNameLogin.getText().toString().trim();
        String password = loginBinding.PasswordNameLogin.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            loginBinding.EmailNameLogin.setError("Please provide a valid Email");
            loginBinding.EmailNameLogin.requestFocus();
            return;
        }
        if(!email.isEmpty() && !password.isEmpty())
        {
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, task -> {
                if(task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Welcome Again!", Toast.LENGTH_SHORT).show();
                        Before_MainActivity.flag = 1; // IMPORTANT...
                        startActivity(new Intent(getApplicationContext(), Main_DashBoard.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Authentication failed during Login", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this, "Please fill email and password", Toast.LENGTH_SHORT).show();
        }
    }
}

//        setCancelable(false)	Disables canceling the dialog by user interaction.
//        setIndeterminate(true)	Sets the progress bar to an animating spinner.