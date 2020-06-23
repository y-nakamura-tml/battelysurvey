package com.technomedialab.battelysurvey;

import android.os.AsyncTask;

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

        //URLをセット
        String url = "https://slack.com/api/files.upload";

        //for文でファイルリスト分ループする
        for(int i=0; i<filePath.length; i++) {
            //チャンネルと文章をセット
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", filePath[i].getName(), RequestBody.create(MediaType.parse("text"), filePath[i]))
                    .addFormDataPart("token", "xoxb-392540951891-1139721507506-GWCkhnBtJooLgFGpEyjbjFhH")
                    .addFormDataPart("channels", "#android_test")
                    .addFormDataPart("filename", filePath[i].getName())
                    .addFormDataPart("filetype", "text")
                    .addFormDataPart("title",  filePath[i].getName())
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            System.out.println(filePath[i].getName());

            try {
                Response response = client.newCall(request).execute();
                System.out.println(response.toString());
                System.out.println("成功");
            } catch (IOException e) {
                errFileList.add(filePath[i].getName());
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
