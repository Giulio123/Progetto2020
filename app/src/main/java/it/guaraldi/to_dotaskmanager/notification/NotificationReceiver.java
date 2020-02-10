package it.guaraldi.to_dotaskmanager.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
/**
 * crea un intent che prende gli altri 2 intent associati a ONGOING e SUBMIT
 */
import androidx.core.app.NotificationCompat;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.ui.calendar.CalendarActivity;

import static it.guaraldi.to_dotaskmanager.notification.Const.*;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationReceiver";

    /* Get intent and make Notification appear */
    @Override
    public void onReceive(Context context, Intent intent) {
        /* crea notifica */
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle bundle = intent.getExtras();

        Integer taskID = bundle.getInt(TASK_ID);
        String taskTitle = bundle.getString(TASK_TITLE);
        int priority = bundle.getInt(PRIORITY);
        long notificationTime = bundle.getLong(Const.START_DATE);
        Log.d(TAG, "onReceive: nCreate otification for task ID=" + intent.getAction() + bundle.getString("test") +  bundle.containsKey(TASK_DATA));

        if (intent.getAction() == COMPLETE) {
            Log.d(TAG, "[debug] onReceive(): intentAction =" + intent.getAction());
            notificationManager.cancel(taskID);
        }
        else if (intent.getAction() == POSTPONE ||
                intent.getAction() == ONGOING) {
            Log.d(TAG, "[debug] onReceive(): intentAction =" + intent.getAction());

            Bundle notificationData = intent.getBundleExtra(NOTIFICATION_DATA);
        }
        else {
            Log.d(TAG, "[debug] onReceive(): intentAction = " + intent.getAction());
            Notification notification = createNotification(bundle, context);
            notificationManager.notify(taskID, notification);
        }
    }

    private Notification createNotification(Bundle bundle, Context context) {
        // SET UP ChannelID and Notification priority
        Integer taskID = bundle.getInt(TASK_ID);
        String taskTitle = bundle.getString(TASK_TITLE);
        String taskDescr = bundle.getString(TASK_DESCR);
        int priority = bundle.getInt(PRIORITY);
        long notificationTime = bundle.getLong(Const.START_DATE);
        String channelId = "";
        int notificationPriority = -1;
        switch (priority) {
            case 1:
                channelId = "minPriorityChannel";
                notificationPriority = NotificationCompat.PRIORITY_MIN;
                break;
            case 2:
                channelId = "lowPriorityChannel";
                notificationPriority = NotificationCompat.PRIORITY_LOW;
                break;
            case 3:
                channelId = "defaultPriorityChannel";
                notificationPriority = NotificationCompat.PRIORITY_DEFAULT;
                break;
            case 4:
                channelId = "highPriorityChannel";
                notificationPriority = NotificationCompat.PRIORITY_HIGH;
                break;
        }

        PendingIntent ongoingInt = createAction(ONGOING, bundle, context);
        PendingIntent completePendInt = createAction(Const.COMPLETE, bundle, context);
        PendingIntent postponePendInt = createAction(Const.POSTPONE, bundle, context);

        Notification ongoingNotification = new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(taskTitle)
                .setContentText("Description: " + taskDescr)
                .addAction(0,"POSTPONE",postponePendInt)
                .addAction(0,"COMPLETE",completePendInt)
                .setContentIntent(ongoingInt)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        return ongoingNotification;
    }

    private PendingIntent createAction(String actionName, Bundle bundle, Context context){
        Intent intent = new Intent(context, NotificationIntentService.class);

        intent.putExtras(bundle);

        if(actionName == Const.POSTPONE){
            intent = new Intent(context, CalendarActivity.class);
            intent.setAction(Const.POSTPONE_TASK_F);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(POSTPONE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (actionName == ONGOING) {
            intent.setAction(ONGOING);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (actionName == COMPLETE){
            intent.setAction(COMPLETE);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
