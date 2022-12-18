package com.hangyeollee.go4lunch.ui.settings;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import android.app.Application;
import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hangyeollee.go4lunch.R;
import com.hangyeollee.go4lunch.data.repository.SettingRepository;
import com.hangyeollee.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class SettingsViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Application application;
    private FirebaseAuth firebaseAuth;
    private SettingRepository settingRepository;

    private FirebaseUser firebaseUser;

    private SettingsViewModel viewModel;

    private final MutableLiveData<Boolean> isNotificationEnabledMutableLiveData = new MutableLiveData<>();

    @Before
    public void setUp() {
        application = Mockito.mock(Application.class);
        firebaseAuth = Mockito.mock(FirebaseAuth.class);
        settingRepository = Mockito.mock(SettingRepository.class);

        isNotificationEnabledMutableLiveData.setValue(false);
        doReturn(isNotificationEnabledMutableLiveData).when(settingRepository).getIsNotificationEnabledLiveData();

        firebaseUser = Mockito.mock(FirebaseUser.class);
        Uri uri = Mockito.mock(Uri.class);

        doReturn(firebaseUser).when(firebaseAuth).getCurrentUser();
        doReturn("userId").when(firebaseUser).getUid();
        doReturn("userName").when(firebaseUser).getDisplayName();
        doReturn("userEmail").when(firebaseUser).getEmail();
        doReturn(uri).when(firebaseUser).getPhotoUrl();
        doReturn("userPhotoUrl").when(uri).toString();

        doReturn("Email unavailable").when(application).getString(R.string.email_unavailable);

        viewModel = new SettingsViewModel(application, firebaseAuth, settingRepository);
    }

    @Test
    public void nominal_case() {
        //WHEN
        SettingsViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getViewStateLiveData());

        //THEN
        SettingsViewState expectedViewState = new SettingsViewState(
            "userPhotoUrl", "userName", "userEmail", false
        );
        assertEquals(expectedViewState, viewState);
    }

    @Test
    public void email_unavailable() {
        //GIVEN
        doReturn(null).when(firebaseUser).getEmail();

        //WHEN
        SettingsViewState viewState = LiveDataTestUtils.getValueForTesting(viewModel.getViewStateLiveData());

        //THEN
        SettingsViewState expectedViewState = new SettingsViewState(
            "userPhotoUrl", "userName", "Email unavailable", false
        );
        assertEquals(expectedViewState, viewState);
    }

}