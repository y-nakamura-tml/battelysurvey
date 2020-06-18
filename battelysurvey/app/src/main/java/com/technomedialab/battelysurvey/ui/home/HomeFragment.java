package com.technomedialab.battelysurvey.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.technomedialab.battelysurvey.LogOutput;
import com.technomedialab.battelysurvey.R;

import java.io.File;
import java.util.List;

public class HomeFragment extends Fragment implements LogOutput.CallBackTask{

    private HomeViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }

    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TextViewをひも付けます
        final TextView mTextView = view.findViewById(R.id.text_home);

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

//                        new AlertDialog.Builder(getActivity())
//                                .setTitle("送信完了")
//                                .setMessage("ログファイルの送信が完了しました")
//                                .setPositiveButton("OK", null)
//                                .show();
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
    public void logOutput(Context context) {
        final String[] resultData = {""};

        LogOutput postTask = new LogOutput();
        final File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File[] files = path.listFiles();
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

}