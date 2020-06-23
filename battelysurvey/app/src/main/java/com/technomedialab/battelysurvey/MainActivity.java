package com.technomedialab.battelysurvey;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends SensorActivity {

    private AppBarConfiguration appBarConfiguration;
    private Intent intent;
    private DisplayMetrics displayMetrics;
    private LogSave tsl;
    private StringBuilder logStr;
    private TextView vertextview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        if (appBarConfiguration == null) {
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                    .build();
        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        vertextview = (TextView)findViewById(R.id.VertextView);
        vertextview.setText("Ver." +Const.APP_VERSION +"(照度あり)");

        if (intent == null) {
            intent = new Intent(getApplication(), BackgroundService.class);
        }
        startService(intent);

        //スクリーンを常にON
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        WindowManager windowManager = getWindowManager();
        AudioManager audioManager = ( AudioManager ) getSystemService(Context.AUDIO_SERVICE);
        LogUtility.TerminalInfoLog(this,this,windowManager,audioManager);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LogUtility.TAG(this), "--------------アプリ終了　測定終了--------------");
    }


}
