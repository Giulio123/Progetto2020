package it.guaraldi.to_dotaskmanager.ui.edittask.personalized;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.data.local.LocalDataSource;
import it.guaraldi.to_dotaskmanager.data.remote.RemoteDataSource;
import it.guaraldi.to_dotaskmanager.ui.base.BasePresenter;

public class PersonalizedPresenter extends BasePresenter<PersonalizedContract.View> implements PersonalizedContract.Presenter {

    private TasksRepository mRepository;

    @Inject
    public PersonalizedPresenter(TasksRepository repository){
        mRepository = repository;

    }

}
