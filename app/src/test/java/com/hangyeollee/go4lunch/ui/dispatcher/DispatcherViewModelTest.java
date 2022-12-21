package com.hangyeollee.go4lunch.ui.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class DispatcherViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final FirebaseAuth firebaseAuth = Mockito.mock(FirebaseAuth.class);

    @Test
    public void currentUser_null_should_go_to_logIn_screen_view_action() {
        // Given
        doReturn(null).when(firebaseAuth).getCurrentUser();

        // When
        DispatcherViewAction result = new DispatcherViewModel(firebaseAuth).getViewActionSingleLiveEvent().getValue();

        // Then
        assertEquals(DispatcherViewAction.GO_TO_LOGIN_SCREEN, result);
    }

    @Test
    public void currentUser_logIn_successfully_should_go_to_mainHome_screen_view_action() {
        // Given
        FirebaseUser firebaseUser = Mockito.mock(FirebaseUser.class);
        doReturn(firebaseUser).when(firebaseAuth).getCurrentUser();

        // When
        DispatcherViewAction result = new DispatcherViewModel(firebaseAuth).getViewActionSingleLiveEvent().getValue();

        // Then
        assertEquals(DispatcherViewAction.GO_TO_MAIN_HOME_SCREEN, result);
    }
}