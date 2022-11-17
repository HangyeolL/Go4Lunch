package com.hangyeollee.go4lunch.ui.logIn_activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.ui.main_home_activity.MainHomeActivity;
import com.hangyeollee.go4lunch.utils.SingleLiveEvent;

import java.util.Collections;

public class LogInViewModel extends ViewModel {

    private final Application context;
    private final FirebaseAuth firebaseAuth;

    private final CallbackManager callbackManager = CallbackManager.Factory.create();

    private final SingleLiveEvent<Intent> intentSingleLiveEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> stringSingleLiveEvent = new SingleLiveEvent<>();

    public LogInViewModel(
            Application context,
            FirebaseAuth firebaseAuth
    ) {
        this.context = context;
        this.firebaseAuth = firebaseAuth;
    }

    /**
     * GETTERS
     */

    public SingleLiveEvent<Intent> getIntentSingleLiveEvent() {
        return intentSingleLiveEvent;
    }

    public SingleLiveEvent<String> getStringSingleLiveEvent() {
        return stringSingleLiveEvent;
    }

    /**
     * EVENTS
     */

    public Intent onButtonGoogleLogInClicked() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(context, gso);

        return gsc.getSignInIntent();
    }

    public void onButtonFacebookLogInClicked(ActivityResultRegistryOwner activityResultRegistryOwner) {
        LoginManager.getInstance()
                .logInWithReadPermissions(
                        activityResultRegistryOwner,
                        callbackManager,
                        Collections.singletonList("public_profile")
                );
    }

    public void onGoogleLogInResult(ActivityResult result){
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            // Get signed accounts from the google log in activity
            Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = googleSignInAccountTask.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth
                        .signInWithCredential(credential).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("Hangyeol", "firebaseAuthWithGoogle:success");
                                        intentSingleLiveEvent.setValue(MainHomeActivity.navigate(context));
                                    } else {
                                        Log.d("Hangyeol", "firebaseAuthWithGoogle:failure/");
                                        task.getException().printStackTrace();
                                        stringSingleLiveEvent.setValue(context.getString(R.string.google_log_in_failed));
                                    }
                                }
                        );
            } catch (ApiException e) {
                e.printStackTrace();
            }

        }

        if (result.getResultCode() == Activity.RESULT_CANCELED) {
            stringSingleLiveEvent.setValue(context.getString(R.string.google_log_in_canceled));
        }
    }

    public void onFacebookLogInResult() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                firebaseAuth
                        .signInWithCredential(credential)
                        .addOnCompleteListener(
                                task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("Hangyeol", "facebookLogin:success");
                                        intentSingleLiveEvent.setValue(MainHomeActivity.navigate(context));
                                    } else {
                                        Log.d("Hangyeol", "facebookLogin:failure");
                                        task.getException().printStackTrace();
                                    }
                                }
                        );
            }

            @Override
            public void onCancel() {
                stringSingleLiveEvent.setValue(context.getString(R.string.facebook_log_in_canceled));
            }

            @Override
            public void onError(@NonNull FacebookException exception) {
                Log.e("Hangyeol", "FacebookLogIn:failed with an error", exception);
                exception.printStackTrace();
                stringSingleLiveEvent.setValue(context.getString(R.string.error_occurred));
            }
        });
    }


}

