package com.hangyeollee.go4lunch.ui.logIn_activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityLogInBinding;
import com.hangyeollee.go4lunch.ui.main_home_activity.MainHomeActivity;
import com.hangyeollee.go4lunch.ui.ViewModelFactory;

import java.util.Collections;

public class LogInActivity extends AppCompatActivity {
    private ActivityLogInBinding binding;

    private LogInViewModel mViewModel;

    private CallbackManager mCallbackManager;

    private ActivityResultLauncher<Intent> mActivityResultLauncher;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(LogInViewModel.class);

        mCallbackManager = CallbackManager.Factory.create();

        buttonLogInLogoSetup();
        setUpListeners();

        onFacebookLogInResult();
        // Retrieve data from googleLogIn()
        mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onGoogleLogInResult);

    }

    /**
     * Start the next activity (MainHomeActivity)
     */
    private void startMainHomeActivity() {
        startActivity(new Intent(LogInActivity.this, MainHomeActivity.class));
        finish();
    }

    private void showSnackBarInLogInActivity(String message) {
        Snackbar snackbar = Snackbar.make(binding.constraintLayoutParentLayout, message, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.show();
    }

    /**
     * Set logos for the buttons
     */
    private void buttonLogInLogoSetup() {
        Drawable facebookLogo = AppCompatResources.getDrawable(this, R.drawable.ic_facebook_logo);
        binding.ButtonFacebookLogIn.setCompoundDrawablesWithIntrinsicBounds(facebookLogo, null, null, null);
        Drawable googleLogo = AppCompatResources.getDrawable(this, R.drawable.ic_google_logo);
        binding.ButtonGoogleLogIn.setCompoundDrawablesWithIntrinsicBounds(googleLogo, null, null, null);
    }

    /**
     * Set up listeners for the buttons
     */
    private void setUpListeners() {
        binding.ButtonGoogleLogIn.setOnClickListener(view -> googleLogIn());
        binding.ButtonFacebookLogIn.setOnClickListener(view -> {
            // Start facebook log in activity
            LoginManager.getInstance().logInWithReadPermissions(this, mCallbackManager, Collections.singletonList("public_profile"));
        });
    }

    /**
     * Launch google log in activity for result via ActivityResultLauncher
     */
    private void googleLogIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(this, gso);
        mActivityResultLauncher.launch(gsc.getSignInIntent());
    }

    private void onGoogleLogInResult(ActivityResult result) {
        // If the google log in activity got lunched successfully
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            // Get signed accounts from the google log in activity
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            // Try to get the logged in accounts and if fails catch the exception
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Link with firebase
                firebaseAuthWithGoogle(account);
            }
            // Log in fails
            catch (ApiException e) {
                Log.e("Google log in failed", e.getMessage());
                showSnackBarInLogInActivity("Google log in failed");
            }
        }
        if (result.getResultCode() == RESULT_CANCELED) {
            showSnackBarInLogInActivity("Google log in canceled");
        }
    }

    /**
     * Link firebase and google log in
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mViewModel.getFirebaseInstance().signInWithCredential(credential).addOnSuccessListener(this,
                authResult -> {
                    Log.d("firebaseAuthWithGoogle", ":success");
                    startMainHomeActivity();
                }
        ).addOnFailureListener(this,
                e -> {
                    Log.d("firebaseAuthWithGoogle", ":failure/" + e.getMessage());
                    Toast.makeText(LogInActivity.this, "firebaseAuthWithGoogle:failure", Toast.LENGTH_SHORT).show();
                }
        );

    }

    /**
     * Retrieve the result of facebook log in
     */
    private void onFacebookLogInResult() {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                showSnackBarInLogInActivity("Facebook log in canceled");
            }

            @Override
            public void onError(@NonNull FacebookException exception) {
                Log.e("FacebookLogIn", "failed", exception);
                showSnackBarInLogInActivity("Error occurred : " + exception.getMessage());
            }
        });
    }

    private void firebaseAuthWithFacebook(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mViewModel.getFirebaseInstance().signInWithCredential(credential).addOnCompleteListener(this,
                task -> {
                    if (task.isSuccessful()) {
                        // Firebase linking success
                        Log.e("firebaseAuthWithFb", ":success");
                        startMainHomeActivity();
                    } else {
                        // Firebase linking fails
                        Log.e("firebaseAuthWithFb", ":failure", task.getException());
                    }
                }
        );
    }


}