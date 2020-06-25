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
        Log.d(LogUtility.TAG(obj), "基盤名称" + Const.CSV_BREAK + Build.BOARD);
        logStr.append(GetTimestamp.getNowDate() + "基盤名称" + Const.CSV_BREAK + Build.BOARD + "\n");
        Log.d(LogUtility.TAG(obj), "ブートローダーバージョン" + Const.CSV_BREAK + Build.BOOTLOADER);
        logStr.append(GetTimestamp.getNowDate() + "ブートローダーバージョン" + Const.CSV_BREAK + Build.BOOTLOADER + "\n");
        Log.d(LogUtility.TAG(obj), "キャリア名" + Const.CSV_BREAK + Build.BRAND);
        logStr.append(GetTimestamp.getNowDate() + "キャリア名" + Const.CSV_BREAK + Build.BRAND + "\n");
        //Log.d(LogUtility.TAG(obj),"ネイティブコードの命令セット" + Const.CSV_BREAK + Build.CPU_ABI);
        //d(LogUtility.TAG(obj),"ネイティブコードの第2命令セット" + Const.CSV_BREAK + Build.CPU_ABI2);
        Log.d(LogUtility.TAG(obj), "端末名" + Const.CSV_BREAK + Build.DEVICE);
        logStr.append(GetTimestamp.getNowDate() + "端末名" + Const.CSV_BREAK + Build.DEVICE + "\n");


        //Log.d(LogUtility.TAG(obj),"ビルドID" + Const.CSV_BREAK + Build.DISPLAY);
        //Log.d(LogUtility.TAG(obj),"ビルド識別子" + Const.CSV_BREAK + Build.FINGERPRINT);
        Log.d(LogUtility.TAG(obj), "ハードウェア名" + Const.CSV_BREAK + Build.HARDWARE);
        logStr.append(GetTimestamp.getNowDate() + "ハードウェア名" + Const.CSV_BREAK + Build.HARDWARE + "\n");
        //Log.d(LogUtility.TAG(obj),"ホスト名" + Const.CSV_BREAK + Build.HOST);
        //.d(LogUtility.TAG(obj),"ID" + Const.CSV_BREAK + Build.ID);
        Log.d(LogUtility.TAG(obj), "メーカー名" + Const.CSV_BREAK + Build.MANUFACTURER);
        logStr.append(GetTimestamp.getNowDate() + "メーカー名" + Const.CSV_BREAK + Build.MANUFACTURER + "\n");
        //.d(LogUtility.TAG(obj),"モデル名" + Const.CSV_BREAK + Build.MODEL);
        Log.d(LogUtility.TAG(obj), "製品名" + Const.CSV_BREAK + Build.PRODUCT);
        logStr.append(GetTimestamp.getNowDate() + "製品名" + Const.CSV_BREAK + Build.PRODUCT + "\n");
        //Log.d(LogUtility.TAG(obj),"無線ファームウェアバージョン" + Const.CSV_BREAK + Build.RADIO);
        //.d(LogUtility.TAG(obj),"ビルドタグ名" + Const.CSV_BREAK + Build.TAGS);
        //Log.d(LogUtility.TAG(obj),"システム時刻" + Const.CSV_BREAK + Build.TIME);
        //Log.d(LogUtility.TAG(obj),"ビルドタイプ" + Const.CSV_BREAK + Build.TYPE);
        //Log.d(LogUtility.TAG(obj),"情報不明時の識別子" + Const.CSV_BREAK + Build.UNKNOWN);
        //Log.d(LogUtility.TAG(obj),"ユーザ情報" + Const.CSV_BREAK + Build.USER);
        //Log.d(LogUtility.TAG(obj),"ビルド種類" + Const.CSV_BREAK + Build.VERSION.CODENAME);
        //Log.d(LogUtility.TAG(obj),"ソースコード管理番号" + Const.CSV_BREAK + Build.VERSION.INCREMENTAL);
        Log.d(LogUtility.TAG(obj), "OSバージョン" + Const.CSV_BREAK + Build.VERSION.RELEASE);
        logStr.append(GetTimestamp.getNowDate() + "OSバージョン" + Const.CSV_BREAK + Build.VERSION.RELEASE + "\n");
        //Log.d(LogUtility.TAG(obj),"API番号" + Const.CSV_BREAK + Build.VERSION.SDK);

        //ディスプレイ情報
