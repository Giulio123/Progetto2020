package it.guaraldi.to_dotaskmanager;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import it.guaraldi.to_dotaskmanager.di.component.AppComponent;
import it.guaraldi.to_dotaskmanager.di.component.DaggerAppComponent;

import it.guaraldi.to_dotaskmanager.di.component.DaggerNewsComponent;
import it.guaraldi.to_dotaskmanager.di.component.NewsComponent;
import it.guaraldi.to_dotaskmanager.di.module.AppModule;
import it.guaraldi.to_dotaskmanager.di.module.NewsModule;


public class NewsApp extends Application{
    private static NewsComponent mNewsComponent;
    private static AppComponent appComponent;



    @Override
    public void onCreate() {
        super.onCreate();
        initializeDagger();
        AndroidThreeTen.init(this);
    }

    private void initializeDagger(){
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);
        mNewsComponent = DaggerNewsComponent.builder()
                .appComponent(appComponent)
                .newsModule(new NewsModule()).
                build();
    }

    public static  NewsComponent getNewsComponent() {
        return mNewsComponent;
    }

}