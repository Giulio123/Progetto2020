package it.guaraldi.to_dotaskmanager.ui.edittask.personalized.child;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.Calendar;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.adapter.MonthAdapter;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.PersonalizedFragment;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.PersonalizedInstanceState;
import it.guaraldi.to_dotaskmanager.util.ActivityUtils;

import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.CHILD_PERSONALIZED_STATE;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.PERSONALIZED_STATE;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.REPLY_TASK;


public class ChildPersonalizedFragment extends BaseFragment implements ChildPersonalizedContract.View, PersonalizedFragment.PersonalizedCallback,View.OnClickListener, AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener {

    private static final String TAG = "ChildPersonalizedFragment";
    private int typeOccurence;
    private String startDate;
    private String monthSpinnerSelection;
    private int monthSpinnerSelectionId;
    private RadioButton neverBtn;
    private RadioButton theDayBtn;
    private RadioButton afterBtn;
    private TextView theDayTv;
    private EditText numberOccorenceEdit;
    private TextView mondayTv;
    private TextView tuesdayTv;
    private TextView wendesdayTv;
    private TextView thursdayTv;
    private TextView fridayTv;
    private TextView saturdayTv;
    private TextView sundayTv;
    private Spinner monthSpinner;
    private ColorStateList textcolor;
    private boolean dialogIsVisible;
    @Inject
    ChildPersonalizedPresenter mPresenter;



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dialogIsVisible = false;
        if(mPresenter!=null && mPresenter.getView()!=null)
        mPresenter.updateLastDay(year,month,dayOfMonth);
    }

    @Override
    public void loadMonthSpinnerData(String [] data) {
        MonthAdapter adapter = new MonthAdapter(getContext(),data);

        monthSpinner.setAdapter(adapter);
        monthSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void loadLastDay(String date) {
        theDayTv.setText(date);
    }

    @Override
    public void loadCurrentDay(int dayPosition) {
        setCheckedCurrentDay(dayPosition);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NewsApp.getNewsComponent().inject(this);
        Log.d(TAG, "onCreateView: ");
        return choiceView(inflater,container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        if(mPresenter!=null)
        mPresenter.attachView(this);
        initViews();
        setUp(getArguments());
    }



    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }


    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if(mPresenter!=null) {
            mPresenter.detachView(this);
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(view != null){
            monthSpinnerSelection = ((TextView)view.findViewById(R.id.month_spinner_item_tv)).getText().toString();
            monthSpinnerSelectionId = position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        setFocusableOccurencesEditText(false);
        switch (v.getId()){
            case R.id.monday_input:
            case R.id.tuesday_input:
            case R.id.wendesday_input:
            case R.id.thursday_input:
            case R.id.friday_input:
            case R.id.saturday_input:
            case R.id.sunday_input:
                changeBgOfDaySelected(v.getId());
                break;
            case R.id.month_spinner_personalized:
                break;
            case R.id.never_radio:
            case R.id.the_day_radio:
            case R.id.the_day_radio_layout:
            case R.id.after_radio:
            case R.id.after_radio_layout:
            case R.id.after_number_occurrence_edit:
                customRadioGroup(v.getId());
                break;
            case R.id.the_day_tv:
                if(theDayBtn.isChecked() && !dialogIsVisible)
                    showDialog();
                else
                    customRadioGroup(v.getId());
                break;
        }
    }

    @Override
    protected void initViews() {
        if(typeOccurence == 1){
            mondayTv = getActivity().findViewById(R.id.monday_input);
            tuesdayTv = getActivity().findViewById(R.id.tuesday_input);
            wendesdayTv = getActivity().findViewById(R.id.wendesday_input);
            thursdayTv = getActivity().findViewById(R.id.thursday_input);
            fridayTv = getActivity().findViewById(R.id.friday_input);
            saturdayTv = getActivity().findViewById(R.id.saturday_input);
            sundayTv = getActivity().findViewById(R.id.sunday_input);
            mondayTv.setOnClickListener(this);
            tuesdayTv.setOnClickListener(this);
            wendesdayTv.setOnClickListener(this);
            thursdayTv.setOnClickListener(this);
            fridayTv.setOnClickListener(this);
            saturdayTv.setOnClickListener(this);
            sundayTv.setOnClickListener(this);
            textcolor = mondayTv.getTextColors();
        }
        if(typeOccurence == 2)
            monthSpinner = getActivity().findViewById(R.id.month_spinner_personalized);
        LinearLayout theDayL =getActivity().findViewById(R.id.the_day_radio_layout);
        LinearLayout afterL =getActivity().findViewById(R.id.after_radio_layout);
        neverBtn = getActivity().findViewById(R.id.never_radio);
        theDayBtn = getActivity().findViewById(R.id.the_day_radio);
        afterBtn = getActivity().findViewById(R.id.after_radio);
        theDayTv = getActivity().findViewById(R.id.the_day_tv);
        numberOccorenceEdit = getActivity().findViewById(R.id.after_number_occurrence_edit);
        theDayL.setOnClickListener(this);
        afterL.setOnClickListener(this);
        numberOccorenceEdit.setOnClickListener(this);
        neverBtn.setOnClickListener(this);
        neverBtn.setChecked(true);
        theDayBtn.setOnClickListener(this);
        afterBtn.setOnClickListener(this);
        theDayTv.setOnClickListener(this);
        dialogIsVisible = false;
    }

    private View choiceView(LayoutInflater inflater, ViewGroup container) {
        getLayout();
        switch (typeOccurence){
            case 0:
            case 3:
                return inflater.inflate(R.layout.child_personalized_fragment_day_year, container, false);
            case 1:
                return inflater.inflate(R.layout.child_personalized_fragment_week, container, false);
            case 2:
                return inflater.inflate(R.layout.child_personalized_fragment_month, container, false);
            default:
                return null;
        }
    }


    private Parcelable saveState() {
        Log.d(TAG, "saveState: ");
        ChildPersonalizedInstanceState state = null;
        boolean [] daysSelection = new boolean[7];
        switch (typeOccurence){
            case 0:
            case 3://DAY AND YEAR
                Arrays.fill(daysSelection,Boolean.FALSE);
                state = new ChildPersonalizedInstanceState(
                        neverBtn.isChecked(),
                        theDayBtn.isChecked(),
                        afterBtn.isChecked(),
                        theDayBtn.isChecked() ? theDayTv.getText().toString():null,
                        afterBtn.isChecked()? numberOccorenceEdit.getText().toString():"1",
                        null,-1,
                        daysSelection);
                break;
            case 1://WEEK
                state = new ChildPersonalizedInstanceState(
                        neverBtn.isChecked(),
                        theDayBtn.isChecked(),
                        afterBtn.isChecked(),
                        theDayBtn.isChecked() ? theDayTv.getText().toString():null,
                        afterBtn.isChecked()? numberOccorenceEdit.getText().toString():"1",
                        null,-1,
                        getCurrentDaysState());
                break;
            case 2://MONTH
                Arrays.fill(daysSelection,Boolean.FALSE);
                state = new ChildPersonalizedInstanceState(
                        neverBtn.isChecked(),
                        theDayBtn.isChecked(),
                        afterBtn.isChecked(),
                        theDayBtn.isChecked() ? theDayTv.getText().toString():null,
                        afterBtn.isChecked()? numberOccorenceEdit.getText().toString():"1",
                        monthSpinnerSelection,monthSpinnerSelectionId,
                        daysSelection);
                break;
        }
        return state;
    }

    @Override
    protected void setUp(Bundle data) {
        Log.d(TAG, "setUp: "+data);
        if(data!=null) {
            if (data.containsKey(PERSONALIZED_STATE)) {
                Log.d(TAG, "setUp: personalized:"+((PersonalizedInstanceState)data.getParcelable(PERSONALIZED_STATE)).toString());
                PersonalizedInstanceState state = data.getParcelable(PERSONALIZED_STATE);
                startDate = state.getDate();
                if(typeOccurence == 2)
                    mPresenter.calculateMonthOccurrences(startDate,getString(R.string.every_month),getResources().getStringArray(R.array.days_occurrences_in_month));
                mPresenter.setUpLastDay(startDate);
            }
            if (data.containsKey(CHILD_PERSONALIZED_STATE)) {
                Log.d(TAG, "setUp: child:"+((ChildPersonalizedInstanceState)data.getParcelable(CHILD_PERSONALIZED_STATE)).toString());
                ChildPersonalizedInstanceState childState = data.getParcelable(CHILD_PERSONALIZED_STATE);
                neverBtn.setChecked(childState.isNeverBtn());
                theDayBtn.setChecked(childState.isTheDayBtn());
                afterBtn.setChecked(childState.isAfterBtn());
                if(childState.getEndDayTv()!=null)
                    theDayTv.setText(childState.getEndDayTv());
                numberOccorenceEdit.setText(childState.getNumberOccorenceEdit());

                if(typeOccurence == 1) {
                    restoreDaySelection(childState.getDaysSelection());
                    if(childState.allDayUnchecked())
                        mPresenter.updateCurrentDay(startDate);
                }
                if(typeOccurence == 2)
                    monthSpinner.setSelection(childState.getMonthSpinnerId());
            }
            else {
                if(typeOccurence == 1){
                    boolean [] daysSelection = new boolean[7];
                    Arrays.fill(daysSelection,Boolean.FALSE);
                    restoreDaySelection(daysSelection);
                    mPresenter.updateCurrentDay(startDate);
                }
            }

        }
    }

    private void setCheckedCurrentDay(int position){
        TypedArray tvId = getResources().obtainTypedArray(R.array.days_of_week_id);
        int id = tvId.getResourceId(position,-1);
        changeBgOfDaySelected(id);
    }
    private void changeBgOfDaySelected(int id){
        TextView tv  = getActivity().findViewById(id);
        if(!dayIsChecked(id)){
            Log.d(TAG, "setBackGroundTv: DEFAULT");
            tv.setBackground(getResources().getDrawable(R.drawable.bg_text_view_circle_selected));
            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }
        else {
            Log.d(TAG, "setBackGroundTv: SELECTED");
            tv.setBackground(getResources().getDrawable(R.drawable.bg_text_view_circle));
            tv.setTextColor(textcolor);
        }
    }

    private boolean dayIsChecked(int id){
        TextView tv = getActivity().findViewById(id);
        Drawable.ConstantState tvBg = tv.getBackground().getConstantState();
        Drawable.ConstantState defaultValue =getResources().getDrawable(R.drawable.bg_text_view_circle)
                .getConstantState();
        return !tvBg.equals(defaultValue);
    }

    private boolean[] getCurrentDaysState(){
        TypedArray tvId = getResources().obtainTypedArray(R.array.days_of_week_id);
        boolean [] res = new boolean[tvId.length()];
        Arrays.fill(res,false);
        for(int i = 0; i < tvId.length(); i++)
                res[i] = dayIsChecked(tvId.getResourceId(i,-1)) ? true:false;
        return res;
    }

    private void customRadioGroup(int id){
        Log.d(TAG, "customRadioGroup: "+getResources().getResourceName(id));
        switch (id){
            case R.id.never_radio:
                theDayBtn.setChecked(false);
                afterBtn.setChecked(false);
                break;
            case R.id.the_day_radio:
            case R.id.the_day_tv:
            case R.id.the_day_radio_layout:
                theDayBtn.setChecked(true);
                neverBtn.setChecked(false);
                afterBtn.setChecked(false);
                break;
            case R.id.after_radio:
            case R.id.after_number_occurrence_edit:
            case R.id.after_radio_layout:
                afterBtn.setChecked(true);
                neverBtn.setChecked(false);
                theDayBtn.setChecked(false);
                setFocusableOccurencesEditText(true);
                break;
        }
    }

    private void showDialog() {
        dialogIsVisible = true;
        Calendar c = Calendar.getInstance();
        Dialog dialog = new DatePickerDialog(getContext(),this,
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogIsVisible = false;
            }
        });
        dialog.show();
    }

    private void setFocusableOccurencesEditText(boolean focusable){
        numberOccorenceEdit.setFocusableInTouchMode(focusable);
        numberOccorenceEdit.setFocusable(focusable);
    }

    private void restoreDaySelection(boolean[] stateSelection){
        TypedArray resIdArray = getResources().obtainTypedArray(R.array.days_of_week_id);
        for(int i = 0; i < stateSelection.length; i++){
            if(stateSelection[i])
                changeBgOfDaySelected(resIdArray.getResourceId(i,-1));
        }
    }

    private void getLayout(){
       Bundle args = getArguments();
        Log.d(TAG, "getLayout: "+getArguments());
        if(args!=null && args.containsKey(PERSONALIZED_STATE)){
            PersonalizedInstanceState state =  args.getParcelable(PERSONALIZED_STATE);
            typeOccurence = state.getPeriodSelectedPosition();
        }
    }


    @Override
    public void close(String btnClicked) {
        Log.d(TAG, "close: "+btnClicked);

        Intent intent = new Intent();
        if(btnClicked.equals("RepetitionChanged")){
            intent.putExtra(CHILD_PERSONALIZED_STATE, saveState());
            getTargetFragment().onActivityResult(ActivityUtils.REPETITION_CHANGED_REQ, android.app.Activity.RESULT_OK, intent);
        }
        if(btnClicked.equals(getString(R.string.save_btn))) {
            intent.putExtra(CHILD_PERSONALIZED_STATE, saveState());
            getTargetFragment().onActivityResult(ActivityUtils.CHILD_PERS_REQ, android.app.Activity.RESULT_OK, intent);
        }
        if(btnClicked.equals(getString(R.string.cancel_btn)))
            getTargetFragment().onActivityResult(ActivityUtils.CHILD_PERS_REQ, Activity.RESULT_CANCELED, intent);
    }
}
