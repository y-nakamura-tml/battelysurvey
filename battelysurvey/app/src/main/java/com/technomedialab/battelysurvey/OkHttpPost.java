package com.technomedialab.battelysurvey;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpPost extends AsyncTask<String,String,String> {

    private OkHttpClient client;
    private RequestBody requestBody;
    private MultipartBody.Builder formBodyBuilder;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected String doInBackground(String... strings) {

        //引数の値を格納
        String params = strings[0];

        if(client == null) {
            client = new OkHttpClient();
        }

        //URLをセット
//        String url = "https://slack.com/api/chat.postMessage";
        String url = "https://slack.com/api/files.upload";
        //RequestBody body = RequestBody.create(JSON, json);
        String filepath = "/storage/emulated/0/Android/data/com.technomedialab.battelysurvey/files/Documents/2020-05-15log.txt";
        File upfile = new File(filepath);


        if(params.equals("a")) {
            //チャンネルと文章をセット
            formBodyBuilder = new MultipartBody.Builder()
                    //.add("title", "ファイルアップロードテスト")
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "2020-05-15log.txt",RequestBody.create(MediaType.parse("text"), upfile))
                    .addFormDataPart("channel", "#android_test")
                    .addFormDataPart("filename", "2020-05-15log.txt")
                    .addFormDataPart("filetype", "text");

            requestBody = formBodyBuilder.build();

        }else{
            //チャンネルと文章をセット
            formBodyBuilder = new MultipartBody.Builder()
                    .addFormDataPart("channel", "#android_test")
                    .addFormDataPart("text", Build.DEVICE + ":電池が " + params + "% です。");

            requestBody = formBodyBuilder.build();
        }

        Request request = new Request.Builder()
                .url(url)
                //トークンはAuthorizationで値にBearerを付けて送信する必要がある
                .addHeader("Authorization", "Bearer xoxb-392540951891-1139721507506-DI7SVtxNn6E4Ip5vPO6CfJy6")
                .post(requestBody)
                .build();


        try {
            Response response = client.newCall(request).execute();
            //Log.d("レスポンス", response.body().string());
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String str) {
        //Log.d("Debug",str);
    }
}
