package com.hangyeollee.go4lunch.view;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityLogInBinding;

import java.util.Collections;

public class LogInActivity extends BaseActivity<ActivityLogInBinding> {
    // Todo : I wanna show SnackBar at MainActivity to inform users, should I code here or in MainActivity ?
    // Todo : When pressing back button from the phone either the one from the activity, it should go back to previous activity

    private FirebaseAuth mFirebaseAuth;
    private CallbackManager mCallbackManager;

    private ActivityResultLauncher<Intent> mActivityResultLauncher;

    @Override
    ActivityLogInBinding getViewBinding() {
        return ActivityLogInBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        buttonLogInLogoSetup();
        setUpListeners();

        onFacebookLogInResult();
        mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                // Retrieve data from googleLogIn()
                onGoogleLogInResult(result);
            }
        });

    }

    /**
     * Start the next activity(MainActivity)
     */
    private void startMainActivity() {
        startActivity(new Intent(LogInActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Show snack bar at the bottom of logInActivity to inform users
     * @param message
     */
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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.firebase_web_client_id)).requestEmail().build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(this, gso);
        mActivityResultLauncher.launch(gsc.getSignInIntent());
    }

    /**
     * Retrieve the result of google log in activity
     * @param result
     */
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
                startMainActivity();
            }
            // Log in fails
            catch (ApiException e) {
                Log.w(TAG, "Google log in failed", e);
                showSnackBarInLogInActivity("Google log in failed");
            }
        }
        if (result.getResultCode() == RESULT_CANCELED) {
            showSnackBarInLogInActivity("Google log in canceled");
        }
    }

    /**
     * Link firebase and google log in
     * @param account
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnSuccessListener(this, authResult -> Log.d(TAG, "firebaseAuthWithGoogle:success")).addOnFailureListener(this, listener -> Log.d(TAG, "firebaseAuthWithGoogle:failure"));

    }

    /**
     * Retrieve the result of facebook log in
     */
    private void onFacebookLogInResult() {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseAuthWithFacebook(loginResult.getAccessToken());
                startMainActivity();
            }

            @Override
            public void onCancel() {
                showSnackBarInLogInActivity("Facebook log in canceled");
            }

            @Override
            public void onError(@NonNull FacebookException exception) {
                Log.w(TAG, "Facebook log in failed", exception);
                showSnackBarInLogInActivity("Error occurred : " + exception.getMessage());
            }
        });
    }

    /**
     * Link firebase and facebook log in
     * @param accessToken
     */
    private void firebaseAuthWithFacebook(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Firebase linking success
                    Log.d(TAG, "firebaseAuthWithFacebook:success");
                } else {
                    // Firebase linking fails
                    Log.w(TAG, "firebaseAuthWithFacebook:failure", task.getException());
                }
            }
        });
    }

}