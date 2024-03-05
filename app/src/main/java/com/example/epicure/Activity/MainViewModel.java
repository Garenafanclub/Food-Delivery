package com.example.epicure.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<Boolean> _isReady = new MutableLiveData<>(false);
    public LiveData<Boolean> isReady = _isReady;

    public MainViewModel() {
        // Simulate a 1-second delay for readiness
        new Thread(() -> {
            try {
                Thread.sleep(1000L);
                _isReady.postValue(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

