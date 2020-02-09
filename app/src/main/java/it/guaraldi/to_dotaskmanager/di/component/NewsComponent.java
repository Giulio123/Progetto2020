package it.guaraldi.to_dotaskmanager.di.component;

import it.guaraldi.to_dotaskmanager.di.module.NewsModule;
import it.guaraldi.to_dotaskmanager.di.scope.NewsScope;
import dagger.Component;
import it.guaraldi.to_dotaskmanager.notification.NotificationReceiver;
import it.guaraldi.to_dotaskmanager.taskdetails.TaskDetailsFragment;
import it.guaraldi.to_dotaskmanager.ui.calendar.CalendarFragment;
import it.guaraldi.to_dotaskmanager.ui.edittask.EditTaskFragment;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.PersonalizedFragment;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.child.ChildPersonalizedFragment;
import it.guaraldi.to_dotaskmanager.ui.graphic.GraphicFragment;
import it.guaraldi.to_dotaskmanager.ui.login.LoginFragment;
import it.guaraldi.to_dotaskmanager.ui.registration.RegistrationFragment;

@NewsScope
@Component(modules = {NewsModule.class}, dependencies = {AppComponent.class})
public interface NewsComponent {
    void inject(CalendarFragment fragment);
    void inject(EditTaskFragment fragment);
    void inject(LoginFragment fragment);
    void inject(RegistrationFragment fragment);
    void inject(PersonalizedFragment fragment);
    void inject(ChildPersonalizedFragment fragment);
    void inject(TaskDetailsFragment taskDetailsFragment);
    void inject(NotificationReceiver notificationReceiver);
    void inject(GraphicFragment graphicFragment);
}
