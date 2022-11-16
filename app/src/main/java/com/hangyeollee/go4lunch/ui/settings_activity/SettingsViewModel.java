package com.hangyeollee.go4lunch.ui.settings_activity;

import static com.hangyeollee.go4lunch.utils.ResourceToUri.resourceToUri;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.data.repository.SettingRepository;
import com.hangyeollee.go4lunch.utils.MySharedPreferenceUtil;

public class SettingsViewModel extends ViewModel {
    ///TODO how to handle sharedPreferences with LiveData to observe ?
    private final Application context;
    private final FirebaseRepository firebaseRepository;
    private final MediatorLiveData<SettingsViewState> mediatorLiveData = new MediatorLiveData<>();

    public SettingsViewModel(
            Application context,
            FirebaseRepository firebaseRepository,
            SettingRepository settingRepository
    ) {
        this.context = context;
        this.firebaseRepository = firebaseRepository;

        LiveData<Boolean> areNotificationEnabledLiveData = settingRepository.getIsNotificationEnabledLiveData();

        //TODO why this method wont be called if i dont put initial value?
        mediatorLiveData.addSource(areNotificationEnabledLiveData, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean areNotificationEnabled) {
                combine(areNotificationEnabled);
            }
        });

    }

    public LiveData<SettingsViewState> viewStateLiveData() {
        return mediatorLiveData;
    }

    private void combine(@Nullable Boolean areNotificationEnabled) {
        if (areNotificationEnabled == null) {
            return;
        }

        FirebaseUser firebaseUser = firebaseRepository.getCurrentUser();

        final String userName = firebaseUser.getDisplayName();
        final String userEmail;
        String userPhotoUrl;

        if (firebaseUser.getEmail() == null || firebaseUser.getEmail().isEmpty()) {
            userEmail = context.getString(R.string.email_unavailable);
        } else {
            userEmail = firebaseUser.getEmail();
        }

        if (firebaseUser.getPhotoUrl() == null) {
            userPhotoUrl = resourceToUri(context, R.drawable.ic_baseline_person_outline_24);
        } else {
            userPhotoUrl = firebaseUser.getPhotoUrl().toString();
        }

//        SettingsViewState viewState = new SettingsViewState(
//                userPhotoUrl,
//                userName,
//                userEmail,
//
//                );
    }

}
