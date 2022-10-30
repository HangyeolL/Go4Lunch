package com.hangyeollee.go4lunch.view.DispatcherActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.hangyeollee.go4lunch.utils.SingleLiveEvent;

public class DispatcherActivityViewModel extends ViewModel {

    @NonNull
    private final SingleLiveEvent<DispatcherActivityViewAction> viewActionSingleLiveEvent = new SingleLiveEvent<>();

    public DispatcherActivityViewModel(@NonNull FirebaseAuth firebaseAuth) {
        // User not connected
        if (firebaseAuth.getCurrentUser() == null) {
            viewActionSingleLiveEvent.setValue(DispatcherActivityViewAction.GO_TO_LOGIN_SCREEN);
        } else {
            viewActionSingleLiveEvent.setValue(DispatcherActivityViewAction.GO_TO_MAIN_HOME_SCREEN);
        }
    }

    @NonNull
    public SingleLiveEvent<DispatcherActivityViewAction> getViewActionSingleLiveEvent() {
        return viewActionSingleLiveEvent;
    }
}
