package com.technomedialab.battelysurvey.ui.home;

import android.content.Context;
import android.os.Environment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.technomedialab.battelysurvey.LogOutput;

import java.io.File;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    //ログ削除処理
    public void LogDelete(Context context){
        final File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        System.out.println(path.toString());

        File[] files = path.listFiles();
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
            }
        }

    }

    //ログ送信処理
    public void logOutput(Context context) throws InterruptedException {

        final String[] resultData = {""};

        LogOutput postTask = new LogOutput();
        final File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File[] files = path.listFiles();
        if(files == null) {
            System.out.println("配下にファイルが存在しない");
        }else{
            postTask.execute(files);
        }
//        postTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,files);

//        if(files == null) {
//            System.out.println("配下にファイルが存在しない");
//        }
//        //for文でファイルリスト分ループする
//        for(int i=0; i<files.length; i++) {
//
//            //ファイルの存在確認
//            if(files[i].exists() == false) {
//                System.out.println("continue:" + files[i].getName());
//                continue;
//                //ファイルの場合は再帰的に自身を呼び出して削除する
//            } else if(files[i].isFile()) {
//                Thread.sleep(100);
//                postTask.execute(files[i]);
//
////                slackOutput(files[i]);
//            }
//        }



    }

//    public String slackOutput(File filePath) {
//
//
//        //引数の値を格納
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .build();
//
//        File files = filePath;
//
//        //URLをセット
//        String url = "https://slack.com/api/files.upload";
//
//        //チャンネルと文章をセット
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("file", files.getName(), RequestBody.create(MediaType.parse("text"), files))
//                .addFormDataPart("token", "xoxb-392540951891-1139721507506-DI7SVtxNn6E4Ip5vPO6CfJy6")
//                .addFormDataPart("channels","#android_test")
//                .addFormDataPart("filename",files.getName())
//                .addFormDataPart("filetype","text")
//                .addFormDataPart("title", Build.DEVICE +"_"+ files.getName())
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                //トークンはAuthorizationで値にBearerを付けて送信する必要がある
////                .addHeader("Authorization", "Bearer xoxb-392540951891-1139721507506-DI7SVtxNn6E4Ip5vPO6CfJy6")
//                .post(requestBody)
//                .build();
//
//        try {
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//
//                }
//            });
////            System.out.println("成功");
//////            System.out.println(response.body().string());
////            return "true";
////        } catch (IOException e) {
////            System.out.println("失敗");
////            e.printStackTrace();
////        }
//        return "false";
//
//    }



}