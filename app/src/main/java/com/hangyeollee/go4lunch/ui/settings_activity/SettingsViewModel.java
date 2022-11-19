package com.hangyeollee.go4lunch.ui.settings_activity;

import static com.hangyeollee.go4lunch.utils.UtilBox.resourceToUri;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.repository.SettingRepository;
import com.hangyeollee.go4lunch.utils.NotificationWorker;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class SettingsViewModel extends ViewModel {

    private final Application context;
    private final FirebaseAuth firebaseAuth;
    private final SettingRepository settingRepository;
    private final Clock clock;

    private final WorkManager workManager;

    private static final String REMINDER_REQUEST = "REMINDER_REQUEST";

    private final MediatorLiveData<SettingsViewState> mediatorLiveData = new MediatorLiveData<>();

    public SettingsViewModel(
            Application context,
            FirebaseAuth firebaseAuth,
            SettingRepository settingRepository,
            Clock clock
    ) {
        this.context = context;
        this.firebaseAuth = firebaseAuth;
        this.settingRepository = settingRepository;
        this.clock = clock;

        this.workManager = WorkManager.getInstance(context);

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
        settingRepository.setNotificationEnable(enable);

        if (enable) {
            LocalDateTime currentDate = LocalDateTime.now(clock);
            LocalDateTime thisNoon = currentDate.with(LocalTime.of(12, 0));

            if (currentDate.isAfter(thisNoon)) {
                thisNoon = thisNoon.plusDays(1);
            }

            long timeLeft = ChronoUnit
                    .SECONDS
                    .between(currentDate, thisNoon);

            PeriodicWorkRequest workRequest = new PeriodicWorkRequest
                    .Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                    .setInitialDelay(timeLeft, TimeUnit.MILLISECONDS)
                    .build();

            workManager.enqueueUniquePeriodicWork(
                    REMINDER_REQUEST,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    workRequest);

        } else {
            workManager.cancelAllWork();
        }

    }
}
