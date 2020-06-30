package com.technomedialab.battelysurvey.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mTextFilePath;

    public DashboardViewModel() {
        mTextFilePath = new MutableLiveData<>();
        mTextFilePath.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mTextFilePath;
    }
    public LiveData<String> setText(String value){
        mTextFilePath.setValue(value);
        return mTextFilePath;
    }
}