package it.guaraldi.to_dotaskmanager.ui.edittask;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.os.Parcelable;
import android.print.PrintAttributes;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.adapter.SpinnerAdapter;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.notification.Const;
import it.guaraldi.to_dotaskmanager.notification.NotificationReceiver;
import it.guaraldi.to_dotaskmanager.ui.base.BaseActivity;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;
import it.guaraldi.to_dotaskmanager.ui.edittask.dialog.DialogNewCategoryFragment;
import it.guaraldi.to_dotaskmanager.ui.edittask.dialog.DialogPeriodsFragment;
import it.guaraldi.to_dotaskmanager.ui.edittask.dialog.DialogShowCategoryFragment;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.PersonalizedInstanceState;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.child.ChildPersonalizedInstanceState;
import it.guaraldi.to_dotaskmanager.util.ActivityUtils;
import it.guaraldi.to_dotaskmanager.util.DateUtils;

import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.CATEGORY;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.CATEGORY_PARAMS;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.CHILD_PERSONALIZED_STATE;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.DIALOG_ADD_CATEGORY_REQ;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.DIALOG_CATEGORY_REQ;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.DIALOG_PERIOD_REQ;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.EDIT_STATE;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.LIST_CATEGORIES;

import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.LOAD_CATEGORY;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.LONG_CLICK;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.PERIOD;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.PERIOD_PARAMS;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.PERSONALIZED_PERIOD;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.PERSONALIZED_POS;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.PERSONALIZED_STATE;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.POS;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.REMOVE_CATEGORY;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.REPLY_TASK;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.SELECTED_CATEGORY;


public class EditTaskFragment extends BaseFragment implements EditTaskContract.View, View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "EditTaskFragment";
    private static final String TYPE_DATA_0 = "PRIORITY";
    private static final String TYPE_DATA_1 = "CATEGORY";
    private static final String DIALOG_DATE_P = "DATE";
    private static final String DIALOG_TIME_P = "TIME";
    private static final String DIALOG_PERIOD_P = "PERIOD_DIALOG";
    private static final String DIALOG_ADD_CATEGORY = "DIALOG_ADD_CATEGORY";
    private static final String DIALOG_CATEGORY = "DIALOG_CATEGORY";
    private Button saveTaskBtn;
    private Button cancelTaskBtn;
    private EditText title;
    private TextView email;
    private Switch aSwitchAllDay;
    private TextView startDate;
    private TextView endDate;
    private TextView startTime;
    private TextView endTime;
    private TextView repeatTv;
    private TextView descriptionTv;
    private TextView category;
    private SpinnerAdapter colorAdapter;
    private Spinner colorS;
    private Spinner priorityS;
    private FloatingActionButton fab;
    private boolean dialogIsVisible = false;
    private boolean errorDate = false;
    private int viewclickedId;
    private ColorStateList textColor;
    private Bundle dialogParams;
    private int periodSelection = 0;
    private int categorySelection = 0;
    private PersonalizedInstanceState personalizedState = null;
    private ChildPersonalizedInstanceState childPersonalizedState = null;
    @Inject
    protected EditTaskPresenter mPresenter;
    private List<String> categoriesList;
    private NotificationReceiver mReciever;
    private Intent mIntent;
    ////////////////////////
