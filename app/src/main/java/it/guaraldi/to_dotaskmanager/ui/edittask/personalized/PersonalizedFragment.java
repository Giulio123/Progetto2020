package it.guaraldi.to_dotaskmanager.ui.edittask.personalized;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.adapter.PeriodAdapter;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;
import it.guaraldi.to_dotaskmanager.ui.edittask.EditInstanceState;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.child.ChildPersonalizedFragment;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.child.ChildPersonalizedInstanceState;
import it.guaraldi.to_dotaskmanager.utils.ActivityUtils;

import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.CHILD_PERSONALIZED_STATE;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.EDIT_STATE;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.EVERY_DAY_POS;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.EVERY_YEAR_POS;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.PERSONALIZED_STATE;

public class PersonalizedFragment extends BaseFragment implements PersonalizedContract.View, AdapterView.OnItemSelectedListener, View.OnClickListener, TextWatcher {

    private static final String TAG = "PersonalizedFragment";
    private Spinner mSpinnersTypePeriods;
    private PeriodAdapter mPeriodAdapter;
    private int periodPosSelected = -1;
    private String periodNameSelected = null;
    private String startDate;
    private Button saveBtn;
    private Button cancelBtn;
    private EditText numberReplyPeriod;
    private EditInstanceState editState = null;
    private PersonalizedInstanceState personalizedState = null;
    private ChildPersonalizedInstanceState childPersonalizedState = null;
    public PersonalizedCallback personalizedCallback;

