package com.technomedialab.battelysurvey;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class FileGeneration {

    private File file;

    private void gomicreate(int gomisize, Context context){
        final File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        final String testfile = "gomi.txt";

        if (file == null) {
            file = new File(path, testfile);
        }

        try {
            String str = "a";
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, Charset.forName("SJIS"));
            BufferedWriter bw = new BufferedWriter(outputStreamWriter);

            for(int count = 0;count >= gomisize;count++) {
                bw.write(str);
            }

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
