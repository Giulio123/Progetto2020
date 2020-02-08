package it.guaraldi.to_dotaskmanager.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import butterknife.Unbinder;
import it.guaraldi.to_dotaskmanager.utils.DialogUtil;

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    protected ProgressDialog mProgressDialog;
    protected Unbinder mUnBinder;

    public void showLoading() {
        hideLoading();
        mProgressDialog = DialogUtil.showLoadingDialog(this);
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void showError(String errorMessage) {
        Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showError(int errorId) {
        Snackbar.make(findViewById(android.R.id.content), getString(errorId), Snackbar.LENGTH_LONG)
                .show();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    protected abstract void setUp();

    protected Class<?> classFromName(String name){

        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final void setAccountAuthenticatorResult(Bundle result) {
        throw new RuntimeException("Stub!");
    }
}
