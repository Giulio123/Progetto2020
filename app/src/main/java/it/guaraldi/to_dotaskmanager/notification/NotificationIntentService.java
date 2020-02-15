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
import android.os.storage.OnObbStateChangeListener;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.ui.calendar.CalendarActivity;

import static it.guaraldi.to_dotaskmanager.notification.Const.ADD_NOTIFICATION;
import static it.guaraldi.to_dotaskmanager.notification.Const.COMPLETE;
import static it.guaraldi.to_dotaskmanager.notification.Const.CONTENT_TEXT;
import static it.guaraldi.to_dotaskmanager.notification.Const.CONTENT_TITLE;
import static it.guaraldi.to_dotaskmanager.notification.Const.DETAILS_TASK_F;
import static it.guaraldi.to_dotaskmanager.notification.Const.NOTIFICATION_DATA;
import static it.guaraldi.to_dotaskmanager.notification.Const.NOTIFICATION_ID;
import static it.guaraldi.to_dotaskmanager.notification.Const.ONGOING;
import static it.guaraldi.to_dotaskmanager.notification.Const.POSTPONE;
import static it.guaraldi.to_dotaskmanager.notification.Const.POSTPONE_TASK_F;
import static it.guaraldi.to_dotaskmanager.notification.Const.PRIORITY;
import static it.guaraldi.to_dotaskmanager.notification.Const.START_DATE;
import static it.guaraldi.to_dotaskmanager.notification.Const.TASK_DATA;

public class NotificationIntentService extends IntentService {

    public static final String TAG = "NotificationIntentS";
    public int mId=0;
    private Intent mIntent = null;
    @Inject
    protected NotificationISPresenter mPresenter;

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NewsApp.getNewsComponent().inject(this);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null && intent.getAction()!=null) {
            mIntent = intent;
            NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
            switch (intent.getAction()) {
                case ONGOING:
                case ADD_NOTIFICATION:
                    Log.d(TAG, "onHandleIntent: action = "+mIntent.getAction());
                    PendingIntent postpone = createAction(POSTPONE, mIntent.getExtras(), this);
                    PendingIntent secondAction = mIntent.getAction().equals(ONGOING) ?
                            createAction(COMPLETE, mIntent.getExtras(), this) :
                            createAction(ONGOING,mIntent.getExtras(),this);
                    Notification n = createNotification(mIntent.getExtras(), this, postpone, secondAction);
                    nmc.notify(mIntent.getIntExtra(NOTIFICATION_ID,-1),n);
                    break;
                case COMPLETE:
                case POSTPONE:
                    Log.d(TAG, "onHandleIntent: action = "+mIntent.getAction());
                    int id = mIntent.getIntExtra(NOTIFICATION_ID,-1);
                    nmc.cancel(id);
                    break;
                default:
                    break;
            }
            if(COMPLETE.equals(intent.getAction()) || ONGOING.equals(intent.getAction()))
                mPresenter.changeTaskStatus(intent.getAction(),mIntent.getIntExtra(NOTIFICATION_ID,-1));
        }
        stopSelf(mId);
    }

    private PendingIntent createAction(String actionName, Bundle bundle, Context context){
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(actionName);
        intent.putExtras(bundle);
        return PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Notification createNotification(Bundle bundle, Context context,PendingIntent action1, PendingIntent action2) {
        // SET UP ChannelID and Notification priority
        int priority = bundle.getInt(PRIORITY);
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

        PendingIntent openDetailsPendInt = createAction(DETAILS_TASK_F,bundle,context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(bundle.getString(CONTENT_TITLE))
                .setContentText(bundle.getString(CONTENT_TEXT))
                .addAction(0,"POSTPONE",action1);
                if(ONGOING.equals(mIntent.getAction()))
                    builder.addAction(0,"COMPLETE",action2);
                else
                    builder.addAction(0,"ONGOING",action2);
                builder.setContentIntent(openDetailsPendInt)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder.build();
    }
}
