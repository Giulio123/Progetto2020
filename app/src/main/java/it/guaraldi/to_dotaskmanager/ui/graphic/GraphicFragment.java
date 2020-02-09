package it.guaraldi.to_dotaskmanager.ui.graphic;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.notification.Const;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class GraphicFragment extends BaseFragment implements GraphicContract.View {

    @Inject
    public GraphicPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NewsApp.getNewsComponent().inject(this);
        return inflater.inflate(R.layout.graphic_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mPresenter != null)
            mPresenter.attachView(this);
        mPresenter.getAllTasksByCategory("Work");
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
    protected void initViews() {

    }

    @Override
    protected void setUp(Bundle data) {

    }

    @Override
    public void getTasksForGraphic(String category, int []totalTask, int []taskPending , int [] taskComplete) {
        for(int i=0; i<totalTask.length;i++){
            Log.d(TAG, "getTasksForGraphic: category="+category);
            Log.d(TAG, "getTasksForGraphic: Total="+totalTask[i]+" Pending="+taskPending[i]+" " +
            "Complete="+taskComplete[i]+" priority="+(i+1));

        }
                Log.d(TAG, "getTasksForGraphic: DATI PER CREARE I GRAFICI");
    }
}
