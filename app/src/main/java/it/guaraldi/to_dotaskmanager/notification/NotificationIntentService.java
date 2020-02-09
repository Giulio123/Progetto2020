package it.guaraldi.to_dotaskmanager.notification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.ui.calendar.CalendarActivity;



public class NotificationIntentService extends Activity {

    public static final String TAG = "NotificationIntentS";

    /**
     * TEST: Hard-coding 10 seconds delayed notification
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduleNotification(getNotification("10 second delay"), 10000);
    }

    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra(Const.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(Const.NOTIFICATION, notification);

        Log.d(TAG, "scheduleNotification: id="+notificationIntent.getIntExtra(Const.NOTIFICATION_ID,0)+" PARCEBLE="+notificationIntent.getParcelableExtra(Const.NOTIFICATION));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        return builder.build();
    }

    //    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        if(intent.getAction() == Const.COMPLETE){
//            Log.d(TAG, "onHandleIntent: intentAction ="+intent.getAction());
//            Log.d(TAG, "NOTIFICATION DATA CONTENT ");
//            for(String key: intent.getBundleExtra(Const.NOTIFICATION_DATA).keySet())
//                Log.d(TAG, key+" = "+intent.getBundleExtra(Const.NOTIFICATION_DATA).get(key));
//
//            int notificationId = intent.getBundleExtra(Const.NOTIFICATION_DATA).getInt(Const.NOTIFICATION_ID);
//            NotificationManagerCompat.from(this).cancel(notificationId);
//        }
//         else if(intent.getAction() == Const.POSTPONE ||
//                intent.getAction() == Const.ONGOING){
//            Log.d(TAG, "onHandleIntent: intentAction ="+intent.getAction());
//
//
//            Bundle notificationData = intent.getBundleExtra(Const.NOTIFICATION_DATA);
//            int notificationId = notificationData.getInt(Const.NOTIFICATION_ID);
//            Notification updatedNotification = updateNotification(notificationData, intent.getAction()).build();
//            NotificationManagerCompat.from(this).notify(notificationId,updatedNotification);
//        }
//        else{
//            Log.d(TAG, "onHandleIntent: intentAction = "+intent.getAction());
//            Log.d(TAG, "NOTIFICATION DATA CONTENT ");
//            for(String key: intent.getBundleExtra(Const.NOTIFICATION_DATA).keySet())
//                Log.d(TAG, key+" = "+intent.getBundleExtra(Const.NOTIFICATION_DATA).get(key));
//
//            Bundle notificationData = intent.getBundleExtra(Const.NOTIFICATION_DATA);
//            int notificationId = notificationData.getInt(Const.NOTIFICATION_ID);
//            int priority = notificationData.getInt(Const.PRIORITY);
//            String channelId = "";
//
//            switch (priority){
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
//
//            notificationData.putInt(Const.NOTIFICATION_PRIORITY,priority);
//            notificationData.putString(Const.NOTIFICATION_CHANNEL,channelId);
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            Notification notification = createNotification(notificationData).build();
//            notificationManager.notify(notificationId,notification);
//        }
//        stopSelf();
//    }
//
//    private NotificationCompat.Builder updateNotification(Bundle notificationData, String action){
//        Log.d(TAG, "updateNotification: ");
//
//        PendingIntent taskDetailsIntent = showActivity(Const.DETAILS_TASK_F,notificationData);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,notificationData.getString(Const.NOTIFICATION_CHANNEL))
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle(notificationData.getString(Const.CONTENT_TITLE))
//                .setContentText(notificationData.getString(Const.CONTENT_TEXT))
//                .setContentIntent(taskDetailsIntent)
//                .setAutoCancel(true)
//                .setPriority(notificationData.getInt(Const.NOTIFICATION_PRIORITY));
//
//        if(action == Const.POSTPONE){
//            Log.d(TAG, "updateNotification: POSTPONE");
//            PendingIntent ongoingAction = createAction(Const.ONGOING,notificationData);
//            builder.addAction(0,"ONGOING",ongoingAction);
//        }
//        if(action == Const.ONGOING){
//            Log.d(TAG, "updateNotification: ONGOING");
//
//            PendingIntent postponeAction = createAction(Const.POSTPONE,notificationData);
//            PendingIntent completeAction = createAction(Const.COMPLETE,notificationData);
//
//            builder.addAction(0,"POSTPONE",postponeAction)
//            .addAction(0,"COMPLETE",completeAction);
//        }
//        return builder;
//    }
//
//    private NotificationCompat.Builder createNotification(Bundle notificationData){
//
//        Log.d(TAG, "createNotification: ");
//
//        PendingIntent taskDetailsIntent = showActivity(Const.DETAILS_TASK_F,notificationData);
//        PendingIntent postponeAction = createAction(Const.POSTPONE,notificationData);
//        PendingIntent ongoingAction = createAction(Const.ONGOING,notificationData);
//
//        return new NotificationCompat.Builder(this,notificationData.getString(Const.NOTIFICATION_CHANNEL))
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle(notificationData.getString(Const.CONTENT_TITLE))
//                .setContentText(notificationData.getString(Const.CONTENT_TEXT))
//                .setContentIntent(taskDetailsIntent)
//                .setAutoCancel(true)
//                .setPriority(notificationData.getInt(Const.NOTIFICATION_PRIORITY))
//                .addAction(0,"ONGOING",ongoingAction)
//                .addAction(0,"POSTPONE",postponeAction);
//    }
//
//    private PendingIntent showActivity(String actionName, Bundle notificationData){
//
//        Bundle taskData = new Bundle();
//        taskData.putInt(Const.TASK_ID,notificationData.getInt(Const.NOTIFICATION_ID));
//
//        Intent intent = new Intent(this, CalendarActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setAction(actionName);
//        intent.putExtra(Const.TASK_DATA,taskData);
//        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }
//
//    private PendingIntent createAction(String actionName,Bundle notificationData){
//
//        if(actionName == Const.POSTPONE){
//            Intent intent = new Intent(this,CalendarActivity.class);
//            intent.setAction(Const.POSTPONE_TASK_F);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Bundle taskData = new Bundle();
//            taskData.putInt(Const.TASK_ID,notificationData.getInt(Const.NOTIFICATION_ID));
//            intent.putExtra(Const.TASK_DATA,taskData);
//            intent.putExtra(Const.NOTIFICATION_DATA,notificationData);
//            return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        }
//        else {
//            Intent intent = new Intent(actionName);
//            intent.putExtra(Const.NOTIFICATION_DATA, notificationData);
//            return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        }
//
//    }
//

}
