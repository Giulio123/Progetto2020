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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import java.util.List;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.adapter.DetailsAdapter;
import it.guaraldi.to_dotaskmanager.notification.Const;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;
import it.guaraldi.to_dotaskmanager.utils.ActivityUtils;

public class TaskDetailsFragment extends BaseFragment implements TaskDetailsContract.View{

    @Inject TaskDetailsPresenter mPresenter;
    private Intent mIntent;
    private Toolbar mToolbar;
    private ListView mListDetails;
    private DetailsAdapter mDAdapter;
    private int mTaskId=-1;

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
                mTaskId = mIntent.getBundleExtra(Const.TASK_DATA).getInt(Const.TASK_ID);
                    mPresenter.getTaskById(mTaskId);
                //TODO CREA VIEW + RIEMPI CAMPI
            }
        Log.d(TAG, "onViewCreated: ");
        initViews();
        setUp(getArguments());
       
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
        Log.d(TAG, "initViews: ");
        mToolbar = getActivity().findViewById(R.id.toolbar_view_details);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
        mListDetails = getActivity().findViewById(R.id.list_details);
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
                mPresenter.modifyTask(mTaskId);
                break;
            case R.id.action_delete_task:
                mPresenter.deleteTaskById(mTaskId);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setUp(Bundle data) {
        Log.d(TAG, "setUp: ");
        if(data!=null){
            Log.d(TAG, "setUp: not null");
            mTaskId =data.getInt(ActivityUtils.ID_TASK);
                mPresenter.getTaskById(mTaskId);
        }

    }

    @Override
    public void updateViewTaskData(List<String> taskDetails) {
        mDAdapter = new DetailsAdapter(getActivity(),taskDetails);
        mListDetails.setAdapter(mDAdapter);
        Log.d(TAG, "updateViewTaskData: DEVI IMPLEMENTARE UPDATE description=");
    }

    @Override
    public void showCalendarView() {
        Navigation.findNavController(getView()).navigate(R.id.action_taskDetailsFragment_to_calendarFragment);
    }

    @Override
    public void showEditTaskView(Bundle data) {
        Navigation.findNavController(getView()).navigate(R.id.action_taskDetailsFragment_to_editTaskFragment,data);
    }
}
