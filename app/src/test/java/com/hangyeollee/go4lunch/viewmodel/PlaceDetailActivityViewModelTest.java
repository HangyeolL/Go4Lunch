package com.hangyeollee.go4lunch.viewmodel;

import com.hangyeollee.go4lunch.repository.FirebaseRepository;
import com.hangyeollee.go4lunch.repository.PlaceDetailDataRepository;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlaceDetailActivityViewModelTest {

    private PlaceDetailDataRepository mPlaceDetailDataRepository = Mockito.mock(PlaceDetailDataRepository.class);
    private FirebaseRepository mFirebaseRepository = Mockito.mock(FirebaseRepository.class);



}
