package it.guaraldi.to_dotaskmanager.di.module;

import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.di.scope.NewsScope;
import it.guaraldi.to_dotaskmanager.notification.NotificationISPresenter;
import it.guaraldi.to_dotaskmanager.taskdetails.TaskDetailsPresenter;
import it.guaraldi.to_dotaskmanager.ui.calendar.CalendarPresenter;
import dagger.Module;
import dagger.Provides;
import it.guaraldi.to_dotaskmanager.ui.edittask.EditTaskPresenter;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.PersonalizedPresenter;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.child.ChildPersonalizedPresenter;
import it.guaraldi.to_dotaskmanager.ui.graphic.GraphicPresenter;
import it.guaraldi.to_dotaskmanager.ui.login.LoginPresenter;
import it.guaraldi.to_dotaskmanager.ui.registration.RegistrationPresenter;

@Module
public class NewsModule {

    @Provides
    @NewsScope
    public CalendarPresenter providesCalendarPresenter(TasksRepository repository) {
        return new CalendarPresenter(repository);
    }

    @Provides
    @NewsScope
    public EditTaskPresenter providesEditTaskPresenter(TasksRepository repository) {
        return new EditTaskPresenter(repository);
    }

    @Provides
    @NewsScope
    public LoginPresenter providesLoginPresenter(TasksRepository repository) {
        return new LoginPresenter(repository);
    }

    @Provides
    @NewsScope
    public RegistrationPresenter providesRegistrationPresenter(TasksRepository repository) {
        return new RegistrationPresenter(repository);
    }

    @Provides
    @NewsScope
    public PersonalizedPresenter providesPersonalizedPresenter(TasksRepository repository) {
        return new PersonalizedPresenter(repository);
    }

    @Provides
    @NewsScope
    public ChildPersonalizedPresenter providesChildPersonalizedPresenter(TasksRepository repository) {
        return new ChildPersonalizedPresenter(repository);
    }

    @Provides
    @NewsScope
    public TaskDetailsPresenter providesTaskDetailsPresenter(TasksRepository repository){
        return new TaskDetailsPresenter(repository);
    }
    
    @Provides
    @NewsScope
    public GraphicPresenter providesGraphicPresenter(TasksRepository repository) {
        return new GraphicPresenter(repository);
    }

    @Provides
    @NewsScope
    public NotificationISPresenter providesNotificationISPresenter(TasksRepository repository){
        return new NotificationISPresenter(repository);
    }


}
