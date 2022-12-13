package com.hangyeollee.go4lunch.ui.settings;

import static com.hangyeollee.go4lunch.utils.UtilBox.resourceToUri;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.repository.SettingRepository;

public class SettingsViewModel extends ViewModel {

    private final Application context;
    private final FirebaseAuth firebaseAuth;
    private final SettingRepository settingRepository;

    private final MediatorLiveData<SettingsViewState> mediatorLiveData = new MediatorLiveData<>();

    public SettingsViewModel(
            Application context,
            FirebaseAuth firebaseAuth,
            SettingRepository settingRepository
    ) {
        this.context = context;
        this.firebaseAuth = firebaseAuth;
        this.settingRepository = settingRepository;

        LiveData<Boolean> isNotificationEnabledLiveData = settingRepository.getIsNotificationEnabledLiveData();

        //TODO why this method wont be called if i dont put initial value?
        mediatorLiveData.addSource(isNotificationEnabledLiveData,
                isNotificationEnabled -> combine(isNotificationEnabled)
        );

    }

    private void combine(@Nullable Boolean isNotificationEnabled) {
        if (isNotificationEnabled == null) {
            return;
        }

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

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

        SettingsViewState viewState = new SettingsViewState(userPhotoUrl, userName, userEmail, isNotificationEnabled);

        mediatorLiveData.setValue(viewState);
    }

    /**
     * GETTERS
     */

    public LiveData<SettingsViewState> getViewStateLiveData() {
        return mediatorLiveData;
    }

    /**
     * EVENTS
     */

    public void onSwitchClicked(boolean enable) {
        settingRepository.setNotificationEnabled(enable);

    }
}