/// FRAGMENT METHODS  //
////////////////////////
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogIsVisible = false;
        viewclickedId = -1;
        dialogParams = new Bundle();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NewsApp.getNewsComponent().inject(this);
        return inflater.inflate(R.layout.edit_task_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, savedInstanceState!=null ? "onViewCreated: state:"+savedInstanceState.containsKey("STATE") : "onViewCreated: savedInstaceState: null");
        Log.d(TAG, getArguments()!=null ? "onViewCreated: args:"+getArguments().containsKey("STATE") : "onViewCreated: args:null");
        super.onViewCreated(view, savedInstanceState);
        if(mPresenter != null)
            mPresenter.attachView(this);
        initViews();
        setUp(getArguments());
        mPresenter.updateCurrentTaskId();

        if((mIntent=getActivity().getIntent())!=null )
            if(mIntent.getAction() == Const.POSTPONE_TASK_F)
                if(mIntent.getExtras() !=null){
                    int taskId = mIntent.getBundleExtra(Const.TASK_DATA).getInt(Const.TASK_ID);
                    int notificationId = mIntent.getBundleExtra(Const.NOTIFICATION_DATA).getInt(Const.NOTIFICATION_ID);
                    NotificationManagerCompat.from(getContext()).cancel(notificationId);
                    mPresenter.getTaskById(taskId);
                    //TODO BLOCCA CAMPI NON NECESSARI
                }



    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");

        registerReciever();
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReciever);
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if(mPresenter != null) {
            mPresenter.detachView(this);
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void dataError(String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
    }

    ////////////////////
/// VIEW METHODS  //
////////////////////
    @Override
    public void updateDate(String newDateOrTime, boolean validDate) {
        switch (viewclickedId) {
            case R.id.starDateText:
                startDate.setText(newDateOrTime);
                break;
            case R.id.endDateText:
                endDate.setText(newDateOrTime);
                break;
        }
        errorDate = validDate;
        Log.d(TAG, "updateDate: "+errorDate);
        errorDate();
    }

    @Override
    public void updateTime(String newTime, boolean validTime) {
        switch (viewclickedId) {
            case R.id.starHourText:
                startTime.setText(newTime);
                break;
            case R.id.endHourText:
                endTime.setText(newTime);
                break;
        }
        errorDate = validTime;
        Log.d(TAG, "updateTime: "+errorDate);
        errorDate();
    }

    @Override
    public void updatePeriod(String period, int position) {

        if(period == getString(R.string.personalized)) {
            if(mPresenter != null && mPresenter.getView()!=null)
            mPresenter.openPersonalizedPeriodicTask((EditInstanceState) saveState());
        }
        else {
            if(position == 0 && !period.equals(getString(R.string.dont_repeat))){
                periodSelection = position;
                dialogParams.putBundle(PERIOD_PARAMS,updateRecyclerDialog(position,period,PERIOD_PARAMS));
                repeatTv.setText(period);
            }
            if(position != 0){
                Log.d(TAG, "updatePeriod: ");
                personalizedState = null;
                childPersonalizedState = null;
            }
            periodSelection = position;
            dialogParams.putBundle(PERIOD_PARAMS,updateRecyclerDialog(position,period,PERIOD_PARAMS));
            repeatTv.setText(period);
        }
    }


    @Override
    public void updateEmail() {

    }

    @Override
    public void updateCategory(int position, String category, int typeUpdate) {
        if(typeUpdate == LOAD_CATEGORY) {
            if (category.contains(getString(R.string.add_other)))
                showDialog(DIALOG_ADD_CATEGORY, R.id.selected_category);
            else {
                categorySelection = position;
                dialogParams.putBundle(CATEGORY_PARAMS, updateRecyclerDialog(position, category, CATEGORY_PARAMS));
                this.category.setText(category);
            }
        }
        if(typeUpdate == REMOVE_CATEGORY) {
            categorySelection = 0;
            categoriesList.remove(position);
            if(this.category.getText().toString().contains(category)) {
                this.category.setText(getString(R.string.c0));
                dialogParams.putBundle(CATEGORY_PARAMS,updateRecyclerDialog(0,getString(R.string.c0),CATEGORY_PARAMS));
            }
        }
    }

    @Override
    public void updatePosition() {

    }

    @Override
    public void errorSaveTask() {

    }

    @Override
    public void showCalendarView(String taskTitle, String duration, int id, long startDate, int priority) {
        Log.d(TAG, "showCalendarView: tasktitle="+taskTitle+" duration="+duration+" id="+id+" startdate="+startDate+" priority="+priority);
        //TODO LANCIA ALLARME NOTIFICA

        Bundle notificationData = new Bundle();
        notificationData.putString(Const.CONTENT_TITLE,taskTitle);
        notificationData.putString(Const.CONTENT_TEXT,duration);
        notificationData.putInt(Const.NOTIFICATION_ID,id);
        notificationData.putLong(Const.START_DATE,startDate);
        notificationData.putInt(Const.PRIORITY,priority);

        Intent broadcastIntent = new Intent(Const.ADD_NOTIFICATION);
        broadcastIntent.putExtra("NOTIFICATION_DATA",notificationData);
        Log.d(TAG, "NOTIFICATION DATA CONTENT ");
        for(String key: notificationData.keySet())
            Log.d(TAG, key+" = "+notificationData.get(key));
        getActivity().sendBroadcast(broadcastIntent);

        Navigation.findNavController(getView()).navigate(R.id.action_editTaskFragment_to_calendarFragment);
    }

    @Override
    public void showPersonalizedView(EditInstanceState editState) {
        Log.d(TAG, "showPersonalizedView: state:"+editState.toString());
        Bundle data = new Bundle();
        buildDataToSend(data,EDIT_STATE,editState);
        if(personalizedState != null)
            buildDataToSend(data,PERSONALIZED_STATE,personalizedState);
        if(childPersonalizedState != null)
            buildDataToSend(data,CHILD_PERSONALIZED_STATE,childPersonalizedState);
        Navigation.findNavController(getView()).navigate(R.id.action_editTaskFragment_to_personalizedFragment,data);
    }

    @Override
    public void loadAllCategories(List<String> categories) {
        categoriesList = new ArrayList<>();
        for(String s: getResources().getStringArray(R.array.category))
            categoriesList.add(s);
        categories.add(categoriesList.remove(categoriesList.size()-1));
        categoriesList.addAll(categories);
    }

    @Override
    public void loadCategory(String category) {
        String last = categoriesList.remove(categoriesList.size()-1);
        categoriesList.add(category);
        categoriesList.add(last);
        this.category.setText(category);
        dialogParams.putBundle(CATEGORY_PARAMS,updateRecyclerDialog(categoriesList.indexOf(category),category,CATEGORY_PARAMS));
    }

    @Override
    public void updateViewTaskData(String title, String email, int priority, String category, String start, String end, String description, String color) {
        Log.d(TAG, "updateViewTaskData: DEVI IMPLEMENTARE UPDATE");
    }

////////////////////
/// OTHER METHODS //TODO HANDLE ALL ONCLICK
////////////////////


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case DIALOG_PERIOD_REQ:
                if(resultCode ==Activity.RESULT_OK){
                    dialogIsVisible = false;
                    if(data != null && data.hasExtra(PERIOD))
                        mPresenter.createPeriodicTask(data.getStringExtra(PERIOD),
                                data.getIntExtra(POS,-1));
                }
                else Log.d(TAG, "onActivityResult: ERROR RESULT CODE PERIOD");
                break;
            case DIALOG_ADD_CATEGORY_REQ:
                if(resultCode == Activity.RESULT_OK){
                    dialogIsVisible = false;
                    if(data != null && data.hasExtra(CATEGORY)) {
                        Log.d(TAG, "onActivityResult: CATEGORY HANDLE INTENT DATA");
                        mPresenter.addCategory(data.getStringExtra(CATEGORY));
                    }
                    else Log.d(TAG, "onActivityResult: CATEGORY NULL INTENT DATA");
                }
                else Log.d(TAG, "onActivityResult: ERROR RESULT CODE CATEGORY");
                break;
            case DIALOG_CATEGORY_REQ:
                if(resultCode == Activity.RESULT_OK) {
                    dialogIsVisible = false;
                    if (data != null && (data.hasExtra(SELECTED_CATEGORY)))
                        if(data.hasExtra(LONG_CLICK)) {
                            mPresenter.deleteCategory(data.getIntExtra(POS, -1), data.getStringExtra(SELECTED_CATEGORY));
                            Log.d(TAG, "onActivityResult: LONG_CLICK");
                        }
                        else
                            mPresenter.getSelectedCategory(data.getIntExtra(POS, -1),
                                data.getStringExtra(SELECTED_CATEGORY));
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //never used!
        Log.d(TAG, "onNothingSelected: ");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView tv;
        if (view != null && (tv = view.findViewById(R.id.category_item)) != null) {
            if (tv.getText().toString().contains(getString(R.string.add_other)))
                showDialog(DIALOG_ADD_CATEGORY,tv.getId());
            else Log.d(TAG, "onItemSelected: "+tv.getText().toString());
        }
        Log.d(TAG, "onItemSelected: position:"+position);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: id:" + getResources().getResourceName(v.getId()));
        switch (v.getId()) {
            case R.id.save_edit_task:
                boolean isReplay = !(repeatTv.getText().toString().equals(getString(R.string.dont_repeat)));
                mPresenter.saveNewTask(title.getText().toString(),email.getText().toString(),aSwitchAllDay.isChecked(),
                         Integer.parseInt((String)priorityS.getSelectedItem()),category.getText().toString(),startDate.getText().toString(),
                        startTime.getText().toString(),endDate.getText().toString(),endTime.getText().toString(),
                        repeatTv.getText().toString(),isReplay,
                        descriptionTv.getText().toString(),getColorId(colorS.getSelectedItemPosition()),personalizedState,childPersonalizedState);
                break;
            case R.id.starDateText:
                if (!dialogIsVisible)
                    showDialog(DIALOG_DATE_P, v.getId());
                break;
            case R.id.endDateText:
                if (!dialogIsVisible)
                    showDialog(DIALOG_DATE_P, v.getId());
                break;
            case R.id.starHourText:
                if (!dialogIsVisible)
                    showDialog(DIALOG_TIME_P, v.getId());
                break;
            case R.id.endHourText:
                if (!dialogIsVisible)
                    showDialog(DIALOG_TIME_P, v.getId());
                break;
            case R.id.repeatLayout:
                if (!dialogIsVisible)
                    showDialog(DIALOG_PERIOD_P, v.getId());
                break;
            case R.id.allDayLayout:
                aSwitchAllDay.setChecked(!aSwitchAllDay.isChecked());
                break;
            case R.id.categoryLayout:
            case R.id.selected_category:
                if(!dialogIsVisible)
                    showDialog(DIALOG_CATEGORY,v.getId());
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String principalTime = viewclickedId == R.id.starDateText ?
                startTime.getText().toString() : endTime.getText().toString();
        String date = viewclickedId == R.id.starDateText ?
                endDate.getText().toString() : startDate.getText().toString();
        String time = viewclickedId == R.id.starHourText ?
                endTime.getText().toString() : startTime.getText().toString();
        if(mPresenter != null && mPresenter.getView()!=null)
            mPresenter.onChangeDatePicker(year, month, dayOfMonth, new String[]{principalTime, date, time}, viewclickedId == R.id.starDateText);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        dialogIsVisible = false;
        String principalDate = viewclickedId == R.id.starDateText ?
                startDate.getText().toString() : endDate.getText().toString();
        String date = viewclickedId == R.id.starDateText ?
                endDate.getText().toString() : startDate.getText().toString();
        String time = viewclickedId == R.id.starHourText ?
                endTime.getText().toString() : startTime.getText().toString();
        if(mPresenter != null && mPresenter.getView()!=null)
            mPresenter.onChangeTimePicker(hourOfDay, minute, new String[]{principalDate, date, time},
                viewclickedId == R.id.starHourText);
    }

    @Override
    protected void initViews() {
        saveTaskBtn = getActivity().findViewById(R.id.save_edit_task);
        cancelTaskBtn = getActivity().findViewById(R.id.cancel_edit_task);
        category = getActivity().findViewById(R.id.selected_category);
        title = getActivity().findViewById(R.id.title_task);
        email = getActivity().findViewById(R.id.email_tv);
        aSwitchAllDay = getActivity().findViewById(R.id.switchAllDay);
        startDate = getActivity().findViewById(R.id.starDateText);
        endDate = getActivity().findViewById(R.id.endDateText);
        startTime = getActivity().findViewById(R.id.starHourText);
        endTime = getActivity().findViewById(R.id.endHourText);
        repeatTv = getActivity().findViewById(R.id.repeat_tv);
        descriptionTv = getActivity().findViewById(R.id.description_tv);
        LinearLayout repeatL = getActivity().findViewById(R.id.repeatLayout);
        LinearLayout allDayL = getActivity().findViewById(R.id.allDayLayout);
        LinearLayout categoryL = getActivity().findViewById(R.id.categoryLayout);
        colorS = getActivity().findViewById(R.id.colorSpinner);
        priorityS = getActivity().findViewById(R.id.prioritySpinner);
        saveTaskBtn.setOnClickListener(this);
        cancelTaskBtn.setOnClickListener(this);
        aSwitchAllDay.setOnCheckedChangeListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        repeatL.setOnClickListener(this);
        category.setOnClickListener(this);
        categoryL.setOnClickListener(this);
        allDayL.setOnClickListener(this);
        textColor = startDate.getTextColors();
        email.setText("PROCODIO@DIOCANE.COM");
        Log.d(TAG, "initViews: textColor: "+textColor.getColorForState(null,ActivityInfo.COLOR_MODE_DEFAULT));
        Log.d(TAG, "initViews: "+String.format("#%09X", (0xFFFFFFFF & 603979776)));
        Log.d(TAG, "initViews: "+String.format("#%06X", (0xFFFFFF & -1979711488)));
        List<String> priority = Arrays.asList(getResources().getStringArray(R.array.priority));
        TypedArray colorsName = getResources().obtainTypedArray(R.array.color_name);
        TypedArray colorsImage = getResources().obtainTypedArray(R.array.color_image);
        SpinnerAdapter priorityAdapter = new SpinnerAdapter(getContext(), priority, TYPE_DATA_0);
        colorAdapter = new SpinnerAdapter(getContext(), colorsImage, colorsName);
        priorityS.setAdapter(priorityAdapter);
        colorS.setAdapter(colorAdapter);
        colorS.setOnItemSelectedListener(this);
        priorityS.setOnItemSelectedListener(this);
        mPresenter.fetchAllCategories();
    }

    @Override
    protected void setUp(Bundle data) {
        if(data!=null) {
            Log.d(TAG, "setUp: data:"+data);
            if(data.containsKey(EDIT_STATE)){
                Log.d(TAG, "setUp: edit:"+((EditInstanceState)data.getParcelable(EDIT_STATE)).toString());
                EditInstanceState state = data.getParcelable(EDIT_STATE);
                title.setText(state.getmTitle());
                email.setText(state.getmEmail());
                aSwitchAllDay.setChecked(state.ismAllDay());
                startDate.setText(state.getmStartD());
                endDate.setText(state.getmEndD());
                startTime.setText(state.getmStartH());
                endTime.setText(state.getmEndH());
                periodSelection = state.getmPeriodPosition();
                repeatTv.setText(state.getmPeriod());
                category.setText(state.getmCategory());
                categorySelection = state.getmCategoryPosition();
                colorS.setSelection(state.getmColor());
                priorityS.setSelection(state.getmPriority());
                descriptionTv.setText(state.getmDescription());
                errorDate = state.isErrorDate();
                errorDate();
                setNotFocusableDateAndHour();
                dialogParams.putBundle(PERIOD_PARAMS,updateRecyclerDialog(periodSelection,state.getmPeriod(),PERIOD_PARAMS));
                dialogParams.putBundle(CATEGORY_PARAMS, updateRecyclerDialog(categorySelection, state.getmCategory(), CATEGORY_PARAMS));
            }
            if(data.containsKey(CHILD_PERSONALIZED_STATE) && data.containsKey(PERSONALIZED_STATE)){
                childPersonalizedState = data.getParcelable(CHILD_PERSONALIZED_STATE);
                personalizedState = data.getParcelable(PERSONALIZED_STATE);
                int repetition = personalizedState.getNumberPeriodRepetition();
                mPresenter.createRepeatedPeriodText(personalizedState.getNumberPeriodRepetition(),
                        personalizedState.getPeriodNameSelection(),
                        childPersonalizedState.getDaysSelection(),
                        childPersonalizedState.getMonthSpinner(),
                        childPersonalizedState.getMonthSpinnerId(),
                        childPersonalizedState.isAfterBtn(),
                        childPersonalizedState.isTheDayBtn(),
                        repetition > 1 ? getResources().getStringArray(R.array.period_type) :
                                getResources().getStringArray(R.array.period_type_singular),
                        getString(R.string.it_repeats_every_period),Integer.parseInt(childPersonalizedState.getNumberOccorenceEdit()),childPersonalizedState.getEndDayTv());
            }
        }
        else {
            Log.d(TAG, "setUp: data: null");
            Calendar c = Calendar.getInstance();
            String date = DateUtils.formatDate(c.getTime());
            c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 30);
            String startH = DateUtils.formatTime(c.getTime());
            c.set(Calendar.HOUR, c.get(Calendar.HOUR) + 1);
            String endH = DateUtils.formatTime(c.getTime());
            startDate.setText(date);
            endDate.setText(date);
            startTime.setText(startH);
            endTime.setText(endH);
            priorityS.setSelection(0);
            colorS.setSelection(colorAdapter.getCount() - 1);
        }

    }


    private Parcelable saveState() {
        return new EditInstanceState(
                title.getText().toString(),
                email.getText().toString(),
                aSwitchAllDay.isChecked(),
                startDate.getText().toString(),
                endDate.getText().toString(),
                startTime.getText().toString(),
                endTime.getText().toString(),
                repeatTv.getText().toString(),
                periodSelection,
                category.getText().toString(),
                categorySelection,
                colorS.getSelectedItemPosition(),
                priorityS.getSelectedItemPosition(),
                descriptionTv.getText().toString(),errorDate);
    }


    private void showDialog(String typePicker, int viewId) {
        dialogIsVisible = true;
        viewclickedId = viewId;
        if (typePicker == DIALOG_PERIOD_P || typePicker == DIALOG_ADD_CATEGORY || typePicker == DIALOG_CATEGORY) {
            DialogFragment df;
            switch (typePicker){
                case DIALOG_PERIOD_P:
                    df = DialogPeriodsFragment.getInstance(dialogParams);
                    df.setTargetFragment(EditTaskFragment.this, DIALOG_PERIOD_REQ);
                    df.show(getFragmentManager(),DIALOG_PERIOD_P);
                    break;
                case DIALOG_ADD_CATEGORY:
                    df = DialogNewCategoryFragment.getInstance(null);
                    df.setTargetFragment(EditTaskFragment.this, DIALOG_ADD_CATEGORY_REQ);
                    df.show(getFragmentManager(),DIALOG_ADD_CATEGORY);
                    break;
                case DIALOG_CATEGORY:
                    Bundle b = new Bundle();
                    b.putStringArrayList(LIST_CATEGORIES, (ArrayList<String>) categoriesList);
                    df = dialogParams.containsKey(CATEGORY_PARAMS)?
                            DialogShowCategoryFragment.getInstance(dialogParams):
                            DialogShowCategoryFragment.getInstance(b);

                    df.setTargetFragment(EditTaskFragment.this, DIALOG_CATEGORY_REQ);
                    df.show(getFragmentManager(),DIALOG_CATEGORY);
                    break;
            }

        }
        else if (typePicker == DIALOG_DATE_P || typePicker == DIALOG_TIME_P) {
            Calendar c = Calendar.getInstance();
            Dialog dialog;
            if (typePicker == DIALOG_DATE_P)
                dialog = new DatePickerDialog(getContext(), this,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
            else
                dialog = new TimePickerDialog(getContext(), this,
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialogIsVisible = false;
                }
            });
            dialog.show();
        }
    }

    private void errorDate() {
        if (errorDate) {
            startDate.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            startTime.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        } else {
            startDate.setTextColor(textColor);
            startTime.setTextColor(textColor);
        }
    }

    private Bundle updateRecyclerDialog(int position, String itemText,String type) {
        Bundle res = new Bundle();
        res.putInt(POS, position);

        if (type.contains(CATEGORY_PARAMS)){
            res.putStringArrayList(LIST_CATEGORIES, (ArrayList<String>) categoriesList);
        }
        else {
            if(position == 0 && !itemText.equals(getString(R.string.dont_repeat)))
                res.putString(PERSONALIZED_PERIOD,itemText);
//            if( position == PERSONALIZED_POS)
//                res.putString(PERSONALIZED_PERIOD, itemText);
        }
        return res;
    }

    private void setNotFocusableDateAndHour(){
        boolean isChecked = aSwitchAllDay.isChecked();
        if(isChecked){
            startDate.setClickable(false);
            startTime.setClickable(false);
            endDate.setClickable(false);
            endTime.setClickable(false);
            startDate.setTextColor(ContextCompat.getColor(getContext(),R.color.colorLightGrey));
            startTime.setTextColor(ContextCompat.getColor(getContext(),R.color.colorLightGrey));
            endDate.setTextColor(ContextCompat.getColor(getContext(),R.color.colorLightGrey));
            endTime.setTextColor(ContextCompat.getColor(getContext(),R.color.colorLightGrey));
        }
        else{
            startDate.setClickable(true);
            startTime.setClickable(true);
            endDate.setClickable(true);
            endTime.setClickable(true);
            startDate.setTextColor(textColor);
            startTime.setTextColor(textColor);
            endDate.setTextColor(textColor);
            endTime.setTextColor(textColor);
        }
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: id:"+getResources().getResourceName(item.getItemId()));
        switch (item.getItemId()){
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: ");
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        setNotFocusableDateAndHour();
    }

    private String getColorId(int selectionPosition){
        String colorName = getResources().getStringArray(R.array.color_name)[selectionPosition];
        int id=-1;
        switch (colorName){
            case "Red":
                id = R.color.red;
                break;
            case "Orange":
                id = R.color.orange;
                break;
            case "Yellow":
                id = R.color.yellow;
                break;
            case "Dark green":
                id = R.color.darkGreen;
                break;
            case "Green":
                id = R.color.green;
                break;
            case "Dark purple":
                id = R.color.darkPurple;
                break;
            case "Light purple":
                id = R.color.lightPurple;
                break;
            case "Purple":
                id = R.color.purple;
                break;
            case "Pink":
                id = R.color.pink;
                break;
            case "Grey":
                id = R.color.grey;
                break;
            case "Light blue":
            case "Default color":
                id = R.color.lightBlue;
                break;

        }
        return Integer.toString(id);
    }



    private Bundle buildDataToSend(Bundle data,String name,Parcelable parcelable){
        data.putParcelable(name,parcelable);
        return data;
    }

    private void registerReciever(){
        mReciever = new NotificationReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("it.guaraldi.to_dotaskmanager.ADD_NOTIFICATION_ALLARM");
//        filter.addAction("it.guaraldi.to_dotaskmanager.ONGOING_TASK");
//        filter.addAction("it.guaraldi.to_dotaskmanager.POSTPONE_TASK");
        Intent intent = getActivity().registerReceiver(mReciever,new IntentFilter());
        Log.d(TAG, "registerReciever: registrato intent="+intent);
    }

}



