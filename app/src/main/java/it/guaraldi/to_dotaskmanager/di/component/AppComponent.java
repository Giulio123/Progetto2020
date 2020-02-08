package it.guaraldi.to_dotaskmanager.di.component;

import android.accounts.AccountManager;
import android.app.KeyguardManager;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.di.module.AppModule;
import javax.inject.Singleton;
import dagger.Component;


@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {
    void inject(NewsApp app);
    TasksRepository repository();

}
