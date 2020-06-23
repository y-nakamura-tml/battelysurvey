package com.technomedialab.battelysurvey;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class LogUtility {
    private static final int MAX_TAG_SIZE = 23;
    // Objのクラス名をMAX_TAG_SIZEの文字数以内で出力してくれる。
    public static String TAG(Object obj) {
        String objName = obj.getClass().getSimpleName();
        return objName.length() > MAX_TAG_SIZE ? objName.substring(0, MAX_TAG_SIZE) : objName;
    }


    public static void TerminalInfoLog(Object obj, Context context, WindowManager windowManager,AudioManager audioManager){

        StringBuilder logStr = new StringBuilder();


        Log.d(LogUtility.TAG(obj), "--------------測定開始--------------");
        logStr.append("--------------アプリ起動　測定開始--------------" + "\n");
        //システム情報
        Log.d(LogUtility.TAG(obj), "基盤名称:" + Build.BOARD);
        logStr.append("基盤名称:" + Build.BOARD + "\n");
        Log.d(LogUtility.TAG(obj), "ブートローダーバージョン:" + Build.BOOTLOADER);
        logStr.append("ブートローダーバージョン:" + Build.BOOTLOADER + "\n");
        Log.d(LogUtility.TAG(obj), "キャリア名:" + Build.BRAND);
        logStr.append("キャリア名:" + Build.BRAND + "\n");
        //Log.d(LogUtility.TAG(obj),"ネイティブコードの命令セット:" + Build.CPU_ABI);
        //d(LogUtility.TAG(obj),"ネイティブコードの第2命令セット:" + Build.CPU_ABI2);
        Log.d(LogUtility.TAG(obj), "端末名:" + Build.DEVICE);
        logStr.append("端末名:" + Build.DEVICE + "\n");


        //Log.d(LogUtility.TAG(obj),"ビルドID:" + Build.DISPLAY);
        //Log.d(LogUtility.TAG(obj),"ビルド識別子:" + Build.FINGERPRINT);
        Log.d(LogUtility.TAG(obj), "ハードウェア名:" + Build.HARDWARE);
        logStr.append("ハードウェア名:" + Build.HARDWARE + "\n");
        //Log.d(LogUtility.TAG(obj),"ホスト名:" + Build.HOST);
        //.d(LogUtility.TAG(obj),"ID:" + Build.ID);
        Log.d(LogUtility.TAG(obj), "メーカー名:" + Build.MANUFACTURER);
        logStr.append("メーカー名:" + Build.MANUFACTURER + "\n");
        //.d(LogUtility.TAG(obj),"モデル名:" + Build.MODEL);
        Log.d(LogUtility.TAG(obj), "製品名:" + Build.PRODUCT);
        logStr.append("製品名:" + Build.PRODUCT + "\n");
        //Log.d(LogUtility.TAG(obj),"無線ファームウェアバージョン:" + Build.RADIO);
        //.d(LogUtility.TAG(obj),"ビルドタグ名:" + Build.TAGS);
        //Log.d(LogUtility.TAG(obj),"システム時刻:" + Build.TIME);
        //Log.d(LogUtility.TAG(obj),"ビルドタイプ:" + Build.TYPE);
        //Log.d(LogUtility.TAG(obj),"情報不明時の識別子:" + Build.UNKNOWN);
        //Log.d(LogUtility.TAG(obj),"ユーザ情報:" + Build.USER);
        //Log.d(LogUtility.TAG(obj),"ビルド種類:" + Build.VERSION.CODENAME);
        //Log.d(LogUtility.TAG(obj),"ソースコード管理番号:" + Build.VERSION.INCREMENTAL);
        Log.d(LogUtility.TAG(obj), "OSバージョン:" + Build.VERSION.RELEASE);
        logStr.append("OSバージョン:" + Build.VERSION.RELEASE + "\n");
        //Log.d(LogUtility.TAG(obj),"API番号:" + Build.VERSION.SDK);

        //ディスプレイ情報
//        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        Log.d(LogUtility.TAG(obj), "ディスプレイ横" + String.valueOf(displayMetrics.widthPixels));
        logStr.append("ディスプレイ横" + String.valueOf(displayMetrics.widthPixels) + "\n");
        Log.d(LogUtility.TAG(obj), "ディスプレイ縦" + String.valueOf(displayMetrics.heightPixels));
        logStr.append("ディスプレイ縦" + String.valueOf(displayMetrics.heightPixels) + "\n");
        Log.d(LogUtility.TAG(obj), "横dpi" + String.valueOf(displayMetrics.xdpi));
        logStr.append("横dpi" + String.valueOf(displayMetrics.xdpi) + "\n");
        Log.d(LogUtility.TAG(obj), "縦dpi" + String.valueOf(displayMetrics.ydpi));
        logStr.append("縦dpi" + String.valueOf(displayMetrics.ydpi) + "\n");
        Log.d(LogUtility.TAG(obj), "論理密度" + String.valueOf(displayMetrics.density));
        logStr.append("論理密度" + String.valueOf(displayMetrics.density) + "\n");
        Log.d(LogUtility.TAG(obj), "スケール密度" + String.valueOf(displayMetrics.scaledDensity));
        logStr.append("スケール密度" + String.valueOf(displayMetrics.scaledDensity) + "\n");
        //Log.d(LogUtility.TAG(obj) ,"width" + String.valueOf(display.getWidth()));
        //Log.d(LogUtility.TAG(obj) ,"height" + String.valueOf(display.getHeight()));
        //Log.d(LogUtility.TAG(obj) ,"orientation" + String.valueOf(display.getOrientation()));
        Log.d(LogUtility.TAG(obj), "リフレッシュレート" + String.valueOf(display.getRefreshRate()));
        logStr.append("リフレッシュレート" + String.valueOf(display.getRefreshRate()) + "\n");
        Log.d(LogUtility.TAG(obj), "pixelFormat" + String.valueOf(display.getPixelFormat()));
        logStr.append("pixelFormat" + String.valueOf(display.getPixelFormat()) + "\n");

//        AudioManager audioManager = ( AudioManager ) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int amsa = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        int mamsa = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        Log.d(LogUtility.TAG(obj),"アラーム音量:" + String.valueOf(amsa) + "/" + String.valueOf(mamsa));
        logStr.append("アラーム音量:" + String.valueOf(amsa) + "/" + String.valueOf(mamsa) + "\n");
        int amsm = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int mamsm = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d(LogUtility.TAG(obj),"音楽音量:" + String.valueOf(amsm) + "/" + String.valueOf(mamsm));
        logStr.append("音楽音量:" + String.valueOf(amsm) + "/" + String.valueOf(mamsm) + "\n");
        int amsn = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        int mamsn = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        Log.d(LogUtility.TAG(obj),"通知音量:" + String.valueOf(amsn) + "/" + String.valueOf(mamsn));
        logStr.append("通知音量:" + String.valueOf(amsn) + "/" + String.valueOf(mamsn) + "\n");
        int amsp = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        int mamsp = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        Log.d(LogUtility.TAG(obj),"着信音量:" +  String.valueOf(amsp) + "/" + String.valueOf(mamsp));
        logStr.append("着信音量:" + String.valueOf(amsp) + "/" + String.valueOf(mamsp) + "\n");
        int amss = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        int mamss = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        Log.d(LogUtility.TAG(obj),"システムメッセージ音量:" + String.valueOf(amss) + "/" + String.valueOf(mamss));
        logStr.append("システムメッセージ音量:" + String.valueOf(amss) + "/" + String.valueOf(mamss) + "\n");
        int amsc = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        int mamsc = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        Log.d(LogUtility.TAG(obj),"通話音量:" + String.valueOf(amsc) + "/" + String.valueOf(mamsc));
        logStr.append("通話音量:" + String.valueOf(amsc) + "/" + String.valueOf(mamsc) + "\n");
        int amsd = audioManager.getStreamVolume(AudioManager.STREAM_DTMF);
        int mamsd = audioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);
        Log.d(LogUtility.TAG(obj),"ダイヤル音量:" + String.valueOf(amsd) + "/" + String.valueOf(mamsd));
        logStr.append("ダイヤル音量:" + String.valueOf(amsd) + "/" + String.valueOf(mamsd) + "\n");

        //ログ保存
        LogSave tsl = new LogSave();
        tsl.textsavelog(context, logStr);
        // ログバッファクリア
        logStr.delete(0, logStr.length());

    }


}
