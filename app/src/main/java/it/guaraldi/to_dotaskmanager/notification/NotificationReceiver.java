package it.guaraldi.to_dotaskmanager.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.NewsApp;

import static it.guaraldi.to_dotaskmanager.notification.Const.*;

public class NotificationReceiver extends BroadcastReceiver implements NotificationReceiverContract.View {

    private static final String TAG = "NotificationReceiver";

    @Inject
    public NotificationReceiverPresenter mPresenter;

    /* Get intent and make Notification appear */
    @Override
    public void onReceive(Context context, Intent intent) {
        NewsApp.getNewsComponent().inject(this);
        mPresenter.attachView(this);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int notificationId = intent.getIntExtra(NOTIFICATION_ID, -1);

        Log.d(TAG, "onReceive: notification ID=" + notificationId + " notification=" + notification);

        if (intent.getAction() == COMPLETE) {
            Log.d(TAG, "[debug] onReceive(): intentAction =" + intent.getAction());
            Log.d(TAG, "[debug] NOTIFICATION DATA CONTENT ");
            for (String key : intent.getBundleExtra(NOTIFICATION_DATA).keySet())
                Log.d(TAG, "[debug] " + key + " = " + intent.getBundleExtra(NOTIFICATION_DATA).get(key));
            notificationManager.cancel(notificationId);
        }
        else if (intent.getAction() == POSTPONE ||
                intent.getAction() == ONGOING) {
            Log.d(TAG, "[debug] onReceive(): intentAction =" + intent.getAction());

            Bundle notificationData = intent.getBundleExtra(NOTIFICATION_DATA);

//            Notification updatedNotification = updateNotification(notificationData, intent.getAction()).build();
//            notificationManager.notify(notificationId, updatedNotification);
        }
        else {
            Log.d(TAG, "[debug] onReceive(): intentAction = " + intent.getAction());
//            Log.d(TAG, "NOTIFICATION DATA CONTENT ");
//
//            int priority = notificationData.getInt(PRIORITY);
//            String channelId = "";
//
//            switch (priority) {
//                case 1:
//                    channelId = "minPriorityChannel";
//                    priority = NotificationCompat.PRIORITY_MIN;
//                    break;
//                case 2:
//                    channelId = "lowPriorityChannel";
//                    priority = NotificationCompat.PRIORITY_LOW;
//                    break;
//                case 3:
//                    channelId = "defaultPriorityChannel";
//                    priority = NotificationCompat.PRIORITY_DEFAULT;
//                    break;
//                case 4:
//                    channelId = "highPriorityChannel";
//                    priority = NotificationCompat.PRIORITY_HIGH;
//                    break;
//            }
            notificationManager.notify(notificationId, notification);
        }
    }

//    private void startNotificationService(Context context,Intent intent){
//        Log.d(TAG, "startNotificationService: ");
//        Intent notificationServiceIntent =  new Intent(context,NotificationIntentService.class);
//        notificationServiceIntent.setAction(intent.getAction());
//        notificationServiceIntent.putExtra(Const.NOTIFICATION_DATA,intent.getBundleExtra(Const.NOTIFICATION_DATA));
//        context.startService(notificationServiceIntent);
//    }

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
