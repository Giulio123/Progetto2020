package it.guaraldi.to_dotaskmanager.taskdetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.notification.Const;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;

public class TaskDetailsFragment extends BaseFragment implements TaskDetailsContract.View{

    @Inject TaskDetailsPresenter mPresenter;
    private Intent mIntent;
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

    }

    @Override
    protected void setUp(Bundle data) {

    }

    @Override
    public void updateViewTaskData(String title, String email, int priority, String category, String start, String end, String description, String color, String status) {
        Log.d(TAG, "updateViewTaskData: DEVI IMPLEMENTARE UPDATE");
    }
}
