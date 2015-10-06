package com.star.downloader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

public class DownloaderService extends Service {

    public static final String ACTION_DOWNLOAD = "download";
    public static final String ACTION_DOWNLOAD_COMPLETED = "download_completed";

    public static final String URL = "url";

    public static final int DOWNLOAD_COMPLETED_NOTIFICATION_ID = 1234;

    private HandlerThread mHandlerThread;

    public DownloaderService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mHandlerThread = new HandlerThread("Skippy");
        mHandlerThread.start();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        String action = intent.getAction();
        final String url = intent.getStringExtra(URL);

        if (action.equals(ACTION_DOWNLOAD)) {
            Handler handler = new Handler(mHandlerThread.getLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {

                    Downloader.download(url);

                    Notification.Builder builder = new Notification.Builder(DownloaderService.this)
                            .setContentTitle("Download completed")
                            .setContentText(url)
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.icon_download);

                    Notification notification = builder.build();

                    NotificationManager notificationManager = (NotificationManager)
                            getSystemService(NOTIFICATION_SERVICE);

                    notificationManager.notify(DOWNLOAD_COMPLETED_NOTIFICATION_ID, notification);

                    Intent downloadCompletedIntent = new Intent();
                    downloadCompletedIntent.setAction(ACTION_DOWNLOAD_COMPLETED);
                    downloadCompletedIntent.putExtra(URL, url);
                    sendBroadcast(intent);

                }
            });
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
