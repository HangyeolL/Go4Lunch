package com.hangyeollee.go4lunch.view.SettingsActivity;

import static com.hangyeollee.go4lunch.utils.resourceToUri.resourceToUri;

import android.app.Application;
import android.content.SharedPreferences;
import android.view.animation.Transformation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.facebook.internal.Mutable;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.utils.MySharedPreferenceUtil;

public class SettingsActivityViewModel extends ViewModel {
    //TODO how to handle sharedPreferences with LiveData to observe ?
    private final Application context;
    private final FirebaseRepository firebaseRepository;
    private final MediatorLiveData<SettingsActivityViewState> mediatorLiveData = new MediatorLiveData<>();

    public SettingsActivityViewModel(
            Application context,
            FirebaseRepository firebaseRepository
    ) {
        this.context = context;
        this.firebaseRepository = firebaseRepository;

        SharedPreferences sharedPref = new MySharedPreferenceUtil(this.context).getInstanceOfSharedPref();

    }

    public LiveData<SettingsActivityViewState> viewStateLiveData() {
        return mediatorLiveData;
    }

    private void combine() {

        String userName;
        String userEmail;
        String userPhotoUrl;

        UserInfo firebaseUserInfo = firebaseRepository.getCurrentUser().getProviderData().get(1);

        userName = firebaseUserInfo.getDisplayName();

        if (firebaseUserInfo.getEmail() == null || firebaseUserInfo.getEmail() == "") {
            userEmail = context.getString(R.string.email_unavailable);
        } else {
            userEmail = firebaseUserInfo.getEmail();
        }

        if (firebaseUserInfo.getPhotoUrl() == null) {
            userPhotoUrl = resourceToUri(context, R.drawable.ic_baseline_person_outline_24);
        } else {
            userPhotoUrl = firebaseUserInfo.getPhotoUrl().toString();
        }

//        SettingsActivityViewState viewState = new SettingsActivityViewState(
//                userPhotoUrl,
//                userName,
//                userEmail,
//
//                );
    }

}
