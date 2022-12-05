package com.hangyeollee.go4lunch.ui.dispatcher;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.hangyeollee.go4lunch.utils.SingleLiveEvent;

public class DispatcherViewModel extends ViewModel {

    @NonNull
    private final SingleLiveEvent<DispatcherViewAction> viewActionSingleLiveEvent = new SingleLiveEvent<>();

    public DispatcherViewModel(@NonNull FirebaseAuth firebaseAuth) {
        // User not connected
        if (firebaseAuth.getCurrentUser() == null) {
            viewActionSingleLiveEvent.setValue(DispatcherViewAction.GO_TO_LOGIN_SCREEN);
        } else {
            viewActionSingleLiveEvent.setValue(DispatcherViewAction.GO_TO_MAIN_HOME_SCREEN);
        }
    }

    @NonNull
    public SingleLiveEvent<DispatcherViewAction> getViewActionSingleLiveEvent() {
        return viewActionSingleLiveEvent;
    }
}
