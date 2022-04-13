package com.hangyeollee.go4lunch.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    private ActivityResultLauncher<Intent> mActivityResultLauncher;

    @Override
    ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                onGoogleLogInResult(result);
            }
        });

        binding.ButtonGoogleLogIn.setOnClickListener(view -> googleLogIn());

        Drawable facebookLogo = getDrawable(R.drawable.ic_facebook_logo);
        binding.ButtonFacebookLogIn.setCompoundDrawablesWithIntrinsicBounds(facebookLogo, null, null, null);
        // Todo : Facebook Login method onclick
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.constraintLayoutParentLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void googleLogIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mActivityResultLauncher.launch(mGoogleSignInClient.getSignInIntent());
    }

    private void onGoogleLogInResult(ActivityResult result) {
        Intent data = result.getData();
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Google sign in success
        if (result.getResultCode() == Activity.RESULT_OK) {
            showSnackBar(getString(R.string.connection_succeed));

            //ToDo : Start Second Activity. Should I use ActivityResultLauncher again to retrieve login account data ?

        } else {
            // ERRORS
            if (response == null) {
                showSnackBar(getString(R.string.error_authentication_canceled));
            } else if (response.getError() != null) {
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(getString(R.string.error_unknown_error));
                }
            }
        }
    }

    private void onFacebookLogInResult() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

}