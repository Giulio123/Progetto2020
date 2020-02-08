package it.guaraldi.to_dotaskmanager.ui.calendar;


import android.os.Bundle;
import android.view.KeyEvent;
import androidx.annotation.Nullable;

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
    }

    @Override
    protected void setUp() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