//        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        Log.d(LogUtility.TAG(obj), "ディスプレイ横" + Const.CSV_BREAK + displayMetrics.widthPixels);
        logStr.append(GetTimestamp.getNowDate() + "ディスプレイ横" + Const.CSV_BREAK + displayMetrics.widthPixels + "\n");
        Log.d(LogUtility.TAG(obj), "ディスプレイ縦" + Const.CSV_BREAK + displayMetrics.heightPixels);
        logStr.append(GetTimestamp.getNowDate() + "ディスプレイ縦" + Const.CSV_BREAK + displayMetrics.heightPixels + "\n");
        Log.d(LogUtility.TAG(obj), "横dpi" + Const.CSV_BREAK + displayMetrics.xdpi);
        logStr.append(GetTimestamp.getNowDate() + "横dpi" + Const.CSV_BREAK + displayMetrics.xdpi + "\n");
        Log.d(LogUtility.TAG(obj), "縦dpi" + Const.CSV_BREAK + displayMetrics.ydpi);
        logStr.append(GetTimestamp.getNowDate() + "縦dpi" + Const.CSV_BREAK + displayMetrics.ydpi + "\n");
        Log.d(LogUtility.TAG(obj), "論理密度" + Const.CSV_BREAK + displayMetrics.density);
        logStr.append(GetTimestamp.getNowDate() + "論理密度" + Const.CSV_BREAK + displayMetrics.density + "\n");
        Log.d(LogUtility.TAG(obj), "スケール密度" + Const.CSV_BREAK + displayMetrics.scaledDensity);
        logStr.append(GetTimestamp.getNowDate() + "スケール密度" + Const.CSV_BREAK + displayMetrics.scaledDensity + "\n");
        //Log.d(LogUtility.TAG(obj) ,"width" + String.valueOf(display.getWidth()));
        //Log.d(LogUtility.TAG(obj) ,"height" + String.valueOf(display.getHeight()));
        //Log.d(LogUtility.TAG(obj) ,"orientation" + String.valueOf(display.getOrientation()));
        Log.d(LogUtility.TAG(obj), "リフレッシュレート" + Const.CSV_BREAK + display.getRefreshRate());
        logStr.append(GetTimestamp.getNowDate() + "リフレッシュレート" + Const.CSV_BREAK + display.getRefreshRate() + "\n");
        Log.d(LogUtility.TAG(obj), "pixelFormat" + Const.CSV_BREAK + display.getPixelFormat());
        logStr.append(GetTimestamp.getNowDate() + "pixelFormat" + Const.CSV_BREAK + display.getPixelFormat() + "\n");

//        AudioManager audioManager = ( AudioManager ) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int amsa = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        int mamsa = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        Log.d(LogUtility.TAG(obj),"アラーム音量" + Const.CSV_BREAK + amsa + "/" + mamsa);
        logStr.append(GetTimestamp.getNowDate() + "アラーム音量" + Const.CSV_BREAK + "'" + amsa + "/" + mamsa + "\n");
        int amsm = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int mamsm = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d(LogUtility.TAG(obj),"音楽音量" + Const.CSV_BREAK +  amsm + "/" + mamsm);
        logStr.append(GetTimestamp.getNowDate() + "音楽音量" + Const.CSV_BREAK + "'" + amsm + "/" + mamsm + "\n");
        int amsn = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        int mamsn = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        Log.d(LogUtility.TAG(obj),"通知音量" + Const.CSV_BREAK +  amsn + "/" + mamsn);
        logStr.append(GetTimestamp.getNowDate() + "通知音量" + Const.CSV_BREAK + "'" + amsn + "/" + mamsn + "\n");
        int amsp = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        int mamsp = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        Log.d(LogUtility.TAG(obj),"着信音量" + Const.CSV_BREAK +  amsp + "/" + mamsp);
        logStr.append(GetTimestamp.getNowDate() + "着信音量" + Const.CSV_BREAK + "'" + amsp + "/" + mamsp + "\n");
        int amss = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        int mamss = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        Log.d(LogUtility.TAG(obj),"システムメッセージ音量" + Const.CSV_BREAK +  amss + "/" + mamss);
        logStr.append(GetTimestamp.getNowDate() + "システムメッセージ音量" + Const.CSV_BREAK + "'" + amss + "/" + mamss + "\n");
        int amsc = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        int mamsc = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        Log.d(LogUtility.TAG(obj),"通話音量" + Const.CSV_BREAK +  amsc + "/" + mamsc);
        logStr.append(GetTimestamp.getNowDate() + "通話音量" + Const.CSV_BREAK + "'" + amsc + "/" + mamsc + "\n");
        int amsd = audioManager.getStreamVolume(AudioManager.STREAM_DTMF);
        int mamsd = audioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);
        Log.d(LogUtility.TAG(obj),"ダイヤル音量" + Const.CSV_BREAK +  amsd + "/" + mamsd);
        logStr.append(GetTimestamp.getNowDate() + "ダイヤル音量" + Const.CSV_BREAK + "'" + amsd + "/" + mamsd + "\n");

        //ログ保存
        LogSave tsl = new LogSave();
        tsl.textsavelog(context, logStr);
        // ログバッファクリア
        logStr.delete(0, logStr.length());

    }


}
