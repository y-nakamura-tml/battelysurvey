package com.technomedialab.battelysurvey.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.technomedialab.battelysurvey.LogOutput;

import java.io.File;

public class HomeViewModel extends ViewModel  {

    private MutableLiveData<String> mText;
    private Context mContext;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}