package com.hangyeollee.go4lunch.ui.login;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mockito;

public class LogInViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Application application;
    private FirebaseAuth firebaseAuth;

    private LogInViewModel viewModel;

    @Before
    public void setUp() {
        application = Mockito.mock(Application.class);
        firebaseAuth = Mockito.mock(FirebaseAuth.class);

        viewModel = new LogInViewModel(application, firebaseAuth);
    }

}