    @Inject
    PersonalizedPresenter mPresenter;

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d(TAG, "beforeTextChanged: ");
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d(TAG, "onTextChanged: ");
    }

    @Override
    public void afterTextChanged(Editable editable) {

        if(editable.toString().length() > 0 ){
            int repetition = Integer.parseInt(editable.toString());
            if(repetition > 1){
                mPeriodAdapter = new PeriodAdapter(getContext(),
                        getResources().getStringArray(R.array.period_type));
                mSpinnersTypePeriods.setAdapter(mPeriodAdapter);
            }
            else {
                mPeriodAdapter = new PeriodAdapter(getContext(),
                        getResources().getStringArray(R.array.period_type_singular));
                mSpinnersTypePeriods.setAdapter(mPeriodAdapter);
            }
            //TODO MANTENERE LE SELEZIONI NEL CHILD PASSANDO PARAMENTRI
            mSpinnersTypePeriods.setSelection(periodPosSelected);
            if(personalizedCallback!=null)
                personalizedCallback.close("RepetitionChanged");
        }
        else Log.d(TAG, "afterTextChanged: editable: "+editable.toString());
    }

    public interface PersonalizedCallback{
        void close(String btnClicked);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NewsApp.getNewsComponent().inject(this);
        return inflater.inflate(R.layout.personalized_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mPresenter!=null)
        mPresenter.attachView(this);
        initViews();
        setUp(getArguments());
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "PORCODIO: size"+getFragmentManager().getPrimaryNavigationFragment());
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
    }

    @Override
    public void onDestroy() {
        if(mPresenter!=null) {
            mPresenter.detachView(this);
            mPresenter = null;
        }
        super.onDestroy();
    }



    @Override
    protected void setUp(Bundle data) {
        if(data!=null) {
            if (data.containsKey(PERSONALIZED_STATE)) {
                Log.d(TAG, "setUp: personalized:" + ((PersonalizedInstanceState) data.getParcelable(PERSONALIZED_STATE)).toString());
                personalizedState = data.getParcelable(PERSONALIZED_STATE);
                startDate = personalizedState.getDate();
                periodPosSelected = personalizedState.getPeriodSelectedPosition();
                periodNameSelected = personalizedState.getPeriodNameSelection();
                numberReplyPeriod.setText(Integer.toString(personalizedState.getNumberPeriodRepetition()));
                if (personalizedState.getNumberPeriodRepetition() > 1) {
                    mPeriodAdapter = new PeriodAdapter(getContext(),
                            getResources().getStringArray(R.array.period_type));
                    mSpinnersTypePeriods.setAdapter(mPeriodAdapter);
                }
                mSpinnersTypePeriods.setSelection(periodPosSelected);
            }
            if (data.containsKey(EDIT_STATE)) {
                Log.d(TAG, "setUp: edit:" + ((EditInstanceState)data.getParcelable(EDIT_STATE)).toString());
                editState = data.getParcelable(EDIT_STATE);
                if (!data.containsKey(PERSONALIZED_STATE)) {
                    startDate = editState.getmStartD();
                    periodPosSelected = editState.getmPeriodPosition();
                    Log.d(TAG, "setUp: position:"+periodPosSelected);
                    mSpinnersTypePeriods.setSelection(periodPosSelected > EVERY_DAY_POS && periodPosSelected <= EVERY_YEAR_POS ? periodPosSelected - 1 : 0);
                    periodNameSelected = (String)mSpinnersTypePeriods.getSelectedItem();
                }
            }
            if (data.containsKey(CHILD_PERSONALIZED_STATE)) {
                Log.d(TAG, "setUp: child:" + ((ChildPersonalizedInstanceState) data.getParcelable(CHILD_PERSONALIZED_STATE)).toString());
                childPersonalizedState = data.getParcelable(CHILD_PERSONALIZED_STATE);
            }
        }
    }

    @Override
    protected void initViews() {
        numberReplyPeriod = getActivity().findViewById(R.id.number_repetition);
        numberReplyPeriod.addTextChangedListener(this);
        saveBtn = (Button) getActivity().findViewById(R.id.save_personalized_btn);
        cancelBtn = (Button) getActivity().findViewById(R.id.cancel_personalized_btn);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        mSpinnersTypePeriods = getActivity().findViewById(R.id.period_spinner);
        mPeriodAdapter = new PeriodAdapter(getContext(),
                getResources().getStringArray(R.array.period_type_singular));
        mSpinnersTypePeriods.setAdapter(mPeriodAdapter);
        mSpinnersTypePeriods.setOnItemSelectedListener(this);
    }

    private void launchChild(){
        ChildPersonalizedFragment fragment = new ChildPersonalizedFragment();
        personalizedCallback = (PersonalizedCallback) fragment;
        Bundle args = new Bundle();
        PersonalizedInstanceState settings = new PersonalizedInstanceState(startDate,
                periodPosSelected,
                periodNameSelected,
                Integer.parseInt(numberReplyPeriod.getText().toString()));
        buildDataToSend(args,PERSONALIZED_STATE,settings);
        if(childPersonalizedState != null)
            buildDataToSend(args,CHILD_PERSONALIZED_STATE,childPersonalizedState);
        fragment.setArguments(args);
        fragment.setTargetFragment(PersonalizedFragment.this,ActivityUtils.CHILD_PERS_REQ);
        getFragmentManager().beginTransaction()
                .replace(R.id.second_frame,fragment,"CHILD_PERSONALIZED_FRAGMENT")
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case ActivityUtils.CHILD_PERS_REQ:
                Bundle args = new Bundle();
                NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.editTaskFragment,true).build();
                switch (resultCode){
                    case Activity.RESULT_OK:
                        updatePersonalizedState();
                        buildDataToSend(args,EDIT_STATE,editState);
                        buildDataToSend(args,PERSONALIZED_STATE,personalizedState);
                        buildDataToSend(args, CHILD_PERSONALIZED_STATE, data.getParcelableExtra(CHILD_PERSONALIZED_STATE));
                        Navigation.findNavController(getView()).
                                navigate(R.id.action_personalizedFragment_to_editTaskFragment,args,options);
                        break;
                    case Activity.RESULT_CANCELED:
                        if(personalizedState != null && childPersonalizedState != null) {
                            buildDataToSend(args,PERSONALIZED_STATE,personalizedState);
                            buildDataToSend(args,CHILD_PERSONALIZED_STATE,childPersonalizedState);
                        }
                        buildDataToSend(args,EDIT_STATE,editState);
                        Navigation.findNavController(getView()).
                                navigate(R.id.action_personalizedFragment_to_editTaskFragment,args,options);
                        break;
                }
                break;
            case ActivityUtils.REPETITION_CHANGED_REQ:
                if(resultCode == Activity.RESULT_OK){
                    if(data.hasExtra(CHILD_PERSONALIZED_STATE))
                        childPersonalizedState = data.getParcelableExtra(CHILD_PERSONALIZED_STATE);
                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(view != null) {
            periodPosSelected = position;
            periodNameSelected = (String)parent.getItemAtPosition(position);
            launchChild();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected: ");
    }

    private Bundle buildDataToSend(Bundle data,String name,Parcelable parcelable){
        data.putParcelable(name,parcelable);
        return data;
    }

    private void updatePersonalizedState(){
        if(personalizedState == null)
            personalizedState = new PersonalizedInstanceState(
                    startDate,
                    periodPosSelected,
                    periodNameSelected,
                    Integer.parseInt(numberReplyPeriod.getText().toString()));
        else {
            personalizedState.setNumberPeriodRepetition(Integer.parseInt(numberReplyPeriod.getText().toString()));
            personalizedState.setPeriodNameSelection(periodNameSelected);
            personalizedState.setPeriodSelectedPosition(periodPosSelected);
        }
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: "+getResources().getResourceName(view.getId()));
        switch (view.getId()){
            case R.id.save_personalized_btn:
                Log.d(TAG, "onClick: restart");
                personalizedCallback.close(saveBtn.getText().toString());
                break;
            case R.id.cancel_personalized_btn:
                personalizedCallback.close(cancelBtn.getText().toString());
                break;
        }
    }
}
