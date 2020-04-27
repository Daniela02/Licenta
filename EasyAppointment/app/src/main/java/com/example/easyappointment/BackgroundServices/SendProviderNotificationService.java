package com.example.easyappointment.BackgroundServices;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.easyappointment.Activities.homePage.HomePageActivity;
import com.example.easyappointment.R;
import com.example.easyappointment.data.Models.Appointments;
import com.example.easyappointment.data.Models.ObjectBox;
import com.example.easyappointment.data.Models.accounts.Provider;
import com.example.easyappointment.data.Models.accounts.Provider_;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import io.objectbox.Box;

public class SendProviderNotificationService extends Service {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private static final String ACCOUNT_ID = "account_id";
    private final static String default_notification_channel_id = "default";
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 500;
    private Integer NOTIFICATION_UID = 10133;
    private Box<Provider> providerBox;
    private Provider provider;
    private Long account_id;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        account_id = intent.getLongExtra(ACCOUNT_ID, 0L);
        providerBox = ObjectBox.get().boxFor(Provider.class);
        provider = providerBox
                .query()
                .equal(Provider_.accountId, account_id)
                .build()
                .findFirst();
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stopTimerTask();
        super.onDestroy();
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 5000, Your_X_SECS * 1000); //
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            public void run() {
                handler.post(() -> {
                    List<Appointments> appointmentsList = provider.getAppointments()
                            .stream()
                            .filter(a -> a.status.equals(getString(R.string.pending)))
                            .collect(Collectors.toList());
                    if (appointmentsList.size() != 0) {
                        List<Appointments> seenAppointments = appointmentsList
                                .stream()
                                .filter(a -> a.seenByProvider.equals(false))
                                .collect(Collectors.toList());
                        if (seenAppointments != null && seenAppointments.size() != 0) {
                            Log.d(TAG, String.valueOf(appointmentsList.size()));
                            createNotification();
                        }
                    }
                });
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        StatusBarNotification[] notifications =
                mNotificationManager.getActiveNotifications();
        for (StatusBarNotification notification : notifications) {
            if (notification.getUid() == NOTIFICATION_UID) {
                // notification exists
                return;
            }
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), default_notification_channel_id);
        mBuilder.setContentTitle("Easy Appointment");
        mBuilder.setContentText("You have a new appointment");
        mBuilder.setTicker("You have a new appointment");
        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        mBuilder.setAutoCancel(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;

        Intent homePageIntent = new Intent(getApplicationContext(), HomePageActivity.class);
        homePageIntent.putExtra(HomePageActivity.EMAIL, provider.account.getTarget().email);
        homePageIntent.putExtra(HomePageActivity.NOTIFICATION, getString(R.string.provider).toLowerCase());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(homePageIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }

}
