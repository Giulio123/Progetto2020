package it.guaraldi.to_dotaskmanager.ui.base;

import androidx.annotation.StringRes;

public interface IBaseView {
    void showLoading();
    void hideLoading();
    void showError(String errorMessage);
    void showError(@StringRes int errorId);
}
