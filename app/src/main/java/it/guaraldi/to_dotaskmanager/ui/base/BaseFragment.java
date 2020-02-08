package it.guaraldi.to_dotaskmanager.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.Unbinder;
import it.guaraldi.to_dotaskmanager.R;

public abstract class BaseFragment extends Fragment implements IBaseView{
    private BaseActivity mActivity;
    private Unbinder mUnBinder;

    protected final static int NO_SELECTION = -1;
    protected final static String RESTORE_STATE = "RESTORE_STATE";
    protected final static String ARGS = "ARGS";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
        }
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    protected abstract void initViews();
    protected abstract void setUp(Bundle data);


    public void setUnbinder(Unbinder unbinder) {
        mUnBinder = unbinder;
    }

    @Override
    public void onDestroyView() {
        if (mUnBinder != null) mUnBinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void showLoading() {
        mActivity.showLoading();
    }

    @Override
    public void hideLoading() {
        mActivity.hideLoading();
    }

    @Override
    public void showError(String errorMessage) {
        mActivity.showError(errorMessage);
    }

    @Override
    public void showError(int errorId) {
        mActivity.showError(errorId);
    }

}

