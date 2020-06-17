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
        vertextview.setText("Ver.0.0.4");

        if (intent == null) {
            intent = new Intent(getApplication(), BackgroundService.class);
        }
        startService(intent);


        //スクリーンを常にON
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        logStr = new StringBuilder();

        Log.d(LogUtility.TAG(this), "--------------測定開始--------------");
        logStr.append("--------------アプリ起動　測定開始--------------" + "\n");
        //システム情報
        Log.d(LogUtility.TAG(this), "基盤名称:" + Build.BOARD);
        logStr.append("基盤名称:" + Build.BOARD + "\n");
        Log.d(LogUtility.TAG(this), "ブートローダーバージョン:" + Build.BOOTLOADER);
        logStr.append("ブートローダーバージョン:" + Build.BOOTLOADER + "\n");
        Log.d(LogUtility.TAG(this), "キャリア名:" + Build.BRAND);
        logStr.append("キャリア名:" + Build.BRAND + "\n");
        //Log.d(LogUtility.TAG(this),"ネイティブコードの命令セット:" + Build.CPU_ABI);
        //d(LogUtility.TAG(this),"ネイティブコードの第2命令セット:" + Build.CPU_ABI2);
        Log.d(LogUtility.TAG(this), "端末名:" + Build.DEVICE);
        logStr.append("端末名:" + Build.DEVICE + "\n");


        //Log.d(LogUtility.TAG(this),"ビルドID:" + Build.DISPLAY);
        //Log.d(LogUtility.TAG(this),"ビルド識別子:" + Build.FINGERPRINT);
        Log.d(LogUtility.TAG(this), "ハードウェア名:" + Build.HARDWARE);
        logStr.append("ハードウェア名:" + Build.HARDWARE + "\n");
        //Log.d(LogUtility.TAG(this),"ホスト名:" + Build.HOST);
        //.d(LogUtility.TAG(this),"ID:" + Build.ID);
        Log.d(LogUtility.TAG(this), "メーカー名:" + Build.MANUFACTURER);
        logStr.append("メーカー名:" + Build.MANUFACTURER + "\n");
        //.d(LogUtility.TAG(this),"モデル名:" + Build.MODEL);
        Log.d(LogUtility.TAG(this), "製品名:" + Build.PRODUCT);
        logStr.append("製品名:" + Build.PRODUCT + "\n");
        //Log.d(LogUtility.TAG(this),"無線ファームウェアバージョン:" + Build.RADIO);
        //.d(LogUtility.TAG(this),"ビルドタグ名:" + Build.TAGS);
        //Log.d(LogUtility.TAG(this),"システム時刻:" + Build.TIME);
        //Log.d(LogUtility.TAG(this),"ビルドタイプ:" + Build.TYPE);
        //Log.d(LogUtility.TAG(this),"情報不明時の識別子:" + Build.UNKNOWN);
        //Log.d(LogUtility.TAG(this),"ユーザ情報:" + Build.USER);
        //Log.d(LogUtility.TAG(this),"ビルド種類:" + Build.VERSION.CODENAME);
        //Log.d(LogUtility.TAG(this),"ソースコード管理番号:" + Build.VERSION.INCREMENTAL);
        Log.d(LogUtility.TAG(this), "OSバージョン:" + Build.VERSION.RELEASE);
        logStr.append("OSバージョン:" + Build.VERSION.RELEASE + "\n");
        //Log.d(LogUtility.TAG(this),"API番号:" + Build.VERSION.SDK);

        //ディスプレイ情報
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        if (displayMetrics == null) {
            displayMetrics = new DisplayMetrics();
        }
        display.getMetrics(displayMetrics);

        Log.d(LogUtility.TAG(this), "ディスプレイ横" + String.valueOf(displayMetrics.widthPixels));
        logStr.append("ディスプレイ横" + String.valueOf(displayMetrics.widthPixels) + "\n");
        Log.d(LogUtility.TAG(this), "ディスプレイ縦" + String.valueOf(displayMetrics.heightPixels));
        logStr.append("ディスプレイ縦" + String.valueOf(displayMetrics.heightPixels) + "\n");
        Log.d(LogUtility.TAG(this), "横dpi" + String.valueOf(displayMetrics.xdpi));
        logStr.append("横dpi" + String.valueOf(displayMetrics.xdpi) + "\n");
        Log.d(LogUtility.TAG(this), "縦dpi" + String.valueOf(displayMetrics.ydpi));
        logStr.append("縦dpi" + String.valueOf(displayMetrics.ydpi) + "\n");
        Log.d(LogUtility.TAG(this), "論理密度" + String.valueOf(displayMetrics.density));
        logStr.append("論理密度" + String.valueOf(displayMetrics.density) + "\n");
        Log.d(LogUtility.TAG(this), "スケール密度" + String.valueOf(displayMetrics.scaledDensity));
        logStr.append("スケール密度" + String.valueOf(displayMetrics.scaledDensity) + "\n");
        //Log.d(LogUtility.TAG(this) ,"width" + String.valueOf(display.getWidth()));
        //Log.d(LogUtility.TAG(this) ,"height" + String.valueOf(display.getHeight()));
        //Log.d(LogUtility.TAG(this) ,"orientation" + String.valueOf(display.getOrientation()));
        Log.d(LogUtility.TAG(this), "リフレッシュレート" + String.valueOf(display.getRefreshRate()));
        logStr.append("リフレッシュレート" + String.valueOf(display.getRefreshRate()) + "\n");
        Log.d(LogUtility.TAG(this), "pixelFormat" + String.valueOf(display.getPixelFormat()));
        logStr.append("pixelFormat" + String.valueOf(display.getPixelFormat()) + "\n");

        AudioManager audioManager = ( AudioManager ) getSystemService(Context.AUDIO_SERVICE);
        int amsa = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        int mamsa = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        Log.d("アラーム音量:", String.valueOf(amsa) + "/" + String.valueOf(mamsa));
        logStr.append("アラーム音量:" + String.valueOf(amsa) + "/" + String.valueOf(mamsa) + "\n");
        int amsm = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int mamsm = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d("音楽音量:", String.valueOf(amsm) + "/" + String.valueOf(mamsm));
        logStr.append("音楽音量:" + String.valueOf(amsm) + "/" + String.valueOf(mamsm) + "\n");
        int amsn = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        int mamsn = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        Log.d("通知音量:", String.valueOf(amsn) + "/" + String.valueOf(mamsn));
        logStr.append("通知音量:" + String.valueOf(amsn) + "/" + String.valueOf(mamsn) + "\n");
        int amsp = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        int mamsp = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        Log.d("着信音量:", String.valueOf(amsp) + "/" + String.valueOf(mamsp));
        logStr.append("着信音量:" + String.valueOf(amsp) + "/" + String.valueOf(mamsp) + "\n");
        int amss = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        int mamss = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        Log.d("システムメッセージ音量:", String.valueOf(amss) + "/" + String.valueOf(mamss));
        logStr.append("システムメッセージ音量:" + String.valueOf(amss) + "/" + String.valueOf(mamss) + "\n");
        int amsc = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        int mamsc = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        Log.d("通話音量:", String.valueOf(amsc) + "/" + String.valueOf(mamsc));
        logStr.append("通話音量:" + String.valueOf(amsc) + "/" + String.valueOf(mamsc) + "\n");
        int amsd = audioManager.getStreamVolume(AudioManager.STREAM_DTMF);
        int mamsd = audioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);
        Log.d("ダイヤル音量:", String.valueOf(amsd) + "/" + String.valueOf(mamsd));
        logStr.append("ダイヤル音量:" + String.valueOf(amsd) + "/" + String.valueOf(mamsd) + "\n");

        //ログ保存
        if (tsl == null) {
            tsl = new LogSave();
        }
        tsl.textsavelog(this, logStr);
        // ログバッファクリア
        logStr.delete(0, logStr.length());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LogUtility.TAG(this), "--------------アプリ終了　測定終了--------------");
    }


}
