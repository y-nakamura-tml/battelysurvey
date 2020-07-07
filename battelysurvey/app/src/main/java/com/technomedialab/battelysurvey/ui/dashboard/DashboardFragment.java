package com.technomedialab.battelysurvey.ui.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.technomedialab.battelysurvey.ExplorerItem;
import com.technomedialab.battelysurvey.ExplorerListArrayAdapter;
import com.technomedialab.battelysurvey.LogOutput;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DashboardFragment extends Fragment implements LogOutput.CallBackTask{

    private DashboardViewModel dashboardViewModel;
    private MainApplication mainApp;
    private ListView listView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);

        //チャンネルの値を取得
        mainApp = (MainApplication)getActivity().getApplicationContext();
        String[] channel = mainApp.getChannel();
        if(channel != null) {
            //チャンネルスピナーの値をセット
            Spinner spinner = root.findViewById(R.id.pd_channel);
            ArrayAdapter<String> arrayAdapter
                    = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line);
            int n = 0;
            for(;n < channel.length;){
                arrayAdapter.add(channel[n]);
                n++;
            }
            spinner.setAdapter(arrayAdapter);
        }else{
            AlertDialog.Builder alertDelete = new AlertDialog.Builder(getContext());
            alertDelete
                    .setTitle("slackの設定ファイルが取得できません。")
                    .setMessage("ファイルを置きなおしてアプリを再起動してください。\nアプリは続行できます。")
                    .setPositiveButton("OK",null)
                    .show();
        }

        //リストの生成
         listView = (ListView)root.findViewById(R.id.lb_fileList);

        return root;
    }

    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ファイル選択押下時
        view.findViewById(R.id.l_fileSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("DashboardFragment:onViewCreated onClick Start");
                outputFileEvent(v);


            }
        });

        // ファイル送信押下時
        view.findViewById(R.id.l_fileOutPut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("DashboardFragment:onViewCreated onClick Start");
                outputClickEvent(v);

            }
        });

    }

    private void outputFileEvent(View v) {

        //ファイルストレージを表示
        try {
            Intent intent = null;
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

//            intent.setType("text/plain");   //TEXT file only
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Open a file"), 1000);

        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(this, "Please install a File Browser/Manager", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    //選択したファイルのパスをリストに表示する処理
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList listData = new ArrayList<>();
        String filePath = "";

        //選択したファイルパスを取得
        if (resultCode == Activity.RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            if(clipData==null){
                // １つだけ選択

                Uri uri = data.getData();
                filePath = getPath(uri);

                listData.add(filePath);

            }else {
                // 複数選択
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri uri = clipData.getItemAt(i).getUri();


                    filePath = getPath(uri);
                    listData.add(filePath);

                }
            }
        }
        // リスト項目とListViewを対応付けるArrayAdapterを用意する
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listData);

        // ListViewにArrayAdapterを設定する
        listView.setAdapter(adapter);
    }

    private void outputClickEvent(View v) {
        AlertDialog.Builder alertOutput = new AlertDialog.Builder(v.getContext());

        if (listView.getCount() == 0){
            alertOutput
                .setTitle("ファイル未選択エラー")
                .setMessage("ファイルが選択されておりません。")
                .setPositiveButton("OK", null)
                .show();

        }else{
            alertOutput
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

    }

    //ログ送信処理
    public void logOutput(Context context) {
//        //zipファイル作成
        final String zipFile = zipCompression(getContext());
//
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

        final File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File[] files = path.listFiles(filter);

        LogOutput postTask = new LogOutput(mainApp.getToken());
        if(files == null) {
            System.out.println("配下にファイルが存在しない");
        }else{
            // コールバック設定
            postTask.setOnCallBack((LogOutput.CallBackTask) this);
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
            Message = "対象ファイルの送信が完了しました";
        } else {

            for (int i = 0; i < result.size(); i++) {
                errStr = errStr + result.get(i).toString() + "\r\n";
            }

            Title = "送信失敗";
            Message = "送信に失敗したファイルがあります" + "\r\n" + errStr;
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
        final File outputPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        String outputFile =  "file_" +  dateFormat.format(date) + ".zip";
        String outputFilepath = outputPath + "/" + outputFile;


        File[] inputFiles = outputPath.listFiles();
        for (int i=0;i<inputFiles.length;i++){
            if (outputFile.equals(inputFiles[i].getName())){
                inputFiles[i].delete();
                System.out.println( outputFile + ":既存のファイルを削除");
            }
        }



        // ZIP対象フォルダ配下の全ファイルを取得
        List<File> files = new ArrayList<File>();

        for (int i=0;i < listView.getCount();i++){
            File path = new File(listView.getItemAtPosition(i).toString());
            String fileName = path.getName();
            String filePath = path.getPath().replace(fileName,"");
            File file = new File(filePath);

            files.add(path);

        }

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
                        file.getAbsolutePath().replace(outputPath.getAbsolutePath() + File.separator, ""));
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


    public String getPath(Uri uri) {
// Will return "image:x*"
        String wholeID = DocumentsContract.getDocumentId(uri);

// Split at colon, use second item in the array
        String id ="";
        Uri uris;
        if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
            //ダウンロードを選択時
            id = wholeID;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                //TODO:うまくとれない
//              uris = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
//              uris = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                uris = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            }else{
                uris = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            }
        }else{
            id = wholeID.split(":")[1];
            uris = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }
//        String[] column = { MediaStore.Images.Media.DATA };
        String[] column = { MediaStore.Files.FileColumns.DATA };

// where id is equal to
//        String sel = MediaStore.Images.Media._ID + "=?";
        String sel = MediaStore.Files.FileColumns._ID + "=?";

        Cursor cursor = getActivity().getContentResolver().
                query(uris,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

}
