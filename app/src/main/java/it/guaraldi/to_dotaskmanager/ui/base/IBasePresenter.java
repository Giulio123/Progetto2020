package it.guaraldi.to_dotaskmanager.ui.base;

public interface IBasePresenter<V extends IBaseView> {
    void attachView(V view);
    void detachView(V view);
    boolean isViewAttached();
}
