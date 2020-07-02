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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

        return root;
    }

    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        //削除処理
        FileDelete(files);

        //ログ削除後、端末情報を出力する
        WindowManager windowManager = getActivity().getWindowManager();
        AudioManager audioManager = ( AudioManager ) getActivity().getSystemService(Context.AUDIO_SERVICE);
        LogUtility.TerminalInfoLog(this,context,windowManager,audioManager);

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
                if(!files[i].getName().equals("channel.txt")) {
                    Log.d("ファイ名",files[i].getName());
                    files[i].delete();
                    System.out.println("削除ファイル：" + files[i].toString());
                }
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

}