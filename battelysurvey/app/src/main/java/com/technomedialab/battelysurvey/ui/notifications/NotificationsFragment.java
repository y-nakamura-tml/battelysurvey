package com.technomedialab.battelysurvey.ui.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.technomedialab.battelysurvey.Const;
import com.technomedialab.battelysurvey.KeyValuePairAdapter;
import com.technomedialab.battelysurvey.MainApplication;
import com.technomedialab.battelysurvey.R;
import com.technomedialab.battelysurvey.SensorActivity;
import com.technomedialab.battelysurvey.TextRead;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private MainApplication mainApp;
    private Spinner pd_interval;
    private Spinner pd_channel;
    private Switch sw_lightSensor;
    private SensorActivity sa;
    private KeyValuePairAdapter mSortSpinnerAdapter;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        mainApp = (MainApplication) getActivity().getApplicationContext();
        sa = (SensorActivity) getActivity();


        //測定間隔の初期値を取得（ミリ秒⇒秒に変換）
        long sec = TimeUnit.MILLISECONDS.toSeconds(mainApp.getMinInterval());
        System.out.println("ミリ秒⇒秒:" + mainApp.getMinInterval() + " ⇒ " + sec);

        //測定間隔プルダウンの設定
        pd_interval = root.findViewById(R.id.pd_interval);
        //測定間隔の初期値からプルダウンの値を選択
        int selectionPosition= ((ArrayAdapter<String>)pd_interval.getAdapter()).getPosition(String.valueOf(sec));
        pd_interval.setSelection(selectionPosition);

        //照度設定スイッチ
        sw_lightSensor = root.findViewById(R.id.sw_lightSensor);
        sw_lightSensor.setChecked(mainApp.getLightSensorFlg());

        //チャンネルの値を取得
        MainApplication mainApp = (MainApplication)getActivity().getApplicationContext();
        String[] channel = mainApp.getChannel();
        if(channel != null) {
            //チャンネルスピナーの値をセット
            pd_channel = root.findViewById(R.id.spinner);
//            ArrayAdapter<String> arrayAdapter
//                    = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line);
//            int n = 0;
//            for(;n < channel.length;){
//                arrayAdapter.add(channel[n]);
//                n++;
//            }

            ArrayList<Pair<String, String>> sortItemList = new ArrayList<Pair<String, String>>();
            for (int i = 0; i < channel.length; i++) {
                //カンマ区切りで分割（チャンネル名，トークンID）
                String[] channelData = channel[i].split(",");
                //分割した値をプルダウンリストにセット
                sortItemList.add(new Pair<String, String>(channelData[0], channelData[1]));

            }

            // スピナーにアダプターを設定
            mSortSpinnerAdapter = new KeyValuePairAdapter(getContext(), android.R.layout.simple_dropdown_item_1line,
                    sortItemList);
            mSortSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            pd_channel.setAdapter(mSortSpinnerAdapter);
//            pd_channel.getAdapter().getPosition(1);
//            int channelPosition = mSortSpinnerAdapter.getPosition(pd_channel.setp);
//            pd_channel.setSelection(channelPosition);
        }else{
            AlertDialog.Builder alertDelete = new AlertDialog.Builder(getContext());
            alertDelete
                    .setTitle("slackの設定ファイルが取得できません。")
                    .setMessage("ファイルを置きなおしてアプリを再起動してください。\nアプリは続行できます。")
                    .setPositiveButton("OK",null)
                    .show();
        }



        final TextView textView = root.findViewById(R.id.l_interval);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
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

        // 保存ボタン押下時
        view.findViewById(R.id.b_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClickEvent(v);
            }
        });

    }

    private void saveClickEvent(View v) {

        //測定間隔
        long mSec = TimeUnit.SECONDS.toMillis(Long.parseLong(pd_interval.getSelectedItem().toString()));
        mainApp.setMinInterval(mSec);

        //照度設定
        mainApp.setLightSensorFlg(sw_lightSensor.isChecked());
        sa.setLightSensor(mainApp.getLightSensorFlg());

        //チャンネル設定
        //選択しているプルダウンのトークンを取得
        Pair<String, String> selectedItem = mSortSpinnerAdapter.getItem(pd_channel.getSelectedItemPosition());
        mainApp.setSelectChannel(selectedItem.first);
        mainApp.setToken(selectedItem.second);


    }


}