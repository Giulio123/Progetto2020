package it.guaraldi.to_dotaskmanager.ui.base;

import android.content.Context;
import android.graphics.Typeface;
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

    protected final String[] months = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected final String[] parties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    private static final int PERMISSION_STORAGE = 0;

    protected Typeface tfRegular;
    protected Typeface tfLight;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        return super.onCreateView(inflater, container, savedInstanceState);
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

