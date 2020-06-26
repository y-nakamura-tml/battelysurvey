package com.technomedialab.battelysurvey;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationAreaUtility {

    private NotificationChannel channel;

    /**
     * 通知エリア（Android8.0以上）
     *
     * @param title   タイトル
     * @param message 通知内容
     */
    public void send(Context context,String title,String message) {

        //Android8.0以降の端末で通知を送る
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager manager = ( NotificationManager )
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (channel == null) {
                channel = new NotificationChannel(
                        // アプリでユニークな ID
                        "channel_1",
                        // ユーザが「設定」アプリで見ることになるチャンネル名
                        "test_channel",
                        // チャンネルの重要度（0 ~ 4）
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                // 通知時のライトの色
                channel.setLightColor(Color.GREEN);
                // ロック画面で通知を表示するかどうか
                channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                // 端末にチャンネルを登録し、「設定」で見れるようにする
                manager.createNotificationChannel(channel);
            }

            // 通知チャンネルのIDを指定するコンストラクタが増えている
            NotificationCompat.Builder builder
                    = new NotificationCompat.Builder(context, "channel_1")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp);
            NotificationManagerCompat.from(context).notify(1, builder.build());

        }
    }
}
