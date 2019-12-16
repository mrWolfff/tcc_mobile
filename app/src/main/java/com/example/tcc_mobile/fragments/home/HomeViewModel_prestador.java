package com.example.tcc_mobile.fragments.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel_prestador extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel_prestador() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}