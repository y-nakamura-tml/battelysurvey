package com.technomedialab.battelysurvey;

import android.os.AsyncTask;
import android.os.Build;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpPost extends AsyncTask<String,String,String> {

    private OkHttpClient client;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //String json = "{\"channel\":\"%23android_test\",\"text\":\"bot test\"}";

    @Override
    protected String doInBackground(String... strings) {

        //引数の値を格納
        String params = strings[0];

        if(client == null) {
            client = new OkHttpClient();
        }

        //URLをセット
        String url = "https://slack.com/api/chat.postMessage";
        //RequestBody body = RequestBody.create(JSON, json);

        //チャンネルと文章をセット
        FormBody.Builder formBodyBuilder = new FormBody.Builder()
                .add("channel","#android_test")
                .add("text", Build.DEVICE + ":電池が " + params + "% です。" );
        RequestBody requestBody = formBodyBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                //トークンはAuthorizationで値にBearerを付けて送信する必要がある
                .addHeader("Authorization", "Bearer xoxb-392540951891-1139721507506-4REHlK11UUNp3lmtjh15Em0A")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
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
