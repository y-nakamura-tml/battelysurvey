package com.technomedialab.battelysurvey;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TextRead {
    private File tfile;
    private File cfile;

//    public void TokenRead(Context context) {
//        File tpath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
//        String testfile = "token.txt";
//        tfile = new File(tpath, testfile);
//
//        try {
//
//            FileInputStream fileInputStream = new FileInputStream(tfile);
//            BufferedReader reader = new BufferedReader( new InputStreamReader(fileInputStream , StandardCharsets.UTF_8));
//            String str = "";
//            String tmp;
//            while( (tmp = reader.readLine()) != null ){
//                str = str + tmp + "\n";
//                //Log.d("ファイル中身：",tmp);
//            }
//
//            fileInputStream.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public String channelRead(Context context) {

        File tpath;

//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            tpath = Environment.getExternalStorageDirectory();
//
//        }else{
//            tpath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
//        }

        //ファイル名
        String testfile = "/Documents/channel.txt";

        tfile = new File(tpath, testfile);

        try {

            FileInputStream fileInputStream = new FileInputStream(tfile);
            BufferedReader reader = new BufferedReader( new InputStreamReader(fileInputStream , StandardCharsets.UTF_8));
            String str = "";
            String tmp;
            while( (tmp = reader.readLine()) != null ){
                str = str + tmp + "\n";
                //Log.d("ファイル中身：",tmp);
            }

            fileInputStream.close();

            return str;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
