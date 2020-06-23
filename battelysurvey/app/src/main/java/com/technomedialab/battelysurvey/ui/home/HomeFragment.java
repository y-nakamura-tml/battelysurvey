package com.technomedialab.battelysurvey.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.technomedialab.battelysurvey.LogOutput;
import com.technomedialab.battelysurvey.LogSave;
import com.technomedialab.battelysurvey.LogUtility;
import com.technomedialab.battelysurvey.MainActivity;
import com.technomedialab.battelysurvey.MainApplication;
import com.technomedialab.battelysurvey.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class HomeFragment extends Fragment implements LogOutput.CallBackTask{

    private DisplayMetrics displayMetrics;
    private LogSave tsl;
    private HomeViewModel homeViewModel;

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }

    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TextViewをひも付けます
        final TextView mTextView = view.findViewById(R.id.text_home);

        // ログ削除ボタン押下時
        view.findViewById(R.id.l_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClickEvent(v);
            }
        });

        // ログ出力ボタン押下時
        view.findViewById(R.id.l_logOutput).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputClickEvent(v);
            }
        });



    }

    private void deleteClickEvent(View v) {

        AlertDialog.Builder alertDelete = new AlertDialog.Builder(v.getContext());
        alertDelete
                .setTitle("ログ削除")
                .setMessage("ログファイルの削除を行いますか？\n実行すると元に戻せません！")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK押下時
                        LogDelete(getContext());
                        startLog(getContext());
                        new AlertDialog.Builder(getActivity())
                                .setTitle("削除完了")
                                .setMessage("ログファイルの削除が完了しました")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void outputClickEvent(View v) {
        AlertDialog.Builder alertDelete = new AlertDialog.Builder(v.getContext());
        alertDelete
                .setTitle("ログ送信")
                .setMessage("ログファイルをSlackに送信しますか？")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK押下時
                        logOutput(getContext());

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    //ログ削除処理
    public void LogDelete(Context context){
        final File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        System.out.println(path.toString());

        File[] files = path.listFiles();
        FileDelete(files);
    }


    public void FileDelete(File[] files) {

        if(files == null) {
            System.out.println("配下にファイルが存在しない");
        }
        //for文でファイルリスト分ループする
        for(int i=0; i<files.length; i++) {

            //ファイルの存在確認
            if(files[i].exists() == false) {
                continue;
                //ファイルの場合は再帰的に自身を呼び出して削除する
            } else if(files[i].isFile()) {
                files[i].delete();
                System.out.println("削除ファイル：" + files[i].toString());

            } else if(files[i].isDirectory()) {
                File[] dirFiles = files[i].listFiles();

                if(dirFiles == null) {
                    System.out.println("配下にファイルが存在しない");
                }

                System.out.println("■ディレクトリ内のファイル削除:" + dirFiles.toString());
                FileDelete(dirFiles);

                //ディレクトリを削除する
                if (files[i].delete()) {
                    System.out.println("ディレクトリ削除成功");
                    System.out.println("削除フォルダ：" + files[i].toString());
                }else{
                    System.out.println("ディレクトリ削除失敗");
                }
            }
        }


    }


    //ログ送信処理
    public void logOutput(Context context) {
        final String[] resultData = {""};

        //zipファイル作成
        final String zipFile = zipCompression(getContext());

        //フィルタを作成する
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File file, String str){

                //指定文字列でフィルタする
                //indexOfは指定した文字列が見つからなかったら-1を返す
                if (str.indexOf(zipFile)  != -1){
                    return true;
                }else{
                    return false;
                }
            }
        };

        MainApplication mainApp = (MainApplication)getActivity().getApplicationContext();
        LogOutput postTask = new LogOutput(mainApp.getToken());


        final File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File[] files = path.listFiles(filter);

        if(files == null) {
            System.out.println("配下にファイルが存在しない");
        }else{
            // コールバック設定
            postTask.setOnCallBack(this);
            postTask.execute(files);
        }
    }

    @Override
    public void CallBack(List result) {

        String Title = "";
        String Message = "";
        String errStr = "";

        if (result.size() == 0) {
            Title = "送信完了";
            Message = "ログファイルの送信が完了しました";
        } else {

            for (int i = 0; i < result.size(); i++) {
                errStr = errStr + result.get(i).toString() + "\r\n";
            }

            Title = "送信失敗";
            Message = "送信に失敗したログファイルがあります" + "\r\n" + errStr;
        }

        new AlertDialog.Builder(getContext())
                .setTitle(Title)
                .setMessage(Message)
                .setPositiveButton("OK", null)
                .show();
    }

    public String zipCompression(Context context){

        // 今日の日付
        final Date date = new Date();
        //日毎にlogファイルを分ける
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        String outputFile = Build.DEVICE + "_" +  dateFormat.format(date) + ".zip";
        String outputFilepath = path + "/" + outputFile;

        File[] inputFiles = path.listFiles();
        for (int i=0;i<inputFiles.length;i++){
            if (outputFile.equals(inputFiles[i].getName())){
                inputFiles[i].delete();
                System.out.println( outputFile + ":既存のファイルを削除");
            }
        }

        // ZIP対象フォルダ配下の全ファイルを取得
        List<File> files = new ArrayList<File>();
        getFiles(path,files);
        // 入力ストリーム
        InputStream is = null;

        // ZIP形式の出力ストリーム
        ZipOutputStream zos = null;

        // 入出力用のバッファを作成
        byte[] buf = new byte[1024];

        // ZipOutputStreamオブジェクトの作成
        try {
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outputFilepath,true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            for (File file : files){
                // 入力ストリームのオブジェクトを作成
                is = new BufferedInputStream(new FileInputStream(file));
                ZipEntry entry = new ZipEntry(
                        file.getAbsolutePath().replace(path.getAbsolutePath() + File.separator, ""));
                zos.putNextEntry(entry);

                // 入力ストリームからZIP形式の出力ストリームへ書き出す
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }

                // 入力ストリームを閉じる
                is.close();

                // エントリをクローズする
                zos.closeEntry();
            }

            // 出力ストリームを閉じる
            zos.close();

            //Zipファイルをメディアスキャン
            String scanFile[] = {outputFilepath};
            MediaScannerConnection.scanFile(context,scanFile,null,null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return outputFile;

    }
    /**
     * 指定したフォルダ配下の全ファイルを取得
     * @param parentDir ファイル取得対象フォルダ
     * @param files     ファイル一覧
     */
    private static void getFiles(final File parentDir, final List<File> files) {

        // ファイル取得対象フォルダ直下のファイル,ディレクトリを走査
        for (File f : parentDir.listFiles()) {

            // ファイルの場合はファイル一覧に追加
            if (f.isFile()) {
                files.add(f);

                // ディレクトリの場合は再帰処理
            } else if (f.isDirectory()) {
                getFiles(f, files);
            }
        }
    }

    public void startLog(Context context){

        StringBuilder logStr = new StringBuilder();


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
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        displayMetrics = new DisplayMetrics();
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

        AudioManager audioManager = ( AudioManager ) getActivity().getSystemService(Context.AUDIO_SERVICE);
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
        tsl = new LogSave();
        tsl.textsavelog(context, logStr);
        // ログバッファクリア
        logStr.delete(0, logStr.length());

    }

}