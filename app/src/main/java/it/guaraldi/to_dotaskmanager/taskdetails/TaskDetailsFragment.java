package it.guaraldi.to_dotaskmanager.taskdetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.notification.Const;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;
import it.guaraldi.to_dotaskmanager.utils.ActivityUtils;

public class TaskDetailsFragment extends BaseFragment implements TaskDetailsContract.View{

    @Inject TaskDetailsPresenter mPresenter;
    private Intent mIntent;
    private Toolbar mToolbar;
    private TextView mTitleTTV;
    private TextView mDurationTTV;
    private TextView 

    private static final String TAG = "TaskDetailsFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NewsApp.getNewsComponent().inject(this);
        mPresenter.attachView(this);
        return inflater.inflate(R.layout.edit_task_details_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        if((mIntent=getActivity().getIntent())!=null )
            if(mIntent.getAction() == Const.DETAILS_TASK_F){
                Log.d(TAG, "onViewCreated: intentAction ="+mIntent.getAction());

                int taskId = mIntent.getBundleExtra(Const.TASK_DATA).getInt(Const.TASK_ID);
                    mPresenter.getTaskById(taskId);
                //TODO CREA VIEW + RIEMPI CAMPI
            }
        setUp(getArguments());
        initViews();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null){
            mPresenter.detachView(this);
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mToolbar = getActivity().findViewById(R.id.toolbar_view_details);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
        mTitleTTV = getActivity().findViewById(R.id.title_taskD);
        mDurationTTV = getActivity().findViewById(R.id.duration_task);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar_menu_task_details,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_modify_task:
                break;
            case R.id.action_delete_task:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setUp(Bundle data) {
        if(data!=null){
            int taskId =data.getInt(ActivityUtils.ID_TASK);
                mPresenter.getTaskById(taskId);
        }

    }

    @Override
    public void updateViewTaskData(String title, String duration, int priority, String category, String description, String color, String status) {
        mTitleTTV.setText(title);
        mDurationTTV.setText("");
        Log.d(TAG, "updateViewTaskData: DEVI IMPLEMENTARE UPDATE");
    }

    @Override
    public void showCalendarView() {
        Navigation.findNavController(getView()).navigate(R.id.action_taskDetailsFragment_to_calendarFragment);
    }

    @Override
    public void showEditTaskView() {
        Navigation.findNavController(getView()).navigate(R.id.action_taskDetailsFragment_to_editTaskFragment);
    }
}
