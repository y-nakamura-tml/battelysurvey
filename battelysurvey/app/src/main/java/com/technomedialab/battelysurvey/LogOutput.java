package com.technomedialab.battelysurvey;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LogOutput extends AsyncTask<File,String,List> {

    private OkHttpClient client;
    private CallBackTask callbacktask;
    public String stoken;
    public String schannels;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public LogOutput(String token,String channels) {
        stoken = token;
        schannels = channels;
    }


    @Override
    protected List doInBackground(File... filePath) {


        List<String> errFileList = new ArrayList<>();
        //引数の値を格納
        if(client == null) {
            client = new OkHttpClient.Builder()
                   .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        File files[] = filePath;

        String filesCount = String.valueOf(files.length);

        //URLをセット
        String url = "https://slack.com/api/files.upload";

        //for文でファイルリスト分ループする
        for(int i=0; i<files.length; i++) {
            //チャンネルと文章をセット
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", files[i].getName(), RequestBody.create(MediaType.parse("text"), files[i]))
                    .addFormDataPart("token", stoken)
                    .addFormDataPart("channels", schannels)
                    .addFormDataPart("filename", files[i].getName())
                    .addFormDataPart("filetype", "text")
                    .addFormDataPart("title",  files[i].getName())
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            System.out.println(files[i].getName());

            try {
                Response response = client.newCall(request).execute();
                //TODO:空ファイルを送信しようとすると応答は帰ってくるが送信結果がエラーとなっている

                String jsonStr = response.body().string();
                JSONObject json = new JSONObject(jsonStr);
                String status = json.getString("ok");

                if ("true".equals(status)){
                    System.out.println("成功");

                }else{
                    errFileList.add(files[i].getName());
                    System.out.println("失敗");

                }


            } catch (IOException | JSONException e) {
                errFileList.add(files[i].getName());
                System.out.println("失敗");
                e.printStackTrace();
            }
        }
        return errFileList;

    }

    @Override
    protected void onPostExecute(List result) {
        super.onPostExecute(result);
        callbacktask.CallBack(result);
    }
    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }


    public interface CallBackTask
    {
        void CallBack(List result);
    }
}
