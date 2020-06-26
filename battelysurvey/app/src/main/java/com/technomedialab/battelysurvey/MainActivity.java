package com.technomedialab.battelysurvey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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



    private static final String secretKey = "If you can dream it, you can do it.";// 暗号化/復号キー

    //暗号化文字列を生成する場合
    private String beginning = "ここに暗号化したいトークンを記述";// 暗号化対象文字列
    private String encValue;//暗号化文字列


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
        vertextview.setText("Ver." +Const.APP_VERSION);

        if (intent == null) {
            intent = new Intent(getApplication(), BackgroundService.class);
        }
        startService(intent);



        //スクリーンを常にON
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        WindowManager windowManager = getWindowManager();
        AudioManager audioManager = ( AudioManager ) getSystemService(Context.AUDIO_SERVICE);
        LogUtility.TerminalInfoLog(this,this,windowManager,audioManager);


        MainApplication mainApp = (MainApplication)getApplicationContext();

        //トークンを復号化
        try {
            CryptUtil cryptUtil = new CryptUtil();
            //トークンの復号化した文字列
            String encryptiontoken = "TMElT4zUFsY5RDTNvJYJ2ZBaA99pKLMn7I98VY3qS1l1Wo7eRcBAT+/9Dkv/tL0jjUJlaYo63ljW9b9FdRst5A==";
            String beginning = cryptUtil.getString(secretKey, encryptiontoken); // 復号
            //Log.d("復号化",beginning);
            mainApp.setToken(beginning);
        } catch (Exception e) {
            Log.e("復号化エラー", e.getMessage(), e);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LogUtility.TAG(this), "--------------アプリ終了　測定終了--------------");
    }


}
