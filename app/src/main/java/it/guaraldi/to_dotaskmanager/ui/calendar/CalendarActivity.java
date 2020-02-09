package it.guaraldi.to_dotaskmanager.ui.calendar;


import android.os.Bundle;
import android.os.Build;
import android.view.KeyEvent;
import android.util.Log;
import androidx.annotation.Nullable;
import android.app.NotificationManager;
import android.app.NotificationChannel;

import butterknife.ButterKnife;
import it.guaraldi.to_dotaskmanager.R;

import it.guaraldi.to_dotaskmanager.ui.base.BaseActivity;

public class CalendarActivity extends BaseActivity {

    private static final String TAG = "CalendarActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        ButterKnife.bind(this);
        setUp();

        // Provide compatibility for higher Android versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Same name and description for all canals
            CharSequence name = "ProvaNomeCanale";
            String description = "ProvaCanaleDescr";

            for (int i = 1; i < 5; i++) {
                int importance = i;
                String channelId = "";
                if (i == 1) {
                    channelId = "minPriorityChannel";
                } else if (i == 2) {
                    channelId = "lowPriorityChannel";
                } else if (i == 3) {
                    channelId = "defaultPriorityChannel";
                } else if (i == 4) {
                    channelId = "highPriorityChannel";
                }
                NotificationChannel channel = new NotificationChannel(channelId, name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                Log.d(TAG, "Creato canale con params " + channelId+ " " + name + " " + importance);
            }
        }
    }

    @Override
    protected void setUp() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
