package com.technomedialab.battelysurvey;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.util.Log;

class LogSave{

    private File file;

    public void textsavelog(Context context, StringBuilder logStr) {


        //ログファイル保存
        final Date date = new Date(); // 今日の日付

        //日毎にlogファイルを分ける
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        final File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        final String testfile = dateFormat.format(date) + "log.txt";

        if (file == null) {
            file = new File(path, testfile);
        }

        try {
            String str = logStr.toString();
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(outputStreamWriter);
            bw.write(str);
            //ファイルに書き込む
            bw.flush();
            //ファイルを閉じる
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LogUtility.TAG(this), "ログの書き込み失敗");
        }finally {

        }

    }

}
