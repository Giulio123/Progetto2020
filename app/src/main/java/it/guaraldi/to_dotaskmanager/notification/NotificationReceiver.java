package it.guaraldi.to_dotaskmanager.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import androidx.core.app.NotificationManagerCompat;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.ui.calendar.CalendarActivity;
import it.guaraldi.to_dotaskmanager.utils.DateUtils;

public class NotificationReceiver extends BroadcastReceiver implements NotificationReceiverContract.View {

    private static final String TAG = "NotificationReceiver";

    @Inject
    public NotificationReceiverPresenter mPresenter;

    /**
     * Show
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        NewsApp.getNewsComponent().inject(this);
        mPresenter.attachView(this);
        Log.d(TAG, "onReceive: ");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(Const.NOTIFICATION);
        int id = intent.getIntExtra(Const.NOTIFICATION_ID, 0);
        Log.d(TAG, "onReceive: id="+id+" notification="+notification);
        notificationManager.notify(id, notification);

//        if (intent != null) {
//            if (intent.getAction() == Const.ADD_NOTIFICATION) {
//                Log.d(TAG, "onReceive: action ="+intent.getAction());
//
//                // /1000
//                long startAllarm = intent.getBundleExtra(Const.NOTIFICATION_DATA).getLong(Const.START_DATE);
//
//                Intent alarmIntent = new Intent(context, TestReciver.class);
//                alarmIntent.setAction(Long.toString(System.currentTimeMillis()));
//                alarmIntent.putExtra(Const.NOTIFICATION_DATA, intent.getBundleExtra(Const.NOTIFICATION_DATA));
//
//                PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(
//                        context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                startAllarm = SystemClock.elapsedRealtime() + 60 * 1000;
//                alarmManager.set(AlarmManager.RTC_WAKEUP, startAllarm, alarmPendingIntent );
//            }
//            else if (intent.getAction() != Const.POSTPONE) {
//                Log.d(TAG, "onReceive: action = " + intent.getAction());
//                startNotificationService(context,intent);
//                int taskId = intent.getBundleExtra(Const.NOTIFICATION_DATA).getInt(Const.NOTIFICATION_ID);
//                mPresenter.changeTaskStatus(intent.getAction(),taskId);
//            }
//        }

    }

    private void startNotificationService(Context context,Intent intent){
        Log.d(TAG, "startNotificationService: ");
        Intent notificationServiceIntent =  new Intent(context,NotificationIntentService.class);
        notificationServiceIntent.setAction(intent.getAction());
        notificationServiceIntent.putExtra(Const.NOTIFICATION_DATA,intent.getBundleExtra(Const.NOTIFICATION_DATA));
        context.startService(notificationServiceIntent);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void showError(int errorId) {

    }
}
